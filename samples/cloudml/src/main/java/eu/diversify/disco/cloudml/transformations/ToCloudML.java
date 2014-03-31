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

package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.samples.commons.DiversityInjection;
import eu.diversify.disco.population.Population;
import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.actions.StandardLibrary;


/**
 * Update a cloud model so as it reflect a given population, as much as possible
 *
 * @author Hui Song
 * @author Franck Chauvel
 *
 * @since 0.1
 */
public class ToCloudML implements DiversityInjection<DeploymentModel>{

    private final StandardLibrary deployer;

    public ToCloudML() {
        deployer = new StandardLibrary();
    }

    @Override
    public DeploymentModel applyTo(Population prescription, DeploymentModel currentModel) {
        return applyTo(currentModel, prescription);
    }

    
    public DeploymentModel applyTo(DeploymentModel deployment, Population toBe) {
        abortIfInvalid(deployment);
        abortIfInvalid(toBe);
        // TODO: Check if the model and the population are consistent?
        
        for (Node nodeType: deployment.getNodeTypes()) {
            adjustNodeInstanceCount(deployment, nodeType, toBe.getNumberOfIndividualsIn(nodeType.getName()));
        }
        
        for (Artefact artefactType: deployment.getArtefactTypes()) {
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
        final List<NodeInstance> existings = deployment.getNodeInstances().ofType(nodeType).toList(); 
        final int error = desired - existings.size();
        if (error < 0) {
            removeExcessiveNodeInstances(existings, Math.abs(error), deployment);
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

    private void removeExcessiveNodeInstances(List<NodeInstance> existings, int excess, DeploymentModel deployment) {
        final int bound = Math.min(excess, existings.size());
        for (int i = 0; i < bound ; i++) {
            deployer.terminate(deployment, existings.get(i));
        }
    }

    private void adjustArtefactInstanceCount(DeploymentModel deployment, Artefact artefact, int desired) {
        final List<ArtefactInstance> existings = deployment.getArtefactInstances().ofType(artefact).toList();
        final int error = desired - existings.size();
        if (error < 0) {
            removeExessiveArtefactInstances(existings, Math.abs(error), deployment);
        }
        else if (error > 0) {
            addExtraArtefactInstances(error, deployment, artefact);
        } 
    }

    private void addExtraArtefactInstances(final int error, DeploymentModel deployment, Artefact artefactType) {
        for (int i = 0; i < error; i++) {
            deployer.install(deployment, artefactType);
        }
    }

    private void removeExessiveArtefactInstances(List<ArtefactInstance> existings, int excess, DeploymentModel deployment) {
        final int bound = Math.min(excess, existings.size());
        for (int i = 0; i < bound ; i++) {
            deployer.uninstall(deployment, existings.get(i));
        }
    }

}
