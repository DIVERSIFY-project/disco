/**
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
package eu.diversify.disco.population;

import eu.diversify.disco.population.exceptions.DuplicateSpecieId;
import eu.diversify.disco.population.exceptions.NegativeIndividualCount;
import eu.diversify.disco.population.exceptions.UnknownSpecie;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the various features of a population model
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class PopulationTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Check the count of species, under specie addition and removal
     */
    @Test
    public void testPopulationSpeciesCount() throws DuplicateSpecieId {
        Population p = new Population();
        assertEquals("Wrong initial species count",
                0,
                p.getSpecies().size());

        p.addSpecie("Elephant");
        assertEquals("Wrong initial species count after specie addition",
                1,
                p.getSpecies().size());

        p.addSpecie("Hippopotamus");
        assertEquals("Wrong initial species count after specie addition",
                2,
                p.getSpecies().size());

        p.deleteSpecie("Elephant");
        assertEquals("Wrong initial species count after specie deletion",
                1,
                p.getSpecies().size());

    }

    /**
     * Check whether duplicate specie id are caught on specie creation
     *
     * @throws DuplicateSpecieId
     */
    @Test(expected = DuplicateSpecieId.class)
    public void testDuplicateSpecieIdDetection() throws DuplicateSpecieId {
        Population p = new Population();
        assertEquals("Wrong initial species count",
                0,
                p.getSpecies().size());

        p.addSpecie("Elephant");
        assertEquals("Wrong initial species count after specie addition",
                1,
                p.getSpecies().size());

        p.addSpecie("Elephant");
        assertEquals("Wrong initial species count after specie addition",
                2,
                p.getSpecies().size());
    }

    /**
     * Test incrementing the individual count of a given specie
     */
    @Test
    public void testIndividualCountIncrement() throws DuplicateSpecieId, UnknownSpecie, NegativeIndividualCount {
        Population p = new Population();

        Specie s = p.addSpecie("Hippopotamus");

        assertEquals("Wrong individual count",
                s.getIndividualCount(),
                0);

        s.setIndividualCount(3);

        assertEquals("Wrong individual count",
                p.getSpecie("Hippopotamus").getIndividualCount(),
                3);

    }

    /**
     * Test whether the unknown specie are properly detected
     */
    @Test(expected = UnknownSpecie.class)
    public void testUnknownSpecieDetection() throws UnknownSpecie {
        Population p = new Population();
        p.getSpecie("Tiger");
    }

    /**
     * Check whether the negative individual count are properly caught
     */
    @Test(expected = NegativeIndividualCount.class)
    public void testNegativeIndividualCountDetection() throws NegativeIndividualCount, DuplicateSpecieId {
        Population p = new Population();
        Specie tigers = p.addSpecie("Tigers");
        tigers.setIndividualCount(-23);
    }

    /**
     * Check whether the negative individual count are properly caught, during
     * the construction of new specie
     */
    @Test(expected = NegativeIndividualCount.class)
    public void testInitialNegativeIndividualCountDetection() throws NegativeIndividualCount, DuplicateSpecieId {
        Population p = new Population();
        Specie tigers = p.addSpecie("Tigers", -23);
    }

    /**
     * Test the calculation of the total number of individual in the population
     */
    @Test
    public void testIndividualCount() {
        Population p = new Population();
        p.addSpecie("Hippopotamus", 34);
        p.addSpecie("Tigers", 56);
        assertEquals(
                "Wrong population individual count",
                90,
                p.getIndividualCount());
    }
}
