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
package eu.diversify.disco.controller.problem;

import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.HashSet;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Check the behaviour of the result object
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class SolutionTest extends TestCase {

    @Test
    public void testRefinement() {
        Problem problem = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(3, 2, 1).build())
                .withReferenceDiversity(2.0)
                .withDiversityMetric(new TrueDiversity())
                .build();

        Solution initial = problem.getInitialEvaluation();

        Action refinement = new ShiftNumberOfIndividualsIn(2, +3);
        Solution next = initial.refineWith(refinement);

        assertEquals(
                "Solution shall be immutable!",
                aPopulation().withDistribution(3, 2, 1).build(),
                initial.getPopulation());

        assertEquals(
                "Solution shall take into account refinements!",
                aPopulation().withDistribution(3, 5, 1).build(),
                next.getPopulation());
    }

    /**
     * Test the equality operator between two evaluations
     */
    @Test
    public void testEquality() {
        Population population = aPopulation().withDistribution(5, 4).build();
        Problem problem = aProblem()
                .withInitialPopulation(population)
                .withReferenceDiversity(1.65)
                .withDiversityMetric(new TrueDiversity())
                .build();
        Solution e1 = problem.evaluate(population);
        assertTrue(
                "An evaluation should equal itself",
                e1.equals(e1));


        final Solution e2 = problem.evaluate(population);
        assertTrue(
                "Two evaluations of similar populations for a given case shall be equal",
                e1.equals(e2));

        Population p2 = aPopulation().withDistribution(6, 3).build();
        final Solution e3 = problem.evaluate(p2);
        assertFalse(
                "Two evaluations of two different populations shall not be equal",
                e1.equals(e3));

    }
    
    /**
     * Test the formatting of the result
     */
    @Test
    public void testFormatting() {
        Population population = aPopulation().withDistribution(5, 4).build();
        Problem problem = aProblem()
                .withInitialPopulation(population)
                .withReferenceDiversity(1.65)
                .withDiversityMetric(new TrueDiversity())
                .build();
        
        Solution result = problem.evaluate(population);
        String text = result.toString();
        assertThat("about diversity", text, containsString("Diversity:"));
        assertThat("about population", text, containsString("Population:"));
        assertThat("about error", text, containsString("Error:"));
    }

    /**
     * Test placing evaluation in a collections
     */
    @Test
    public void testCollectionUse() {
        Population population = aPopulation().withDistribution(5, 4).build();
        Problem problem = aProblem()
                .withInitialPopulation(population)
                .withReferenceDiversity(0.25)
                .withDiversityMetric(new TrueDiversity().normalise())
                .build();
        final Solution e = problem.evaluate(population);
        final HashSet<Solution> set = new HashSet<Solution>();
        set.add(problem.evaluate(population));

        assertTrue(
                "collections do not recognize the same evaluation",
                set.contains(e));

        assertTrue(
                "collections do not recognize similar evaluations",
                set.contains(problem.evaluate(population)));


    }
}