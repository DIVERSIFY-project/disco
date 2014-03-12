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
package eu.diversify.disco.cloudml.transformations.tocloudml;

import eu.diversify.disco.cloudml.transformations.ToCloudML;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.cloudml.core.samples.SshClientServer;
import static org.cloudml.core.samples.SshClientServer.*;

import static eu.diversify.disco.cloudml.matchers.Commons.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(JUnit4.class)
public class ToCloudMLTest extends TestCase {

    public ToCloudMLTest() {
    }
    
    @Test
    public void testThatResultHasBeenCreatedFromScratch() {
        DeploymentModel source = new SshClientServer()
                .getTypes()
                .build();

        Population reference = new PopulationBuilder()
                .withSpeciesNamed(LINUX_TYPE, WINDOWS_TYPE, CLIENT_TYPE, SERVER_TYPE)
                .withDistribution(1, 1, 1, 1)
                .make();

        ToCloudML transformation = new ToCloudML();
        DeploymentModel output = transformation.applyTo(source, reference);

        assertThat("output model", output, is(valid()));
        assertThat("output model", output, is(closer().to(reference).than(source)));
    }
    

    @Test
    public void thatTheResultHasBeenExpanded() {
        DeploymentModel source = new SshClientServer()
                .getOneClientConnectedToOneServer()
                .build();

        Population reference = new PopulationBuilder()
                .withSpeciesNamed(LINUX_TYPE, WINDOWS_TYPE, CLIENT_TYPE, SERVER_TYPE)
                .withDistribution(3, 1, 1, 1)
                .make();

        ToCloudML transformation = new ToCloudML();
        DeploymentModel output = transformation.applyTo(source, reference);

        assertThat("output model", output, is(valid()));
        assertThat("output model", output, is(closer().to(reference).than(source)));
    }

    @Test
    public void thatTheResultHasBeenShrinked() {
        DeploymentModel source = new SshClientServer()
                .getTwoClientsConnectedToOneServer()
                .build();

        Population reference = new PopulationBuilder()
                .withSpeciesNamed(LINUX_TYPE, WINDOWS_TYPE, CLIENT_TYPE, SERVER_TYPE)
                .withDistribution(1, 1, 1, 1)
                .make();

        ToCloudML transformation = new ToCloudML();
        DeploymentModel output = transformation.applyTo(source, reference);

        assertThat("output model", output, is(valid()));
        assertThat("output model", output, is(closer().to(reference).than(source)));
    }
}