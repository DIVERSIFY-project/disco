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
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.TrueDiversity;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * General set of test which should work for each controller
 */
@RunWith(JUnit4.class)
public abstract class SolverTest extends TestCase {

    public static final double MAXIMUM_DIVERSITY = 1D;
    public static final double MINIMUM_DIVERSITY = 0D;
    public static final double MAXIMUM_ERROR = 1e-10;
    final Mockery context;

    public SolverTest() {
        this.context = new JUnit4Mockery();
    }

    /**
     * @return a fresh instance of the solver to test.
     */
    public abstract Solver solverUnderTest();

    private Problem theProblem(double reference, Integer... distribution) {
        return aProblem()
                .withInitialPopulation(aPopulation()
                .withDistribution(distribution)
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies())
                .withReferenceDiversity(reference)
                .withDiversityMetric(new TrueDiversity().normalise())
                .build();
    }

    @Test
    public void testSolverName() {
        final String solverName = solverUnderTest().getName();
        verifySolverName(solverName);
    }

    public abstract void verifySolverName(String solverName);

    /**
     * Check that the controller is able to drop the diversity to its lowest
     * possible value (i.e., zero)
     */
    @Test
    public void testMinimizeDiversity() {
        final Solver solver = solverUnderTest();
        final Problem problem = theProblem(MINIMUM_DIVERSITY, 10, 10);

        Solution result = solver.solve(problem);

        verifySolution(result);
    }

    /**
     * Test that the controller is able to raise the diversity to its maximum
     * value.
     */
    @Test
    public void testMaximizeDiversity() {
        final Solver controller = solverUnderTest();
        final Problem problem = theProblem(MAXIMUM_DIVERSITY, 20, 0);

        Solution result = controller.solve(problem);

        verifySolution(result);
    }

    private void verifySolution(Solution solution) {
        final Population population = solution.getPopulation();
        final Population expected = solution.getProblem().getInitialPopulation();

        assertThat("head count",
                   population.getTotalHeadcount(),
                   is(equalTo(expected.getTotalHeadcount())));

        assertThat("species count",
                   population.getSpeciesCount(),
                   is(equalTo(expected.getSpeciesCount())));

        assertThat("error", solution.getError(), is(lessThan(MAXIMUM_ERROR)));
    }

    @Test
    public void testNotificationOfIntermediateSolutions() {
        Population population = aPopulation()
                .withDistribution(1, 1, 12)
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .build();

        Problem problem = aProblem()
                .withDiversityMetric(new TrueDiversity().normalise())
                .withInitialPopulation(population)
                .withReferenceDiversity(0.95)
                .build();

        Solver solver = solverUnderTest();
        final SolverListener listener = context.mock(SolverListener.class);

        context.checking(new Expectations() {
            {
                atLeast(1).of(listener).onIntermediateSolution(with(aNonNull(Solution.class)));
                exactly(1).of(listener).onFinalSolution(with(aNonNull(Solution.class)));
            }
        });

        solver.solve(problem);
    }
}