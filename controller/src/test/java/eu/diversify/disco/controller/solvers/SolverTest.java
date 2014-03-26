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
package eu.diversify.disco.controller.solvers;

import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.constraints.Constraint;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.NormalisedDiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
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
public abstract class SolverTest extends TestCase {

    /**
     * @return a fresh instance of the controller to test.
     */
    public abstract IterativeSearch factory();

    /**
     * Check that the controller is able to drop the diversity to its lowest
     * possible value (i.e., zero)
     */
    @Test
    public void testMinimizeDiversity() {
        IterativeSearch controller = factory();

        Population population = aPopulation().withDistribution(5, 5).build();

        final double reference = 0.;

        final Problem problem = new Problem(population, reference, new NormalisedDiversityMetric(new TrueDiversity()), new ArrayList<Constraint>());
        Solution result = controller.solve(problem);
        assertEquals(
                "Illegal update of the population size",
                population.getTotalNumberOfIndividuals(),
                result.getPopulation().getTotalNumberOfIndividuals());

        assertEquals(
                "Illegal update of the number of species",
                population.getNumberOfSpecies(),
                result.getPopulation().getNumberOfSpecies());

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
        IterativeSearch controller = factory();

        Population population = aPopulation().withDistribution(20, 0).build();

        final double reference = 1.;

        final Problem problem = new Problem(population, reference, new NormalisedDiversityMetric(new TrueDiversity()), new ArrayList<Constraint>());
        Solution result = controller.solve(problem);

        assertEquals(
                "Illegal update of the population size",
                population.getTotalNumberOfIndividuals(),
                result.getPopulation().getTotalNumberOfIndividuals());

        assertEquals(
                "Illegal update of the number of species",
                population.getNumberOfSpecies(),
                result.getPopulation().getNumberOfSpecies());

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