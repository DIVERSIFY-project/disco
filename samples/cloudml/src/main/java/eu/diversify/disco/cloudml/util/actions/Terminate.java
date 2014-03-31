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

import java.util.Collection;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;


public class Terminate extends AbstractAction<Void> {

    private final NodeInstance instance;

    public Terminate(StandardLibrary library, NodeInstance instance) {
        super(library);
        this.instance = instance;
    } 
    
    @Override
    public Void applyTo(DeploymentModel deployment) {
        final Collection<ArtefactInstance> hosted = deployment.getArtefactInstances().hostedBy(instance);
        for(ArtefactInstance artefact: hosted) {
            getLibrary().migrate(deployment, artefact);
        }
        getLibrary().stop(deployment, instance);
        deployment.getNodeInstances().remove(instance);
        return NOTHING;
    }
    
}
