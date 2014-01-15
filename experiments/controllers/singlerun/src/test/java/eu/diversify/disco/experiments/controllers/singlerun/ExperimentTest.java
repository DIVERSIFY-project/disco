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
package eu.diversify.disco.experiments.controllers.singlerun;

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
        Experiment runner = new Experiment();

        // Set the runners
        runner.addController("Hill Climbing", new HillClimber(new TrueDiversity()));
        runner.addController("Adaptive Hill Climbing", new AdaptiveHillClimber(new TrueDiversity()));
        assertEquals(
                "Wrong set of controllers",
                2,
                runner.getControllers().size());

        // Set the reference
        runner.setReference(0.23);
        assertEquals(
                "Wrong reference",
                0.23,
                runner.getReference());

        Population population = new Population();
        population.addSpecie("Hippos", 10);
        population.addSpecie("Snails", 14);
        population.addSpecie("Sludges", 32);
        population.addSpecie("Crocodiles", 5);
        runner.setInitialPopulation(population);
        assertEquals(
                "Wrong population",
                population,
                runner.getInitialPopulation());

        runner.run();

        runner.saveResultsAs("results.csv");
        File results = new File("results.csv");
        assertTrue(
                "No file were created",
                results.exists());


    }
}
