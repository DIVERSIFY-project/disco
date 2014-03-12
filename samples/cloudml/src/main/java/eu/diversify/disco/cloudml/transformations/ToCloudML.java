/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Disco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.cloudml.util.actions.DeploymentEngineer;
import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.shuffle;
import java.util.List;
import java.util.Random;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;


/**
 * Update a cloud model so as it reflect a given population, as much as possible
 *
 * @author Hui Song
 * @author Franck Chauvel
 *
 * @since 0.1
 */
public class ToCloudML {

    private final DeploymentEngineer deployer;
    private final Random random;

    public ToCloudML() {
        this.random = new Random();
        deployer = new DeploymentEngineer();
    }

    public DeploymentModel applyTo(DeploymentModel deployment, Population toBe) {
        abortIfInvalid(deployment);
        abortIfInvalid(toBe);

        for (final String specieName : toBe.getSpeciesNames()) {
            if (deployment.getNodeTypes().containsKey(specieName)) {
                adjustNodeInstanceCount(
                        deployment, 
                        deployment.findNodeByName(specieName), 
                        toBe.getNumberOfIndividualsIn(specieName));
            }
            else if (deployment.getArtefactTypes().containsKey(specieName)) {
                adjustArtefactInstanceCount(deployment, specieName, toBe.getNumberOfIndividualsIn(specieName));
            
            }
            else {
                final String message = String.format("Specie '%s' is neither a node nor an artefact!", specieName);
                throw new RuntimeException(message);
            }
            
        }
        return deployment;
    }


    private void abortIfInvalid(DeploymentModel deployment) {
        if (deployment == null) {
            throw new IllegalArgumentException("Cannot convert 'null' as input deployment model");
        }
    }

    private void abortIfInvalid(Population toBe) {
        if (toBe == null) {
            throw new IllegalArgumentException("Cannot exploit 'null' as a target population configuration");
        }
    }

    private void adjustNodeInstanceCount(DeploymentModel deployment, final Node nodeType, int desired) {
        final Collection<NodeInstance> existings = findNodeInstancesByType(nodeType, deployment);
        final int error = desired - existings.size();
        if (error < 0) {
            removeExcessiveNodeInstances(existings, error, deployment);
        }
        else if (error > 0) {
            addExtraNodeInstances(error, deployment, nodeType);
        }
    }

    // FIXME : Should be available on a cloudML deployment model
    private Collection<NodeInstance> findNodeInstancesByType(Node nodeType, DeploymentModel deployment) {
        ArrayList<NodeInstance> selection = new ArrayList<NodeInstance>();
        for (NodeInstance ni : deployment.getNodeInstances()) {
            if (ni.getType().equals(nodeType)) {
                selection.add(ni);
            }
        }
        return selection;
    }

    private void addExtraNodeInstances(int missing, DeploymentModel deployment, final Node nodeType) {
        for (int i = 0; i < missing; i++) {
            deployer.provision(deployment, nodeType);
        }
    }

    private void removeExcessiveNodeInstances(Collection<NodeInstance> existings, int error, DeploymentModel deployment) {
        List<NodeInstance> existingList = new ArrayList<NodeInstance>(existings);
        shuffle(existingList);
        for (int i = 0; i > error && !deployment.getNodeInstances().isEmpty(); i--) {
            deployer.shutdown(deployment, existingList.get(i));
        }
    }

    private void adjustArtefactInstanceCount(DeploymentModel deployment, final String artefactTypeName, int desired) {
        Collection<ArtefactInstance> existings = findArtefactInstancesByType(artefactTypeName, deployment);
        final int error = desired - existings.size();
        if (error > 0) {
            addExtraArtefactInstances(error, deployment, artefactTypeName);
        }
        else if (error > 0) {
            removeExessiveArtefactInstances(existings, error, deployment);
        }
    }

    private Collection<ArtefactInstance> findArtefactInstancesByType(String typeName, DeploymentModel deployment) {
        ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance ai : deployment.getArtefactInstances()) {
            if (ai.getType().getName().equals(typeName)) {
                selection.add(ai);
            }
        }
        return selection;
    }

    private void addExtraArtefactInstances(final int error, DeploymentModel deployment, final String artefactTypeName) {
        for (int i = 0; i < error; i++) {
            deployer.install(deployment, deployment.findArtefactByName(artefactTypeName));
        }
    }

    private void removeExessiveArtefactInstances(Collection<ArtefactInstance> existings, final int error, DeploymentModel deployment) {
        List<ArtefactInstance> existingsList = new ArrayList<ArtefactInstance>(existings);
        shuffle(existingsList);
        for (int i = 0; i > error; i--) {
            deployer.uninstall(deployment, existingsList.get(i));
        }
    }

}
