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

import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;

import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.solvers.searches.BreadthFirst;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.NormalisedDiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Test the implementation of the Breadth-first search strategy.
 */
@RunWith(JUnit4.class)
public class BreadthFirstSearchTest extends SolverTest {

    @Override
    public IterativeSearch solverUnderTest() {
        return new IterativeSearch(new BreadthFirst());
    }

    @Override
    public void verifySolverName(String solverName) {
        final String name = solverName.toLowerCase();
        assertThat("solver name", name, containsString("breadth"));
        assertThat("solver name", name, containsString("first"));
        assertThat("solver name", name, containsString("search"));
    }

    /**
     * Check the behaviour of pushing back the frontier
     */
    @Test
    public void testPushback() {
        Population source = aPopulation()
                .withDistribution(5, 5)
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .build();

        Problem problem = aProblem()
                .withInitialPopulation(source)
                .withDiversityMetric(new NormalisedDiversityMetric(new TrueDiversity()))
                .withReferenceDiversity(0.)
                .build();

        final BreadthFirst search = new BreadthFirst();
        search.setUp(problem);
        assertEquals(
                "Pushback did not augment properly the set of explored populations",
                1,
                search.getExplored().size());

        assertTrue(
                "Pushback did not augment the set of explored populations",
                search.getExplored().contains(problem.evaluate(source)));

        assertEquals(
                "Pushback did not compute enough new state to add in the frontier",
                2,
                search.getFrontier().size());


    }
}
