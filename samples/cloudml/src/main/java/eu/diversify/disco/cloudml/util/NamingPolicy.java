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
package eu.diversify.disco.cloudml.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class NamingPolicy {

    public static final int DEFAULT_MINIMUM_ID = 1;
    public static final String DEFAULT_NODE_TYPE_NAME_PREFIX = "node type #";
    public static final String DEFAULT_NODE_INSTANCE_NAME_PREFIX = "node instance #";
    public static final String DEFAULT_ARTEFACT_TYPE_NAME_PREFIX = "artefact type #";
    public static final String DEFAULT_ARTEFACT_INSTANCE_NAME_PREFIX = "artefact instance #";
    private final int minimumId;
    private final String nodeTypeNamePrefix;
    private final String nodeInstanceNamePrefix;
    private final String artefactTypeNamePrefix;
    private final String artefactInstanceNamePrefix;

    public NamingPolicy() {
        this.minimumId = DEFAULT_MINIMUM_ID;
        this.nodeTypeNamePrefix = DEFAULT_NODE_TYPE_NAME_PREFIX;
        this.nodeInstanceNamePrefix = DEFAULT_NODE_INSTANCE_NAME_PREFIX;
        this.artefactTypeNamePrefix = DEFAULT_ARTEFACT_TYPE_NAME_PREFIX;
        this.artefactInstanceNamePrefix = DEFAULT_ARTEFACT_INSTANCE_NAME_PREFIX;
    }

    public NamingPolicy(int minimumId, String nodeTypNamePrefix, String nodeInstanceNamePrefix, String artefactTypeNamePrefix, String artefactInstanceNamePrefix) {
        this.minimumId = minimumId;
        this.nodeTypeNamePrefix = nodeTypNamePrefix;
        this.nodeInstanceNamePrefix = nodeInstanceNamePrefix;
        this.artefactTypeNamePrefix = artefactTypeNamePrefix;
        this.artefactInstanceNamePrefix = artefactInstanceNamePrefix;
    }

    public String defaultNodeTypeName(Collection<String> existingTypeNames) {
        return nodeTypeNamePrefix + minimumUnusedId(existingTypeNames);
    }

    public String defaultNodeInstanceName(Collection<String> existingInstanceNames) {
        return nodeInstanceNamePrefix + minimumUnusedId(existingInstanceNames);
    }

    public String defaultArtefactTypeName(Collection<String> existingTypeNames) {
        return artefactTypeNamePrefix + minimumUnusedId(existingTypeNames);
    }

    public String defaultArtefactInstanceName(Collection<String> existingInstanceNames) {
        return artefactInstanceNamePrefix + minimumUnusedId(existingInstanceNames);
    }

    public int getMinimumId() {
        return minimumId;
    }

    public String getNodeTypNamePrefix() {
        return nodeTypeNamePrefix;
    }

    public String getNodeInstanceNamePrefix() {
        return nodeInstanceNamePrefix;
    }

    public String getArtefactTypeNamePrefix() {
        return artefactTypeNamePrefix;
    }

    public String getArtefactInstanceNamePrefix() {
        return artefactInstanceNamePrefix;
    }

    private int minimumUnusedId(Collection<String> names) {
        final List<Integer> usedIds = extractUsedIds(names);
        int id = minimumId;
        while (usedIds.contains(id)) {
            id++;
        }
        return id;
    }

    private ArrayList<Integer> extractUsedIds(Collection<String> names) throws NumberFormatException {
        final ArrayList<Integer> usedIds = new ArrayList<Integer>();
        final String nodeTypePattern = "#(\\d+)";
        final int nodeTypeIdGroup = 1;
        final Pattern pattern = Pattern.compile(nodeTypePattern);
        for (String name : names) {
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                usedIds.add(Integer.parseInt(matcher.group(nodeTypeIdGroup)));
            }
        }
        return usedIds;
    }

    public String nameOfNodeType(int index) {
        return nodeTypeNamePrefix + index;
    }

    public String nameOfArtefactType(int index) {
        return artefactTypeNamePrefix + index;
    }
}
