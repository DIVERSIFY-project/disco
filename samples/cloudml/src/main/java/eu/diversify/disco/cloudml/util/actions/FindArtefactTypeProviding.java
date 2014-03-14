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
import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;

class FindArtefactTypeProviding extends AbstractFind<Artefact> {

    private final Binding bindingType;

    public FindArtefactTypeProviding(StandardLibrary library, Binding bindingType) {
        super(library);
        this.bindingType = bindingType;
    }

    @Override
    protected void handleLackOfCandidate(DeploymentModel deployment, List<Artefact> candidates) {
        final String string = String.format(
                "Unable to find a artefact type matching the server end of binding type '%s' (port type'%s')",
                bindingType.getName(),
                bindingType.getServer().getName());
        throw new IllegalStateException(string);
    }

    @Override
    protected List<Artefact> collectCandidates(DeploymentModel deployment) {
        List<Artefact> candidates = new ArrayList<Artefact>();
        for (Artefact artefact : deployment.getArtefacts()) {
            if (isCandidate(artefact)) {
                candidates.add(artefact);
            }
        }
        return candidates;
    }

    private boolean isCandidate(Artefact artefact) {
        return artefact.getProvided().contains(bindingType.getServer());
    }

}