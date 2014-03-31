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

package eu.diversify.disco.cloudml.util;

import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;

// FIXME: To be moved in cloudml
public class ToolBox {

    public static List<BindingInstance> findBindingInstancesByType(DeploymentModel deployment, Binding type) {
        List<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : deployment.getBindingInstances()) {
            if (binding.getType().equals(type)) {
                selection.add(binding);
            }
        }
        return selection;
    }

    public static int countNodesOfType(DeploymentModel deployment, String typeName) {
        final Node type = deployment.getNodeTypes().named(typeName);
        return deployment.getNodeInstances().ofType(type).size();
    }

    public static int countArtefactsOfType(DeploymentModel deployment, String typeName) {
        final Artefact type = deployment.getArtefactTypes().named(typeName);
        return deployment.getArtefactInstances().ofType(type).size();
    }

    public static int countBindingsOfType(DeploymentModel deployment, String typeName) {
        final Binding type = deployment.getBindingTypes().named(typeName);
        return findBindingInstancesByType(deployment, type).size();
    }
}
