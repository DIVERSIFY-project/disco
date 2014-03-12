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
import java.util.Random;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;

public class FindServerPortInstance implements Action<ServerPortInstance> {

    private final DeploymentEngineer deployer;
    private final Binding bindingType;

    public FindServerPortInstance(DeploymentEngineer deployer, Binding bindingType) {
        this.deployer = deployer;
        this.bindingType = bindingType;
    }

    @Override
    public final ServerPortInstance applyTo(DeploymentModel deployment) {
        final List<ServerPortInstance> candidates = collectCandidates(deployment);
        if (candidates.isEmpty()) {
            final Artefact artefact = deployer.findArtefactTypeProviding(deployment, bindingType);
            final ArtefactInstance instance = deployer.install(deployment, artefact);
            final ServerPortInstance serverPort = instance.findProvidedPortByName(bindingType.getServer().getName());
            candidates.add(serverPort);
        }
        return chooseOne(candidates);
    }

    private ArrayList<ServerPortInstance> collectCandidates(DeploymentModel deployment) {
        ArrayList<ServerPortInstance> candidates = new ArrayList<ServerPortInstance>();
        for (ArtefactInstance artefactInstance : deployment.getArtefactInstances()) {
            for (ServerPortInstance serverPort : artefactInstance.getProvided()) {
                if (isCandidate(bindingType, serverPort)) {
                    candidates.add(serverPort);
                }
            }
        }
        return candidates;
    }

    private boolean isCandidate(Binding bindingType, ServerPortInstance serverPort) {
        return bindingType.getServer().equals(serverPort.getType());
    }

    protected ServerPortInstance chooseOne(List<ServerPortInstance> candidates) {
        final int index = new Random().nextInt(candidates.size());
        return candidates.get(index);
    }
}
