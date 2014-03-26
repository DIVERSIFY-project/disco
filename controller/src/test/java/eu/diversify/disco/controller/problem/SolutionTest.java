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

import eu.diversify.disco.controller.problem.constraints.Constraint;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.Script;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(aPopulation().withDistribution(3, 2, 1).build())
                .withReferenceDiversity(2.0)
                .withDiversityMetric(new TrueDiversity())
                .make();

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
        Population p = aPopulation().withDistribution(5, 4).build();
        Problem c = new Problem(p, 1.65, new TrueDiversity(), new ArrayList<Constraint>());
        Solution e1 = c.evaluate(p);
        assertTrue(
                "An evaluation should equal itself",
                e1.equals(e1));


        final Solution e2 = c.evaluate(p);
        assertTrue(
                "Two evaluations of similar populations for a given case shall be equal",
                e1.equals(e2));

        Population p2 = aPopulation().withDistribution(6, 3).build();
        final Solution e3 = c.evaluate(p2);
        assertFalse(
                "Two evaluations of two different populations shall not be equal",
                e1.equals(e3));

    }

    /**
     * Test the linkage between evaluation of partial solutions
     */
    @Test
    public void testLinkage() {

        Population p = aPopulation().withDistribution(5, 4).build();
        final Problem c = new Problem(p, 1.65, new TrueDiversity(), new ArrayList<Constraint>());

        Population p2 = aPopulation().withDistribution(6, 3).build();
        final Solution e1 = c.evaluate(p2);
        assertFalse(
                "Shall not have a previous evaluation",
                e1.hasPrevious());
        assertEquals(
                "Wrong number of iteration",
                1,
                e1.getIteration());


        final Action u = new Script(Arrays.asList(new Action[]{
            new ShiftNumberOfIndividualsIn(1, +1),
            new ShiftNumberOfIndividualsIn(2, -1)
        }));

        final Solution e2 = e1.refineWith(u);
        assertTrue(
                "Shall have a previous evaluation",
                e2.hasPrevious());
        assertNotNull(
                "The previous shall not be null",
                e2.getPrevious());
        assertEquals(
                "Wrong number of iteration",
                1,
                e1.getIteration());
    }

    /**
     * Test the formatting of the result
     */
    @Test
    public void testFormatting() {
        Population p = aPopulation().withDistribution(5, 4).build();
        Problem c = new Problem(p, 1.65, new TrueDiversity(), new ArrayList<Constraint>());

        Solution result = c.evaluate(p);
        String text = result.toString();
        assertEquals(
                "Wrong formatting of evaluation",
                " - Iteration count: 1\n - Population: [ sp. #1: 5, sp. #2: 4 ]\n - Reference: 1.65\n - Diversity: 1.9877674693472378\n - Error: 0.11408686334923729\n",
                text);
    }

    /**
     * Test placing evaluation in a collections
     */
    @Test
    public void testCollectionUse() {
        Population p = aPopulation().withDistribution(5, 4).build();
        final Problem c = new Problem(p, 1.65, new TrueDiversity(), new ArrayList<Constraint>());

        final Solution e = c.evaluate(p);
        final HashSet<Solution> set = new HashSet<Solution>();
        set.add(c.evaluate(p));

        assertTrue(
                "collections do not recognize the same evaluation",
                set.contains(e));

        assertTrue(
                "collections do not recognize similar evaluations",
                set.contains(c.evaluate(p)));


    }
}