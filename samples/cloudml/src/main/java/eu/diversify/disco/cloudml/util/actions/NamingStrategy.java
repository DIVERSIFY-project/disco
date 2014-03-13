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
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NamedElement;

public class NamingStrategy {

    public static final String DEFAULT_PATTERN = "#(\\d+)";
    public static final int ID_GROUP_INDEX = 1;
    public static final String NODE_INSTANCE_NAME_FORMAT = "node instance #%d";
    public static final String ARTEFACT_INSTANCE_NAME_FORMAT = "artefact instance #%d";
    public static final String BINDING_INSTANCE_NAME_FORMAT = "binding instance #%d";
    private final int minimumId = 1;
    private final Pattern pattern;

    public NamingStrategy() {
        this.pattern = Pattern.compile(DEFAULT_PATTERN);
    }

    private String createUniqueName(Collection<? extends NamedElement> existings, String format) {
        return String.format(format, minimumUnusedId(existings));
    }

    private int minimumUnusedId(Collection<? extends NamedElement> names) {
        final List<Integer> usedIds = extractUsedIds(names);
        int id = minimumId;
        while (usedIds.contains(id)) {
            id++;
        }
        return id;
    }

    private List<Integer> extractUsedIds(Collection<? extends NamedElement> names) throws NumberFormatException {
        final ArrayList<Integer> usedIds = new ArrayList<Integer>();
        for (NamedElement element : names) {
            Matcher matcher = pattern.matcher(element.getName());
            if (matcher.find()) {
                usedIds.add(Integer.parseInt(matcher.group(ID_GROUP_INDEX)));
            }
        }
        return usedIds;
    }

    public String createUniqueNodeInstanceName(DeploymentModel deployment) {
        return createUniqueName(deployment.getNodeInstances(), NODE_INSTANCE_NAME_FORMAT);
    }

    public String createUniqueArtefactInstanceName(DeploymentModel deployment) {
        return createUniqueName(deployment.getArtefactInstances(), ARTEFACT_INSTANCE_NAME_FORMAT);
    }

    public String createUniqueBindingInstanceName(DeploymentModel deployment) {
        return createUniqueName(deployment.getBindingInstances(), BINDING_INSTANCE_NAME_FORMAT);
    }
}
