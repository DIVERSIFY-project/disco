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

package eu.diversify.disco.cloudml.util.actions;

import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;


public class Terminate implements Action<Void> {

    private final DeploymentEngineer deployer;
    private final NodeInstance instance;

    public Terminate(DeploymentEngineer deployer, NodeInstance instance) {
        this.deployer = deployer;
        this.instance = instance;
    }
      
    
    @Override
    public Void applyTo(DeploymentModel target) {
        final List<ArtefactInstance> hosted = findArtefactInstancesByDestination(target, instance);
        for(ArtefactInstance artefact: hosted) {
            deployer.migrate(target, artefact);
        }
        deployer.stop(target, instance);
        target.getNodeInstances().remove(instance);
        return NOTHING;
    }

    // FIXME: Should be part of the deployment model
    private List<ArtefactInstance> findArtefactInstancesByDestination(DeploymentModel deployment, NodeInstance instance) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance artefact: deployment.getArtefactInstances()) {
            if (artefact.getDestination().equals(instance)) {
                selection.add(artefact);
            }
        }
        return selection;
    }

    
}
