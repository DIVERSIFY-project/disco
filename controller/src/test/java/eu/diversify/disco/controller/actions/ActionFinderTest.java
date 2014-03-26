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
package eu.diversify.disco.controller.actions;

import eu.diversify.disco.controller.exploration.ExplorationStrategy;
import eu.diversify.disco.controller.exploration.IndividualPermutationExplorer;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.diversity.ShannonIndex;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class ActionFinderTest extends TestCase {

    @Test
    public void testImmutabilityOfSolution() {
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(new PopulationBuilder().withDistribution(5, 5).make())
                .withDiversityMetric(new ShannonIndex())
                .withReferenceDiversity(0.5)
                .make();

        ExplorationStrategy finder = new IndividualPermutationExplorer();
        for (Action action: finder.search(problem.getInitialEvaluation(), 1)) {
            problem.getInitialEvaluation().refineWith(action);
        }
        
        assertEquals(
                "Action finder shall never modify the population explored",
                new PopulationBuilder().withDistribution(5, 5).make(),
                problem.getInitialEvaluation().getPopulation());
    }
    
    @Test
    public void testFindAllPermutationsOfIndividualsInMinimalPopulation() {
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(new PopulationBuilder().withDistribution(1, 1).make())
                .withDiversityMetric(new ShannonIndex())
                .withReferenceDiversity(0.5)
                .make();

        ExplorationStrategy finder = new IndividualPermutationExplorer();

        Set<Population> actual = new HashSet<Population>();
        final int scaleFactor = 1;
        for (Action action : finder.search(problem.getInitialEvaluation(), scaleFactor)) {
            actual.add(action.applyTo(problem.getInitialPopulation()));
        }

        Set<Population> expected = new HashSet<Population>();
        expected.add(new PopulationBuilder().withDistribution(2, 0).make());
        expected.add(new PopulationBuilder().withDistribution(0, 2).make());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllPermutationsOfIndividualsInMinimalPopulationWithScaleFactor() {
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(new PopulationBuilder().withDistribution(4, 4).make())
                .withDiversityMetric(new ShannonIndex())
                .withReferenceDiversity(0.5)
                .make();

        ExplorationStrategy finder = new IndividualPermutationExplorer();

        Set<Population> actual = new HashSet<Population>();
        final int scaleFactor = 2;
        for (Action action : finder.search(problem.getInitialEvaluation(), scaleFactor)) {
            actual.add(action.applyTo(problem.getInitialPopulation()));
        }

        Set<Population> expected = new HashSet<Population>();
        expected.add(new PopulationBuilder().withDistribution(6, 2).make());
        expected.add(new PopulationBuilder().withDistribution(2, 6).make());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllPermutationsOfIndividuals() {
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(new PopulationBuilder().withDistribution(2, 2, 2).make())
                .withDiversityMetric(new ShannonIndex())
                .withReferenceDiversity(0.5)
                .make();

        ExplorationStrategy finder = new IndividualPermutationExplorer();

        Set<Population> actual = new HashSet<Population>();
        final int scaleFactor = 1;
        for (Action action : finder.search(problem.getInitialEvaluation(), scaleFactor)) {
            actual.add(action.applyTo(problem.getInitialPopulation()));
        }

        Set<Population> expected = new HashSet<Population>();
        expected.add(new PopulationBuilder().withDistribution(3, 1, 2).make());
        expected.add(new PopulationBuilder().withDistribution(3, 2, 1).make());
        expected.add(new PopulationBuilder().withDistribution(2, 3, 1).make());
        expected.add(new PopulationBuilder().withDistribution(1, 3, 2).make());
        expected.add(new PopulationBuilder().withDistribution(1, 2, 3).make());
        expected.add(new PopulationBuilder().withDistribution(2, 1, 3).make());

        assertEquals(expected, actual);
    }
}