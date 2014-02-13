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

package eu.diversify.disco.population.diversity;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.exceptions.EmptyPopulation;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * General set of test for each diversity metric.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class DiversityMetricTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * @return a fresh diversity metric to be tested
     */
    public abstract DiversityMetric newMetricUnderTest();

    /**
     * Check that the diversity of a population whose species are uniform is 1.
     */
    @Test
    public void testMaximumDiversity() {
        Population population = new Population();
        population.addSpecie("Lion", 10);
        population.addSpecie("Tiger", 10);
        population.addSpecie("Elephant", 10);

        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.normalised(population);
        assertEquals(
                "Wrong diversity metric",
                1.,
                diversity,
                1e-15);

    }

    /**
     * Check that diversity of a population whose individual are all in the same
     * specie is equal to 0.
     */
    @Test
    public void testMinimumDiversity() {
        Population population = new Population();
        population.addSpecie("Lion", 30);
        population.addSpecie("Tiger", 0);
        population.addSpecie("Elephant", 0);

        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.normalised(population);
        assertEquals(
                "Wrong diversity metric",
                0.,
                diversity,
                1e-15);
    }

    /**
     * Check that an mixed population has a diversity level between 0 and 1.
     */
    @Test
    public void testMediumDiversity() {
        Population population = new Population();
        population.addSpecie("Lion", 10);
        population.addSpecie("Tiger", 13);
        population.addSpecie("Elephant", 6);

        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.normalised(population);
        assertTrue(
                "Wrong diversity metric",
                diversity < 1. && diversity > 0.);
    }

    /**
     * Test whether scaling population does preserve the diversity metric
     */
    @Test
    public void testScaling() {
        DiversityMetric metric = newMetricUnderTest();

        Population p1 = new Population();
        p1.addSpecie("Lion", 10);
        p1.addSpecie("Elephant", 5);
        p1.addSpecie("Tiger", 3);
        final double d1 = metric.normalised(p1);

        Population p2 = new Population();
        p2.addSpecie("Lion", 20);
        p2.addSpecie("Elephant", 10);
        p2.addSpecie("Tiger", 6);
        final double d2 = metric.normalised(p2);
        
        
        assertEquals(
                "Scaled population shall have the same diviersty",
                d1,
                d2,
                1e-10
                );
        
    }

    /**
     * Check that the diversity is undefined for an empty population
     */
    @Test(expected = EmptyPopulation.class)
    public void testUndefinedDiversity() {
        Population population = new Population();

        DiversityMetric metric = newMetricUnderTest();
        metric.absolute(population);
    }

    /**
     * The equality of two metrics
     */
    @Test
    public void testEquality() {
        DiversityMetric m1 = new TrueDiversity();
        DiversityMetric m2 = new TrueDiversity();
        DiversityMetric m3 = new ShannonIndex();

        assertTrue(
                "Two instance of the same metric shall be equal",
                m1.equals(m2));

        assertFalse(
                "Two instance of the different metric shall not be equal",
                m1.equals(m3));

    }
}
