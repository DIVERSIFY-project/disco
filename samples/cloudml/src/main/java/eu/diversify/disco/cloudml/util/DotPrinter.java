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


import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NamedElement;
import org.cloudml.core.NodeInstance;

public class DotPrinter {

    private final SymbolTable symbols;
    private final StringBuilder builder;

    public DotPrinter() {
        this.builder = new StringBuilder();
        this.symbols = new SymbolTable();
    }

    public String print(DeploymentModel model) {
        builder.append("digraph ");
        builder.append("G");
        builder.append(" {");
        printClusters(model);
        printEdges(model);
        builder.append("}");
        return builder.toString();
    }

    private void printClusters(DeploymentModel model) {
        int clusterIndex = 0;
        for (NodeInstance nodeInstance : model.getNodeInstances()) {
            clusterIndex += 1;
            builder.append("subgraph cluster_").append(clusterIndex).append(" {");
            builder.append("style=filled;\n");
            builder.append("color=lightgrey;\n");
            builder.append("label=\"").append(escape(nodeInstance.getName())).append("\";");
            printDotNodes(clusterIndex, model.findArtefactInstancesByDestination(nodeInstance));
            builder.append("}");
        }
    }

    private void printDotNodes(int clusterIndex, List<ArtefactInstance> artefactInstances) {
        int artefactIndex = 0;
        for (ArtefactInstance artefact : artefactInstances) {
            artefactIndex += 1;
            final String dotNodeName = "node_" + clusterIndex + "_" + artefactIndex;
            symbols.put(artefact, dotNodeName);
            builder.append("\t\t")
                    .append(dotNodeName)
                    .append(" [")
                    .append("label=\"")
                    .append(escape(artefact.getName()))
                    .append("\",color=black,style=filled,fillcolor=white];\n");
        }
    }

    private void printEdges(DeploymentModel model) {
        for (BindingInstance binding : model.getBindingInstances()) {
            printEdge(binding);
        }
    }

    private void printEdge(BindingInstance binding) {
        builder.append("\t");
        builder.append(symbols.get(binding.getClient().getOwner()));
        builder.append(" -> ");
        builder.append(symbols.get(binding.getServer().getOwner()));
        builder.append("[label=\"").append(escape(binding.getType().getName())).append("\"]");
        builder.append(";");
    }

    private String escape(String name) {
        return name.trim().replace("[^\\]\"", "\\\"");
    }

    public void saveAs(DeploymentModel model, String fileName) throws FileNotFoundException {
        PrintStream out = null;
        try {
            out = new PrintStream(fileName);
            out.print(print(model));

        } catch (FileNotFoundException ex) {
            throw ex;

        }
        finally {
            out.close();
        }

    }

    private static class SymbolTable {

        private final HashMap<Object, String> table;

        public SymbolTable() {
            this.table = new HashMap<Object, String>();
        }

        private <T extends NamedElement> String get(T namedElement) {
            return this.table.get(namedElement);
        }

        private <T extends NamedElement> void put(T namedElement, String dotName) {
            this.table.put(namedElement, dotName);
        }
    }
}
