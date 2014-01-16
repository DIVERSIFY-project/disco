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


package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * General set of test which should work for each controller
 *
 * @author Frank Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class ControllerTest extends TestCase {

    /**
     * @return a fresh instance of the controller to test.
     */
    public abstract Controller factory();

    /**
     * Check that the controller is able to drop the diversity to its lowest
     * possible value (i.e., zero)
     */
    @Test
    public void testMinimizeDiversity() {
        Controller controller = factory();

        Population population = new Population();
        population.addSpecie("Tiger", 5);
        population.addSpecie("Sludge", 5);
        //population.addSpecie("Hippopotamus", 5);
        //population.addSpecie("Pig", 5);

        final double reference = 0.;
        Evaluation result = controller.applyTo(population, reference);
        assertEquals(
                "Illegal update of the population size",
                population.getIndividualCount(),
                result.getPopulation().getIndividualCount());

        assertEquals(
                "Illegal update of the number of species",
                population.getSpecies().size(),
                result.getPopulation().getSpecies().size());

        assertEquals(
                "Unacceptable diversity error",
                reference,
                result.getDiversity(),
                1e-10);

        assertEquals(
                "Unacceptable control error",
                0.,
                result.getError(),
                1e-10);
    }

    /**
     * Test that the controller is able to raise the diversity to its maximum
     * value.
     */
    @Test
    public void testMaximizeDiversity() {
        Controller controller = factory();

        Population population = new Population();
        population.addSpecie("Tiger", 20);
        population.addSpecie("Sludge", 0);
        //population.addSpecie("Hippopotamus", 0);
        //population.addSpecie("Pig", 0);

        final double reference = 1.;
        Evaluation result = controller.applyTo(population, reference);
        assertEquals(
                "Illegal update of the population size",
                population.getIndividualCount(),
                result.getPopulation().getIndividualCount());

        assertEquals(
                "Illegal update of the number of species",
                population.getSpecies().size(),
                result.getPopulation().getSpecies().size());

        assertEquals(
                "Unacceptable diversity error",
                reference,
                result.getDiversity(),
                1e-10);

        assertEquals(
                "Unacceptable control error",
                0.,
                result.getError(),
                1e-10);
    }


}