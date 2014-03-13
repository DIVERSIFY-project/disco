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
package eu.diversify.disco.cloudml.util.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

public class FindDestination extends AbstractFinder<NodeInstance> {

    private final Artefact artefact;
    private final ArrayList<NodeInstance> excluded;

    public FindDestination(StandardLibrary library, Artefact artefact, NodeInstance... toExclude) {
        super(library);
        this.artefact = artefact;
        this.excluded = new ArrayList<NodeInstance>(Arrays.asList(toExclude));
    }

    @Override
    protected ArrayList<NodeInstance> collectCandidates(DeploymentModel deployment) {
        ArrayList<NodeInstance> candidates = new ArrayList<NodeInstance>();
        for (NodeInstance nodeInstance : deployment.getNodeInstances()) {
            if (isCandidate(nodeInstance)) {
                candidates.add(nodeInstance);
            }
        }
        return candidates;
    }

    @Override
    protected void handleLackOfCandidate(DeploymentModel deployment, List<NodeInstance> candidates) {
        Node nodeType = getLibrary().findNodeType(deployment, artefact);
        candidates.add(getLibrary().provision(deployment, nodeType));
    }

    private boolean isCandidate(NodeInstance nodeInstance) {
        return canHost(artefact, nodeInstance) && !isExcluded(nodeInstance);
    }

    protected boolean canHost(Artefact artefact, NodeInstance nodeInstance) {
        return true;
    }

    private boolean isExcluded(NodeInstance nodeInstance) {
        return this.excluded.contains(nodeInstance);
    }
}
