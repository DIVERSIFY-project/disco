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
import static eu.diversify.disco.population.PopulationBuilder.*;

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
        Population first = aPopulation().withDistribution(1, 2, 3, 4).build();
        Population second = aPopulation().build();
        assertNotSame(first, second);
        assertTrue(second.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSpeciesNames() {
        Population population = aPopulation()
                .withDistribution(1, 3, 2, 5)
                .withSpeciesNamed("s1", "s3")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIndividualCounts() {
        Population population = aPopulation()
                .withDistribution(1, 3)
                .withSpeciesNamed("s1", "s2", "s3")
                .build();
    }
    
    public void testDuplicateCallToImmuatble() {
        Population population = aPopulation()
                .immutable()
                .withDistribution(1, 2, 3)
                .withSpeciesNamed("s1", "s2", "s3")
                .immutable()
                .build();
    }

    @Test
    public void testClonePopulation() {
        Population population = aPopulation().withDistribution(1, 2, 3, 4).build();
        Population clone = aPopulation().clonedFrom(population).build();
        assertNotNull(clone);
        assertNotSame(population, clone);
        assertEquals(population, clone);
    }

    @Test
    public void testWithDistribution() {
        Population population = aPopulation()
                .withDistribution(3, 2, 1)
                .build();
        assertEquals(3, population.getNumberOfSpecies());
        assertEquals(3, population.getNumberOfIndividualsIn(1));
        assertEquals(2, population.getNumberOfIndividualsIn(2));
        assertEquals(1, population.getNumberOfIndividualsIn(3));
      }

        @Test
    public void testWithSpeciesNames() {
        Population population = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .build();
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
        Population population = aPopulation()
                .withDistribution(1, 2, 3, 4)
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .build();
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
        Population population = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(1, 2, 3, 4)
                .build();
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
        Population population = aPopulation()
                .withDistribution(1, 2, 3, 4)
                .immutable()
                .build();

        Population population2 = population.shiftNumberOfIndividualsIn(1, +2);
        assertNotSame(population, population2);
        assertEquals(1, population.getNumberOfIndividualsIn(1));
        assertEquals(3, population2.getNumberOfIndividualsIn(1));
    }
    
        @Test
    public void testEmptyPopulation() {
        Population population = aPopulation().build();
        assertTrue(population.isEmpty());
    }


    @Test
    public void testEqualsWithImmutableEquivalent() {
        Population p1 = aPopulation().withDistribution(1, 2, 3).build();
        Population p2 = aPopulation().withDistribution(1, 2, 3).immutable().build();
        assertEquals(p1, p2);
    }

    @Test
    public void testEqualsWithDynamicEquivalent() {
        Population p1 = aPopulation().withDistribution(1, 2, 3).build();
        Population p2 = aPopulation().withDistribution(1, 2, 3).dynamic().build();
        assertEquals(p1, p2);
    }
}