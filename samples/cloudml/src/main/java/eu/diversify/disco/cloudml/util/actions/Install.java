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

import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.builders.ArtefactInstanceBuilder;


import static org.cloudml.core.builders.Commons.*;


public class Install implements Action<ArtefactInstance> {

    private final Artefact artefactType;
    private final DeploymentEngineer deployer;
    
    
    
    public Install(DeploymentEngineer deployer, Artefact artefactType) {
        this.artefactType = artefactType;
        this.deployer = deployer;
    }

    @Override
    public ArtefactInstance applyTo(DeploymentModel target) {
        final String instanceName = deployer.createUniqueArtefactInstanceName(target);
        ArtefactInstanceBuilder builder = anArtefactInstance()
                .named(instanceName)
                .ofType(artefactType.getName())
                .hostedBy(deployer.findDestinationFor(target, artefactType).getName());
        builder.integrateIn(target);
        ArtefactInstance instance = target.findArtefactInstanceByName(instanceName);
        for(ClientPortInstance clientPort: instance.getRequired()) {
            deployer.bind(target, clientPort);
        }
        builder.integrateIn(target);
        return instance;
    }
    
}
