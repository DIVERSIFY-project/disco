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
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import static eu.diversify.disco.population.actions.ScriptBuilder.*;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

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
        Population population = aPopulation().withDistribution(10, 10, 10).build();

        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.applyTo(population);
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
        Population population = aPopulation().withDistribution(30, 0, 0).build();

        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.applyTo(population);
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
        Population population = aPopulation().withDistribution(10, 13, 6).build();

        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.applyTo(population);
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
        Population population = aPopulation().withDistribution(15, 5, 3).build();
        final double d1 = metric.applyTo(population);
        Population populationX2 = aPopulation().withDistribution(30, 10, 6).build();
        final double d2 = metric.applyTo(populationX2);
        assertEquals(
                "Scaled population shall have the same diviersty",
                d1,
                d2,
                1e-10);

    }

    /**
     * Check that the diversity is undefined for an empty population
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUndefinedDiversity() {
        Population population = aPopulation().build();
        DiversityMetric metric = newMetricUnderTest();
        metric.applyTo(population);
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
    
    @Test
    public void testMonotony() {
        final DiversityMetric metric = newMetricUnderTest();
     
        Population population = aPopulation()
                .withDistribution(49, 1)
                .build();
        
        final Action transfert = aScript()
                .shift(1, -1)
                .shift(2, +1)
                .build();
        
        double previous = metric.applyTo(population);
        while(!population.isUniformlyDistributed()) {
            population = transfert.applyTo(population);
            double next = metric.applyTo(population);
            assertThat("diversity increases", next, is(greaterThan(previous)));
            previous = next;
        }
    }
}
