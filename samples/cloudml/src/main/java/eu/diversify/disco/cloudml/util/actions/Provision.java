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
/*
 */

package eu.diversify.disco.cloudml.util.actions;

import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.builders.NodeInstanceBuilder;

public class Provision implements Action<NodeInstance> {
 
    private final Node nodeType;
    private final DeploymentEngineer deployer;
    
       
    public Provision(Node nodeType, DeploymentEngineer deployer) {
        this.nodeType = nodeType;
        this.deployer = deployer;
    }
    
    @Override
    public NodeInstance applyTo(DeploymentModel target) {
        String instanceName = deployer.createUniqueNodeInstanceName(target);
        NodeInstanceBuilder builder = aNodeInstance()
                .named(instanceName)
                .ofType(nodeType.getName());
        builder.integrateIn(target);
        return target.findNodeInstanceByName(instanceName);
    }
   
}
