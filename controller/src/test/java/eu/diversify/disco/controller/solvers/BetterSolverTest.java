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
/*
 */
package eu.diversify.disco.controller.solvers;

import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;

import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.MetricFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class BetterSolverTest extends TestCase {

 

    private final String name;
    private final Population population;
    private final DiversityMetric diversity;
    private final Solver solver;
    private final double reference;

    public BetterSolverTest(String name, Population population, DiversityMetric diversity, Solver solver, double reference) {
        this.name = name;
        this.population = population;
        this.diversity = diversity;
        this.solver = solver;
        this.reference = reference;
    }

    @Test
    public void testSolver() {
        Problem problem = aProblem()
                .withInitialPopulation(population)
                .withDiversityMetric(diversity)
                .withReferenceDiversity(reference)
                .build();

        Solution solution = solver.solve(problem);

        assertThat("error small enough", solution.getError(), is(lessThan(0.05)));
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> examples() {
        final Collection<Object[]> examples = new ArrayList<Object[]>();
        for (Population population : testedPopulations()) {
            for (Solver solver : testedSolvers()) {
                for (DiversityMetric diversity : testedDiversityMetrics()) {
                    for (double reference : testedReferences()) {
                        final String name = makeNameForExample( population, diversity, solver, reference);
                        examples.add(new Object[]{name, population, diversity,
                                                  solver,
                                                  reference});
                    }
                }
            }
        }
        return examples;
    }

    private static Collection<Population> testedPopulations() {
        final ArrayList<Population> testedPopulations = new ArrayList<Population>();
        testedPopulations.add(aPopulation().withDistribution(16, 0, 0, 0).withFixedNumberOfIndividuals().withFixedNumberOfSpecies().build());
        testedPopulations.add(aPopulation().withDistribution(4, 4, 4, 4).withFixedNumberOfIndividuals().withFixedNumberOfSpecies().build());
        testedPopulations.add(aPopulation().withDistribution(1, 7, 5, 3).withFixedNumberOfIndividuals().withFixedNumberOfSpecies().build());
        return testedPopulations;
    }

    private static Collection<Solver> testedSolvers() {
        final ArrayList<Solver> testedSolvers = new ArrayList<Solver>();
        final SolverFactory factory = new SolverFactory();
        testedSolvers.add(factory.instantiate("Hill Climbing"));
        testedSolvers.add(factory.instantiate("Adaptive Hill Climbing"));
        return testedSolvers;
    }

    private static Collection<DiversityMetric> testedDiversityMetrics() {
        final ArrayList<DiversityMetric> testedMetrics = new ArrayList<DiversityMetric>();
        testedMetrics.add(MetricFactory.create("true diversity (theta = 2)").normalise());
        testedMetrics.add(MetricFactory.create("shannon index").normalise());
        testedMetrics.add(MetricFactory.create("gini simpson index").normalise());
        return testedMetrics;
    }

    private static Collection<Double> testedReferences() {
        return Arrays.asList(new Double[]{0.00, 0.25, 0.50, 0.75, 1.00});
    }
    
       private static String makeNameForExample(Population population, DiversityMetric metric, Solver solver, double reference) {
        final String distribution = population.getDistribution().toString();
       
        return String.format("%s to %.2f (%s/%s)", distribution, reference, metric.getClass().getName(), "solver");
    }
}