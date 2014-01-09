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
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * General set of test for each diversity metric.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class DiversityMetricTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * @return a fresh diversity metric to be tested
     */
    public abstract DiversityMetric newMetricUnderTest();
    
     /**
     * Check that the diversity of a population whose species are uniform is
     * 1.
     */
    @Test
    public void testMaximumDiversity() {
        Population population = new Population();
        population.addSpecie("Lion", 10);
        population.addSpecie("Tiger", 10);
        population.addSpecie("Elephant", 10);
        
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
        Population population = new Population();
        population.addSpecie("Lion", 30);
        population.addSpecie("Tiger", 0);
        population.addSpecie("Elephant", 0);
        
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
        Population population = new Population();
        population.addSpecie("Lion", 30);
        population.addSpecie("Tiger", 0);
        population.addSpecie("Elephant", 0);
        
        DiversityMetric metric = newMetricUnderTest();
        final double diversity = metric.applyTo(population);
        assertTrue(
                "Wrong diversity metric",
                diversity < 1. && diversity > 0.);
    }
    
    /**
     * Check that the diversity is undefined for an empty population
     */
    @Test(expected = EmptyPopulation.class)
    public void testUndefinedDiversity() {
        Population population = new Population();
        
        DiversityMetric metric = newMetricUnderTest();
        metric.applyTo(population);
    }
}
