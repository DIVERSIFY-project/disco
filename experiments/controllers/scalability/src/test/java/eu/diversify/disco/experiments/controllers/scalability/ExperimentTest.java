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

import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.File;
import java.io.FileNotFoundException;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    public void testRunner() throws FileNotFoundException {
        Experiment experiment = new Experiment();

        // Set the runners
        experiment.addController("Hill Climbing", new HillClimber(new TrueDiversity()));
        experiment.addController("Adaptive Hill Climbing", new AdaptiveHillClimber(new TrueDiversity()));
        assertEquals(
                "Wrong set of controllers",
                2,
                experiment.getControllers().size());
        
        experiment.setIndividualsCount(new int[]{25, 50});
        assertEquals(
                "Wrong number of individuals",
                2,
                experiment.getIndividualCounts().size()
                );
        
        experiment.setSpeciesCounts(new int[]{2, 4, 6, 8});
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
