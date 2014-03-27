
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
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.TrueDiversity;
import eu.diversify.disco.population.diversity.ShannonIndex;
import java.util.ArrayList;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the control case
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ProblemTest extends TestCase {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithNullAsInitialPopulation() {
        Problem problem = new Problem(null, 0.25, new TrueDiversity(), new ArrayList<Constraint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithEmptyAsInitialPopulation() {
        Population population = aPopulation().withDistribution(0, 0, 0, 0).build();
        Problem problem = new Problem(population, 0.25, new TrueDiversity(), new ArrayList<Constraint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithAReferenceIrrelevantForTheSelectedMetric() {
        Population population = aPopulation().withDistribution(3, 4, 5, 6).build();
        Problem problem = new Problem(population, 75, new TrueDiversity(), new ArrayList<Constraint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithNullAsDiversityMetric() {
        Population population = aPopulation().withDistribution(3, 4, 5, 6).build();
        Problem problem = new Problem(population, 0.25, null, new ArrayList<Constraint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithNullAsConstraints() {
        Population population = aPopulation().withDistribution(3, 4, 5, 6).build();
        Problem problem = new Problem(population, 0.25, new TrueDiversity(), null);
    }

    @Test
    public void testGetInitialEvaluation() {
        Population initial = aPopulation().withDistribution(10, 23).build();
        Problem problem = aProblem()
                .withInitialPopulation(initial)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertEquals(initial, problem.getInitialEvaluation().getPopulation());
    }

    // TODO: test that evaluate return the proper error calculation
    // TODO: test evaluate on null
    // TODO: Test evaluate on empty
    // TODO: Test is legal ???
    @Test
    public void testProblemEqualityWithItself() {
        Population initial = aPopulation().withDistribution(10, 23).build();
        Problem problem = aProblem()
                .withInitialPopulation(initial)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertTrue(
                "A case shall be equal to itself",
                problem.equals(problem));
    }

    @Test
    public void testEqualityWithNull() {
        Population initial = aPopulation().withDistribution(10, 23).build();
        Problem problem = aProblem()
                .withInitialPopulation(initial)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertFalse(
                "A case shall not be equal to 'null'",
                problem.equals(null));
    }

    @Test
    public void testEqualityWithIncompatibleObject() {
        Population initial = aPopulation().withDistribution(10, 23).build();
        Problem problem = aProblem()
                .withInitialPopulation(initial)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertFalse(
                "A case shall not be equals to an object with an incompatible type",
                problem.equals(new Double(23D)));
    }

    @Test
    public void testEqualityWithTwoEquivalentProblems() {
        Population initialA = aPopulation().withDistribution(10, 23).build();
        Problem problemA = aProblem()
                .withInitialPopulation(initialA)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Population initialB = aPopulation().withDistribution(10, 23).build();

        Problem problemB = aProblem()
                .withInitialPopulation(initialB)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertTrue(
                "A case shall be equal to an equivalent case",
                problemA.equals(problemB));
    }

    @Test
    public void testEqualityWithProblemsWhoseInitialPopulationIsDifferent() {
        Population initialA = aPopulation().withDistribution(10, 23).build();
        Problem problemA = aProblem()
                .withInitialPopulation(initialA)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Population initialB = aPopulation().withDistribution(10, 23, 8).build();
        Problem problemB = aProblem()
                .withInitialPopulation(initialB)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertFalse(
                "Two problems shall be different if their population are different",
                problemA.equals(problemB));

    }

    @Test
    public void testEqualityWithProblemsWhoseReferenceIsDifferent() {
        Population initialA = aPopulation().withDistribution(10, 23).build();
        Problem problemA = aProblem()
                .withInitialPopulation(initialA)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Population initialB = aPopulation().withDistribution(10, 23).build();
        Problem problemB = aProblem()
                .withInitialPopulation(initialB)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.85) // A different reference
                .build();

        assertFalse(
                "Two problems shall be different is their reference as different",
                problemA.equals(problemB));
    }

    @Test
    public void testEqualityWithProblemsWhoseMetricIsDifferent() {
        Population initialA = aPopulation().withDistribution(10, 23).build();
        Problem problemA = aProblem()
                .withInitialPopulation(initialA)
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Population initialB = aPopulation().withDistribution(10, 23).build();

        Problem problemB = aProblem()
                .withInitialPopulation(initialB)
                .withDiversityMetric(new ShannonIndex()) // A different metric
                .withReferenceDiversity(0.5) // Needed as the shannon index imply a different scale
                .build();

        assertFalse(
                "Two problems shall be different if their metrics are different",
                problemA.equals(problemB));

    }
}
