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
package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.experiments.commons.data.DataSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Run the various control strategies on a single case and collect their
 * trajectory.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ExperimentTest extends TestCase {


    @Test
    public void testRunner() throws FileNotFoundException, ControllerInstantiationException {
        final String setupFile = "../src/test/resources/setup.yml";
        final Yaml yaml = new Yaml(new Constructor(ScalabilitySetup.class));
        final ScalabilitySetup setup = (ScalabilitySetup) yaml.load(new FileInputStream(setupFile));
        assertEquals(
                "Wrong number of species counts",
                4,
                setup.getSpeciesCounts().size());
        assertEquals(
                "Wrong number of individual counts",
                3,
                setup.getIndividualsCounts().size());
        assertEquals(
                "Wrong number of strategies",
                2,
                setup.getStrategies().size());

        ScalabilityExperiment experiment = new ScalabilityExperiment(setup);
        List<DataSet> results = experiment.run();

        assertEquals("Wrong number of results", 1, results.size());
        assertEquals(
                "Wrong number of data point",
                setup.getIndividualsCounts().size() * setup.getSpeciesCounts().size() * setup.getStrategies().size(),
                results.get(0).getSize());
    }
}
