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
package eu.diversify.disco.population;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Build another of various configurations
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class PopulationBuilderTest extends TestCase {
    
    
    @Test
    public void testBuilderWithoutMemory() {
        Population first = new PopulationBuilder().withDistribution(1, 2, 3, 4).make();
        Population second = new PopulationBuilder().make();
        assertNotSame(first, second);
        assertTrue(second.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSpeciesNames() {
        Population population = new PopulationBuilder()
                .withDistribution(1, 3, 2, 5)
                .withSpeciesNamed("s1", "s3")
                .make();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIndividualCounts() {
        Population population = new PopulationBuilder()
                .withDistribution(1, 3)
                .withSpeciesNamed("s1", "s2", "s3")
                .make();
    }
    
    public void testDuplicateCallToImmuatble() {
        Population population = new PopulationBuilder()
                .immutable()
                .withDistribution(1, 2, 3)
                .withSpeciesNamed("s1", "s2", "s3")
                .immutable()
                .make();
    }

    @Test
    public void testClonePopulation() {
        Population population = new PopulationBuilder().withDistribution(1, 2, 3, 4).make();
        Population clone = new PopulationBuilder().clonedFrom(population).make();
        assertNotNull(clone);
        assertNotSame(population, clone);
        assertEquals(population, clone);
    }

    @Test
    public void testWithDistribution() {
        Population population = new PopulationBuilder()
                .withDistribution(3, 2, 1)
                .make();
        assertEquals(3, population.getNumberOfSpecies());
        assertEquals(3, population.getNumberOfIndividualsIn(1));
        assertEquals(2, population.getNumberOfIndividualsIn(2));
        assertEquals(1, population.getNumberOfIndividualsIn(3));
      }

        @Test
    public void testWithSpeciesNames() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .make();
        assertEquals(4, population.getNumberOfSpecies());
        assertEquals(0, population.getNumberOfIndividualsIn(1));
        assertEquals(0, population.getNumberOfIndividualsIn("s1"));
        assertEquals(0, population.getNumberOfIndividualsIn(2));
        assertEquals(0, population.getNumberOfIndividualsIn("s2"));
        assertEquals(0, population.getNumberOfIndividualsIn(3));
        assertEquals(0, population.getNumberOfIndividualsIn("s3"));
        assertEquals(0, population.getNumberOfIndividualsIn(4));
        assertEquals(0, population.getNumberOfIndividualsIn("s4"));
    }
    
    @Test
    public void testWithDistributionAndSpeciesNames() {
        Population population = new PopulationBuilder()
                .withDistribution(1, 2, 3, 4)
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .make();
        assertEquals(4, population.getNumberOfSpecies());
        assertEquals(1, population.getNumberOfIndividualsIn(1));
        assertEquals(1, population.getNumberOfIndividualsIn("s1"));
        assertEquals(2, population.getNumberOfIndividualsIn(2));
        assertEquals(2, population.getNumberOfIndividualsIn("s2"));
        assertEquals(3, population.getNumberOfIndividualsIn(3));
        assertEquals(3, population.getNumberOfIndividualsIn("s3"));
        assertEquals(4, population.getNumberOfIndividualsIn(4));
        assertEquals(4, population.getNumberOfIndividualsIn("s4"));
    }

    @Test
    public void testWithSpecieNameBeforeDistribution() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(1, 2, 3, 4)
                .make();
        assertEquals(4, population.getNumberOfSpecies());
        assertEquals(1, population.getNumberOfIndividualsIn(1));
        assertEquals(1, population.getNumberOfIndividualsIn("s1"));
        assertEquals(2, population.getNumberOfIndividualsIn(2));
        assertEquals(2, population.getNumberOfIndividualsIn("s2"));
        assertEquals(3, population.getNumberOfIndividualsIn(3));
        assertEquals(3, population.getNumberOfIndividualsIn("s3"));
        assertEquals(4, population.getNumberOfIndividualsIn(4));
        assertEquals(4, population.getNumberOfIndividualsIn("s4"));
    }

    @Test
    public void testWithImmutability() {
        Population population = new PopulationBuilder()
                .withDistribution(1, 2, 3, 4)
                .immutable()
                .make();

        Population population2 = population.shiftNumberOfIndividualsIn(1, +2);
        assertNotSame(population, population2);
        assertEquals(1, population.getNumberOfIndividualsIn(1));
        assertEquals(3, population2.getNumberOfIndividualsIn(1));
    }
    
        @Test
    public void testEmptyPopulation() {
        Population population = new PopulationBuilder().make();
        assertTrue(population.isEmpty());
    }


    @Test
    public void testEqualsWithImmutableEquivalent() {
        Population p1 = new PopulationBuilder().withDistribution(1, 2, 3).make();
        Population p2 = new PopulationBuilder().withDistribution(1, 2, 3).immutable().make();
        assertEquals(p1, p2);
    }

    @Test
    public void testEqualsWithDynamicEquivalent() {
        Population p1 = new PopulationBuilder().withDistribution(1, 2, 3).make();
        Population p2 = new PopulationBuilder().withDistribution(1, 2, 3).dynamic().make();
        assertEquals(p1, p2);
    }
}