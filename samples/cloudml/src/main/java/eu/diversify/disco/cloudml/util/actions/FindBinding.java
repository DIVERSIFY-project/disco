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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.cloudml.core.Binding;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;

public class FindBinding extends AbstractAction<Binding> {

    private final ClientPortInstance clientPort;

    public FindBinding(StandardLibrary library, ClientPortInstance clientPort) {
        super(library);
        this.clientPort = clientPort;
    }

    @Override
    public final Binding applyTo(DeploymentModel deployment) {
        final List<Binding> candidates = collectCandidates(deployment);
        if (candidates.isEmpty()) {
            final String message = String.format(
                    "Unable to find any relevant binding type for client port '%s:%s' of type '%s'",
                    clientPort.getOwner().getName(),
                    clientPort.getName(),
                    clientPort.getType().getName());
            throw new IllegalStateException(message);
        }
        return chooseOne(deployment, candidates);
    }

    private boolean isRelevant(DeploymentModel deployment, ClientPortInstance clientPort, Binding binding) {
        return binding.getClient().equals(clientPort.getType())
                && !isExcluded(binding);
    }

    protected Binding chooseOne(DeploymentModel deployment, List<Binding> candidates) {
        int index = new Random().nextInt(candidates.size());
        return candidates.get(index);
    }

    protected boolean isExcluded(Binding binding) {
        return false;
    }

    private ArrayList<Binding> collectCandidates(DeploymentModel deployment) {
        final ArrayList<Binding> candidates = new ArrayList<Binding>();
        for (Binding binding : deployment.getBindings()) {
            if (isRelevant(deployment, clientPort, binding)) {
                candidates.add(binding);
            }
        }
        return candidates;
    }
}
