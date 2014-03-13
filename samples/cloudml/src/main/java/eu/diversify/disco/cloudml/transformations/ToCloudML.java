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

import eu.diversify.disco.cloudml.util.actions.StandardLibrary;
import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.shuffle;
import java.util.List;
import java.util.Random;
import org.cloudml.core.Artefact;
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

    private final StandardLibrary deployer;
    private final Random random;

    public ToCloudML() {
        this.random = new Random();
        deployer = new StandardLibrary();
    }

    public DeploymentModel applyTo(DeploymentModel deployment, Population toBe) {
        abortIfInvalid(deployment);
        abortIfInvalid(toBe);
        // TODO: Check if the model and the population are consistent?
        
        for (Node nodeType: deployment.getNodes()) {
            adjustNodeInstanceCount(deployment, nodeType, toBe.getNumberOfIndividualsIn(nodeType.getName()));
        }
        
        for (Artefact artefactType: deployment.getArtefacts()) {
            adjustArtefactInstanceCount(deployment, artefactType, toBe.getNumberOfIndividualsIn(artefactType.getName()));
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
        final Collection<NodeInstance> existings = deployment.findNodeInstancesByType(nodeType); 
        final int error = desired - existings.size();
        if (error < 0) {
            removeExcessiveNodeInstances(existings, error, deployment);
        }
        else if (error > 0) {
            addExtraNodeInstances(error, deployment, nodeType);
        }
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

    private void adjustArtefactInstanceCount(DeploymentModel deployment, Artefact artefact, int desired) {
        Collection<ArtefactInstance> existings = deployment.findArtefactInstancesByType(artefact);
        final int error = desired - existings.size();
        if (error > 0) {
            addExtraArtefactInstances(error, deployment, artefact);
        }
        else if (error > 0) {
            removeExessiveArtefactInstances(existings, error, deployment);
        }
    }


    private void addExtraArtefactInstances(final int error, DeploymentModel deployment, Artefact artefactType) {
        for (int i = 0; i < error; i++) {
            deployer.install(deployment, artefactType);
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
