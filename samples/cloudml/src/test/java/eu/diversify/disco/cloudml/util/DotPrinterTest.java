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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import org.cloudml.core.samples.SshClientServer;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(JUnit4.class)
public class DotPrinterTest {

    @Test
    public void testSaveAs() throws FileNotFoundException {
        DeploymentModel model = new SshClientServer()
                .getTwoClientsConnectedToOneServer()
                .build();
        final String fileName = "test.dot";

        new DotPrinter().saveAs(model, fileName); 
        
        verifyThatFileExistsOnDisk(fileName);
    }

    @Test
    public void testNodeAreTransformedIntoCluster() {
        DeploymentModel model = new SshClientServer()
                .getTwoClientsConnectedToOneServer()
                .build();

        String dotText = new DotPrinter().print(model);

        verifyNumberOfDigraphs(model, dotText);
        verifyNumberOfClusters(model, dotText);
        verifyNumberOfDotNodes(model, dotText);
        verifyNumberOfDotEdges(model, dotText);
    }

    private void verifyNumberOfDigraphs(DeploymentModel model, String dotText) {
        int count = countOccurences(dotText, "digraph");
        assertThat("number of directed graphs", count, is(equalTo(1)));
    }

    private void verifyNumberOfClusters(DeploymentModel model, String dotText) {
        final int nodeInstancesCount = model.getNodeInstances().size();
        final int clusterCount = countOccurences(dotText, "subgraph cluster_");
        assertThat("cluster count", clusterCount, is(equalTo(nodeInstancesCount)));
    }

    private void verifyNumberOfDotNodes(DeploymentModel model, String dotText) {
        final int dotNodeCount = countOccurences(dotText, "(?!->)\\s+node_\\d+_\\d+\\s+(?!->)");
        final int artefactInstancesCount = model.getArtefactInstances().size();
        assertThat("dot nodes count", dotNodeCount, is(equalTo(artefactInstancesCount)));
    }

    private void verifyNumberOfDotEdges(DeploymentModel model, String dotText) {
        final int dotNodeCount = countOccurences(dotText, "node_\\d+_\\d+ -> node_\\d+_\\d+");
        final int bindingInstancesCount = model.getBindingInstances().size();
        assertThat("dot edges count", dotNodeCount, is(equalTo(bindingInstancesCount)));
    }

    private int countOccurences(String dotText, String label) {
        Pattern pattern = Pattern.compile(label);
        Matcher matcher = pattern.matcher(dotText);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private void verifyThatFileExistsOnDisk(String fileName) {
        assertThat("dot file created", new File(fileName).exists(), is(true));
    }
}