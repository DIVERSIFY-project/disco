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
package eu.diversify.disco.controller;

import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.solvers.SolverFactory;
import eu.diversify.disco.controller.solvers.SolverListener;
import eu.diversify.disco.controller.solvers.searches.AdaptiveHillClimbing;
import eu.diversify.disco.controller.solvers.searches.HillClimbing;
import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.disco.controller.problem.ProblemBuilder.aProblem;
import static eu.diversify.disco.population.PopulationBuilder.aPopulation;

/**
 * This is a sandbox test to experiment with the controller behaviour.
 *
 * By default, this test is ignored.
 */
@RunWith(JUnit4.class)
public class SandboxTest {

    @Test
    public void sandboxTest() {

        final Reference setPoint = new Reference() {

            @Override
            public double getReference() {
                return 0.95;
            }

            @Override
            public void subscribe(Reference.Listener... listener) {

            }
        };

        final Population population = aPopulation()
                .withDistribution(1, 8, 2, 1, 8)
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .withAtLeast(1, "s1")
                .withAtMost(1, "s1")
                .build();

        final PopulationReader input = new PopulationReader() {

            @Override
            public Population read() {

                return population;
            }
        };

        final PopulationWriter output = new PopulationWriter() {

            @Override
            public void write(Solution solution) {
                System.out.println(solution.getPopulation().toString());
            }
        };

        System.out.println("Initial Population: " + population);
        final DiversityMetric trueDiversity = new TrueDiversity().normalise();
        System.out.println("Diveristy: " + trueDiversity.applyTo(population));
        
        final ProblemBuilder problemTemplate = aProblem()
                .withDiversityMetric(trueDiversity);
        
        final IterativeSearch solver = new IterativeSearch(new AdaptiveHillClimbing());
        solver.subscribe(new SolverListener() {
            
            @Override
            public void onInitialSolution(Solution solution) {
            }

            @Override
            public void onIntermediateSolution(Solution solution) {
                System.out.println(solution);
            }

            @Override
            public void onFinalSolution(Solution solution) {
            }
        });
        
        final Controller facade = new Controller(
                problemTemplate,
                setPoint,
                input, solver,
                output);

        facade.control();

    }

}
