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
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;

public class FindNodeType implements Action<Node> {

    private final DeploymentEngineer library;
    private final Artefact artefact;

    public FindNodeType(DeploymentEngineer library, Artefact artefact) {
        this.library = library;
        this.artefact = artefact;
    }

    @Override
    public final Node applyTo(DeploymentModel deployment) {
        final List<Node> candidates = collectCandidates(deployment);
        if (candidates.isEmpty()) {
            final String message = String.format(
                    "Unable to find a node type relevant to install artefacts of type '%s'",
                    artefact.getName());
            throw new IllegalStateException(message);
        }
        return chooseOne(candidates);
    }

    protected boolean canHost(Artefact artefact, Node node) {
        return true;
    }

    protected Node chooseOne(List<Node> candidates) {
        final int index = new Random().nextInt(candidates.size());
        return candidates.get(index);
    }

    private List<Node> collectCandidates(DeploymentModel deployment) {
        List<Node> candidates = new ArrayList<Node>();
        for (Node node : deployment.getNodes()) {
            if (canHost(artefact, node)) {
                candidates.add(node);
            }
        }
        return candidates;
    }
}
