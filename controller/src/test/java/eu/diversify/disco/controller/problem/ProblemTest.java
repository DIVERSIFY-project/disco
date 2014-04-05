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
import eu.diversify.disco.population.diversity.TrueDiversity;
import eu.diversify.disco.population.diversity.ShannonIndex;
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
        Population population = null;
        aProblem()
                .withInitialPopulation(population) 
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithEmptyAsInitialPopulation() {
        aProblem()
                .withInitialPopulation(aPopulation().withDistribution(0, 0, 0, 0))
                .withReferenceDiversity(0.25)
                .withDiversityMetric(new TrueDiversity())
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithAReferenceIrrelevantForTheSelectedMetric() {
        aProblem()
                .withInitialPopulation(aPopulation().withDistribution(3, 4, 5, 6))
                .withReferenceDiversity(75)
                .withDiversityMetric(new TrueDiversity())
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithNullAsDiversityMetric() {
        Population population = aPopulation().withDistribution(3, 4, 5, 6).build();
        aProblem()
                .withInitialPopulation(aPopulation().withDistribution(3, 4, 5, 6))
                .withReferenceDiversity(0.25)
                .withDiversityMetric(null)
                .build();
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
        Problem problem = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertTrue(
                "A case shall be equal to itself",
                problem.equals(problem));
    }

    @Test
    public void testEqualityWithNull() {
        Problem problem = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertFalse(
                "A case shall not be equal to 'null'",
                problem.equals(null));
    }

    @Test
    public void testEqualityWithIncompatibleObject() {
        Problem problem = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertFalse(
                "A case shall not be equals to an object with an incompatible type",
                problem.equals(new Double(23D)));
    }

    @Test
    public void testEqualityWithTwoEquivalentProblems() {
        Problem problemA = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Problem problemB = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertTrue(
                "A case shall be equal to an equivalent case",
                problemA.equals(problemB));
    }

    @Test
    public void testEqualityWithProblemsWhoseInitialPopulationIsDifferent() {
        Problem problemA = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Problem problemB = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23, 8))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        assertFalse(
                "Two problems shall be different if their population are different",
                problemA.equals(problemB));

    }

    @Test
    public void testEqualityWithProblemsWhoseReferenceIsDifferent() {
        Problem problemA = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Problem problemB = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.85) // A different reference
                .build();

        assertFalse(
                "Two problems shall be different is their reference as different",
                problemA.equals(problemB));
    }

    @Test
    public void testEqualityWithProblemsWhoseMetricIsDifferent() {
        Problem problemA = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new TrueDiversity())
                .withReferenceDiversity(1.75)
                .build();

        Problem problemB = aProblem()
                .withInitialPopulation(aPopulation().withDistribution(10, 23))
                .withDiversityMetric(new ShannonIndex()) // A different metric
                .withReferenceDiversity(0.5) // Needed as the shannon index imply a different scale
                .build();

        assertFalse(
                "Two problems shall be different if their metrics are different",
                problemA.equals(problemB));

    }
}
