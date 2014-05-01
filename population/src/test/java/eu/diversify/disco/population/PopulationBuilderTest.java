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

package eu.diversify.disco.population;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static eu.diversify.disco.population.PopulationBuilder.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of the population builder
 */
@RunWith(JUnit4.class)
public class PopulationBuilderTest extends TestCase {

    @Test
    public void buildShouldNotHaveMemory() {
        Population first = aPopulation().withDistribution(1, 2, 3, 4).build();

        Population second = aPopulation().build();

        assertThat("not the same", first, is(not(sameInstance(second))));
        assertThat("default population", second.isEmpty());
    }

    @Test
    public void buildShouldAcceptAMapFromNameToHeadcount() {
        final HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("s1", 2);
        map.put("s2", 3);
        map.put("s3", 4);

        Map<String, Integer> out = aPopulation().fromMap(map).build().toMap();

        assertThat("same entries", out.entrySet(), is(equalTo(map.entrySet())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildShouldRejectMissingSpecieName() {
        Population population = aPopulation()
                .withDistribution(1, 3, 2, 5)
                .withSpeciesNamed("s1", "s3")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildShouldRejectMissingHeadcount() {
        Population population = aPopulation()
                .withDistribution(1, 3)
                .withSpeciesNamed("s1", "s2", "s3")
                .build();
    }

    public void buildShouldAcceptDuplicateCallsToImmutable() {
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
        Population clone = population.deepCopy().build();
        assertNotNull(clone);
        assertNotSame(population, clone);
        assertEquals(population, clone);
    }

    @Test
    public void buildShouldAcceptTheDistributionOfIndividuals() {
        Population population = aPopulation()
                .withDistribution(3, 2, 1)
                .build();

        assertThat("distribution", population.getDistribution(), contains(3, 2, 1));
    }

    @Test
    public void buildShouldAcceptTheSpeciesName() {
        Population population = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .build();
        assertThat("species names", population.getSpeciesNames(), contains("s1", "s2", "s3", "s4"));
    }

    @Test
    public void buildShouldAcceptSpecieNameAndDistribution() {
        Population population = aPopulation()
                .withDistribution(1, 2, 3, 4)
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .build();

        assertThat("distribution", population.getDistribution(), contains(1, 2, 3, 4));
        assertThat("species names", population.getSpeciesNames(), contains("s1", "s2", "s3", "s4"));
    }

    @Test
    public void buildShouldAcceptSpecieNamesAndDistributionInAnyOrder() {
        Population population = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(1, 2, 3, 4)
                .build();

        assertThat("distribution", population.getDistribution(), contains(1, 2, 3, 4));
        assertThat("species names", population.getSpeciesNames(), contains("s1", "s2", "s3", "s4"));
    }

    @Test
    public void buildShouldAcceptImmutability() {
        Population population = aPopulation()
                .withDistribution(1, 2, 3, 4)
                .immutable()
                .build();

        Population population2 = population.getSpecie(1).shiftHeadcountBy(+2);
        assertNotSame(population, population2);
        assertEquals(1, population.getSpecie(1).getHeadcount());
        assertEquals(3, population2.getSpecie(1).getHeadcount());
    }

    @Test
    public void buildShouldReturnAnEmptyPopulationByDefault() {
        Population population = aPopulation().build();
        assertTrue(population.isEmpty());
    }

}