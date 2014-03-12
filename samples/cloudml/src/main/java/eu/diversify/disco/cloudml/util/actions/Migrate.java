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

import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;

public class Migrate implements Action<NodeInstance> {
        
    private final ArtefactInstance instance;
    private final DeploymentEngineer deployer;
    
    public Migrate(DeploymentEngineer deployer, ArtefactInstance instance) {
        this.instance = instance;
        this.deployer = deployer;
    }

    @Override
    public NodeInstance applyTo(DeploymentModel target) {
        NodeInstance newDestination = deployer.findAlternativeDestinationFor(target, instance);
        instance.setDestination(newDestination);
        return newDestination;
    }  

}
