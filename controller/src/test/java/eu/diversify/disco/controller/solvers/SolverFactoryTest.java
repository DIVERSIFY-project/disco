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

import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.controller.problem.Problem;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.controller.solvers.searches.AdaptiveHillClimbing;
import eu.diversify.disco.controller.solvers.searches.BreadthFirst;
import eu.diversify.disco.controller.solvers.searches.HillClimbing;
import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import eu.diversify.disco.controller.solvers.searches.SearchStrategy;
import eu.diversify.disco.population.diversity.TrueDiversity;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the controller factory
 */
@RunWith(JUnit4.class)
public class SolverFactoryTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAdaptiveHillClimbingInstantiation() {
        final SolverFactory factory = new SolverFactory();
        final IterativeSearch ahc = (IterativeSearch) factory.instantiate("Adaptive Hill Climbing");
        SearchStrategy strategy = ahc.getSearchFactory();

        assertEquals(
                "Wrong controller instantiation",
                AdaptiveHillClimbing.class,
                strategy.getClass());
    }

    @Test
    public void testHillClimbingInstantiation() {
        final SolverFactory factory = new SolverFactory();
        final IterativeSearch ahc = (IterativeSearch) factory.instantiate("Hill Climbing");
        SearchStrategy strategy = ahc.getSearchFactory();

        assertEquals(
                "Wrong controller instantiation",
                HillClimbing.class,
                strategy.getClass());
    }

    @Test
    public void testBreadthFirstInstantiation() {
        final SolverFactory factory = new SolverFactory();
        final IterativeSearch ahc = (IterativeSearch) factory.instantiate("Breadth-First Search");
        SearchStrategy strategy = ahc.getSearchFactory();

        assertEquals(
                "Wrong controller instantiation",
                BreadthFirst.class,
                strategy.getClass());
    }

    @Test
    public void testEscapingName() {
        final SolverFactory factory = new SolverFactory();
        final IterativeSearch ahc = (IterativeSearch) factory.instantiate(" Hill    ClimBING");
        SearchStrategy strategy = ahc.getSearchFactory();

        assertEquals(
                "Wrong controller instantiation",
                HillClimbing.class,
                strategy.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownStrategyDetection() {
        final SolverFactory factory = new SolverFactory();
        factory.instantiate(" Hill   CLIMBING 2 ");
    }

}
