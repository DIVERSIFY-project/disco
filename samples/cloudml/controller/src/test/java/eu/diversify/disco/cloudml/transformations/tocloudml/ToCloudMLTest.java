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


package eu.diversify.disco.cloudml.transformations.tocloudml;

import eu.diversify.disco.cloudml.transformations.ToCloudML;
import eu.diversify.disco.population.Population;
import java.io.FileNotFoundException;

import static eu.diversify.disco.population.PopulationBuilder.*;

import junit.framework.TestCase;
import org.cloudml.core.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.samples.SshClientServer.*;

import static eu.diversify.disco.cloudml.matchers.Commons.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static eu.diversify.disco.cloudml.util.ToolBox.*;

@RunWith(JUnit4.class)
public class ToCloudMLTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullDeploumentModel() {
        Population reference = aPopulation()
                .withSpeciesNamed(EC2_LARGE_LINUX, EC2_XLARGE_WINDOWS_7, SSH_CLIENT, SSH_SERVER)
                .withDistribution(1, 1, 1, 1)
                .build();

        ToCloudML transformation = new ToCloudML();
        transformation.applyTo(null, reference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullReferencePopulation() {
        Deployment source = getSshTypes().build();

        Population reference = null;

        ToCloudML transformation = new ToCloudML();
        transformation.applyTo(source, reference);
    }

    @Test
    public void testThatResultHasBeenCreatedFromScratch() {
        Deployment source = getSshTypes().build();

        Population reference = aPopulation()
                .withSpeciesNamed(EC2_LARGE_LINUX, EC2_XLARGE_WINDOWS_7, SSH_CLIENT, SSH_SERVER)
                .withDistribution(1, 1, 1, 1)
                .build();

        ToCloudML transformation = new ToCloudML();
        Deployment output = transformation.applyTo(source, reference);

        verifyModel(output, reference, source, 1, 1, 1, 1, 1);
    }

    @Test
    public void thatTheResultHasBeenExpanded() {
        Deployment source = getOneClientConnectedToOneServer()
                .build();

        Population reference = aPopulation()
                .withSpeciesNamed(EC2_LARGE_LINUX, EC2_XLARGE_WINDOWS_7, SSH_CLIENT, SSH_SERVER)
                .withDistribution(3, 1, 1, 1)
                .build();

        ToCloudML transformation = new ToCloudML();
        Deployment output = transformation.applyTo(source, reference);

        verifyModel(output, reference, source, 3, 1, 1, 1, 1);
    }

    @Test
    public void thatTheResultHasBeenShrinked() {
        Deployment source = getTwoClientsConnectedToOneServer().build();

        Population reference = aPopulation()
                .withSpeciesNamed(EC2_LARGE_LINUX, EC2_XLARGE_WINDOWS_7, SSH_CLIENT, SSH_SERVER)
                .withDistribution(1, 1, 1, 1)
                .build();

        ToCloudML transformation = new ToCloudML();
        Deployment output = transformation.applyTo(source, reference);

        verifyModel(output, reference, source, 1, 1, 1, 1, 1);
    }

    @Test
    public void weRequestManyClients() {
        Deployment source = getTwoClientsConnectedToOneServer().build();

        Population reference = aPopulation()
                .withSpeciesNamed(EC2_LARGE_LINUX, EC2_XLARGE_WINDOWS_7, SSH_CLIENT, SSH_SERVER)
                .withDistribution(1, 1, 5, 1)
                .build();

        ToCloudML transformation = new ToCloudML();
        Deployment output = transformation.applyTo(source, reference);

        verifyModel(output, reference, source, 1, 1, 5, 1, 5);
    }

    @Test
    public void weTearDownEveryThingExceptANode() throws FileNotFoundException {
        Deployment source = getTwoClientsConnectedToOneServer().build();

        Population reference = aPopulation()
                .withSpeciesNamed(EC2_LARGE_LINUX, EC2_XLARGE_WINDOWS_7, SSH_CLIENT, SSH_SERVER)
                .withDistribution(1, 0, 0, 0)
                .build();

        ToCloudML transformation = new ToCloudML();
        Deployment output = transformation.applyTo(source, reference);

        verifyModel(output, reference, source, 1, 0, 0, 0, 0);
    }

    private void verifyModel(Deployment output, Population reference, Deployment source, int linuxCount, int windowsCount, int clientCount, int serverCount, int bindingCount) {
        assertThat("Linux instance", countNodesOfType(output, EC2_LARGE_LINUX), is(equalTo(linuxCount)));
        assertThat("Windows instance", countNodesOfType(output, EC2_XLARGE_WINDOWS_7), is(equalTo(windowsCount)));
        assertThat("Client instance", countArtefactsOfType(output, SSH_CLIENT), is(equalTo(clientCount)));
        assertThat("Server instance", countArtefactsOfType(output, SSH_SERVER), is(equalTo(serverCount)));
        assertThat("Binding instance", countBindingsOfType(output, SSH_CONNECTION), is(equalTo(bindingCount)));
        assertThat("output model", output, is(valid()));
        assertThat("output model", output, is(closer().to(reference).than(source)));
    }
}