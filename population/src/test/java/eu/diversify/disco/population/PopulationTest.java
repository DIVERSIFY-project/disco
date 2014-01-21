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
        assertTrue("Fresh population shall be empty", p.isEmpty());
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
     * Check whether duplicate specie id are caught on specie creation, without
     * individual count
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
     * Check whether duplicate specie id are caught on specie creation, without
     * individual count
     *
     * @throws DuplicateSpecieId
     */
    @Test(expected = DuplicateSpecieId.class)
    public void testDuplicateSpecieIdDetectionWithCount() throws DuplicateSpecieId {
        Population p = new Population();
        assertEquals("Wrong initial species count",
                0,
                p.getSpecies().size());

        p.addSpecie("Elephant", 34);
        assertEquals("Wrong initial species count after specie addition",
                1,
                p.getSpecies().size());

        p.addSpecie("Elephant", 23);
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
        p.addSpecie("Tigers", -23);
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

    /**
     * Test the species equality
     */
    @Test
    public void testSpecieEquality() {
        Specie s1 = new Specie("lion", 23);
        assertTrue(
                "A specie shall be equal to itself",
                s1.equals(s1));

        Specie s2 = new Specie("lion", 23);
        assertTrue(
                "A specie shall be equal to any other equivalent specie",
                s1.equals(s2));

        Specie s3 = new Specie("lion", 34);
        assertFalse(
                "Individual count must distinguish two species",
                s1.equals(s3));

        Specie s4 = new Specie("sludge", 23);
        assertFalse(
                "Specie name must distinguish two species",
                s1.equals(s4));
    }

    /**
     * Test the equality of two populations
     */
    @Test
    public void testPopulationEquality() {
        Population p1 = new Population();
        p1.addSpecie("lion", 23);
        p1.addSpecie("tiger", 12);
        p1.addSpecie("horse", 17);

        Population p2 = new Population();
        p2.addSpecie("lion", 23);
        p2.addSpecie("tiger", 12);
        p2.addSpecie("horse", 17);

        assertTrue(
                "Two equal populations shall be equal",
                p1.equals(p2));

        assertTrue(
                "Two equal populations shall be equal",
                p2.equals(p1));

        assertTrue(
                "Two equal populations shall be equal",
                p1.equals(p1));


        // Check the detection of a missing specie
        Population p3 = new Population();
        p3.addSpecie("lion", 23);
        p3.addSpecie("horse", 17);

        assertFalse(
                "A missing specie shall make two populations different",
                p1.equals(p3));

        // Check the detection of different individual count
        Population p4 = new Population();
        p4.addSpecie("lion", 25); // Update: +1!
        p4.addSpecie("tiger", 12);
        p4.addSpecie("horse", 17);

        assertFalse(
                "Different individual count shall make two populations different",
                p1.equals(p4));

    }
    
    /**
     * Test the default formatting of a population
     */
    @Test
    public void testPopulationFormatting() {
        Population p = new Population();
        String text = p.toString();
        assertEquals(
                "Empty population shall be properly formatted",
                "{}",
                text
                );
        
        Population p2 = new Population();
        p2.addSpecie("Elephant");
        p2.addSpecie("Lion", 30);
        p2.addSpecie("Hippopotamus", 4);
        String text2 = p2.toString();
        assertEquals(
                "population shall be properly formatted",
                "{Elephant: 0, Lion: 30, Hippopotamus: 4}",
                text2
                );
      
    }
    
}
