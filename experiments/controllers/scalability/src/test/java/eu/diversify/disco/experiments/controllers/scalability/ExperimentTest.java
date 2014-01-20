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
package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import junit.framework.TestCase;
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
    public void testParameter() throws FileNotFoundException {
        final String setupFile = "../src/test/resources/setup.yml";
        final Yaml yaml = new Yaml(new Constructor(Setup.class));
        final Setup setup = (Setup) yaml.load(new FileInputStream(setupFile));
        assertEquals(
                "Wrong number of species counts",
                6,
                setup.getSpeciesCounts().size());
        assertEquals(
                "Wrong number of individual counts",
                1,
                setup.getIndividualsCounts().size());
        assertEquals(
                "Wrong number of strategies",
                2,
                setup.getStrategies().size());
    }

    @Test
    public void testRunner() throws FileNotFoundException {
        Experiment experiment = new Experiment();

        // Set the runners
        experiment.addController("Hill Climbing", new HillClimber());
        experiment.addController("Adaptive Hill Climbing", new AdaptiveHillClimber());
        assertEquals(
                "Wrong set of controllers",
                2,
                experiment.getControllers().size());

        experiment.setIndividualsCount(Arrays.asList(new Integer[]{25, 50}));
        assertEquals(
                "Wrong number of individuals",
                2,
                experiment.getIndividualCounts().size());

        experiment.setSpeciesCounts(Arrays.asList(new Integer[]{2, 4, 6, 8}));
        assertEquals(
                "Wrong species counts",
                4,
                experiment.getSpeciesCounts().size());

        experiment.run();

        experiment.saveResultsAs("results.csv");
        File results = new File("results.csv");
        assertTrue(
                "No file were created",
                results.exists());


    }
}
