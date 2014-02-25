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

package eu.diversify.disco.population.random;

import eu.diversify.disco.population.Population;
import java.util.Iterator;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the generator of random populations
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class GeneratorTest extends TestCase {

    @Test
    public void testNominalUsage() {
        Generator generator = new Generator();

        Profile profile = new Profile();
        profile.setSpeciesCount(5, 50);
        profile.setIndividualsCount(500, 10000);

        testProfile(generator, 1000, profile);
    }

    @Test
    public void testNarrowProfile() {
        Generator generator = new Generator();

        Profile profile = new Profile();
        profile.setSpeciesCount(5, 6);
        profile.setIndividualsCount(500, 501);

        testProfile(generator, 1000, profile);
    }

    /**
     * To test a generator under a given profile, we ask the generator to make
     * makeMany population according to the given profile, and we check whether the
     * the resulting populations match it.
     *
     * @param underTest the generator to test
     * @param count the number of population to generate
     * @param profile the profile to use
     */
    private void testProfile(Generator underTest, int count, Profile profile) {
        Iterator<Population> populations = underTest.makeMany(count, profile);
        int counter = 0;
        while (populations.hasNext()) {
            counter++;
            Population population = populations.next();
            assertTrue("The population does not fit the given profile (p = " + population + ")", profile.matches(population));
        }
        assertEquals("Wrong number of populations generated", count, counter);
    }
}
