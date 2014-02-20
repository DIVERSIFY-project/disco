package eu.diversify.disco.population;

import static eu.diversify.disco.population.PopulationValue.*;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the value population class
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class PopulationValueTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testEmptyPopulation() {
        PopulationValue population = PopulationValue.emptyPopulation();
        assertTrue(population.isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingIndividualCounts() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3", "s4"},
                new Integer[]{3, 2, 0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSpecieName() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 0, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySpecieName() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "", "s3", "s4"},
                new Integer[]{3, 2, 0, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSpecieName() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", null, "s3", "s4"},
                new Integer[]{3, 2, 0, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeIndividualCount() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3", "s4"},
                new Integer[]{3, -2, 0, 1});
    }

    @Test
    public void testAccess() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3", "s4"},
                new Integer[]{3, 2, 0, 1});
        assertFalse(p.isEmpty());
        assertEquals(6, p.getTotalNumberOfIndividuals());
        assertEquals(4, p.getNumberOfSpecies());
        assertEquals(3, p.getNumberOfIndividualsIn(1));
        assertEquals(3, p.getNumberOfIndividualsIn("s1"));
        assertEquals(2, p.getNumberOfIndividualsIn(2));
        assertEquals(2, p.getNumberOfIndividualsIn("s2"));
        assertEquals(0, p.getNumberOfIndividualsIn(3));
        assertEquals(0, p.getNumberOfIndividualsIn("s3"));
        assertEquals(1, p.getNumberOfIndividualsIn(4));
        assertEquals(1, p.getNumberOfIndividualsIn("s4"));
    }

    @Test
    public void testEqualsWhenEquals() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        PopulationValue p2 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testEqualsWithNull() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertFalse(p1.equals(null));
    }

    @Test
    public void testEqualsWithWrongType() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertFalse(p1.equals(23));
    }

    @Test
    public void testEqualsWhenIndividualCountAreDifferent() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        PopulationValue p2 = fromDistributionOfIndividuals(new Integer[]{3, 3, 0, 1});
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsWhenSpecieNameAreDifferent() {
        PopulationValue p1 = fromDistributionOfIndividuals("sp no.", new Integer[]{3, 2, 0, 1});
        PopulationValue p2 = fromDistributionOfIndividuals("sp noooo.", new Integer[]{3, 2, 0, 1});
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsWhenSpeciesCountAreDifferent() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        PopulationValue p2 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1, 5});
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsWhenSame() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertTrue(p1.equals(p1));
    }

    @Test
    public void testSameHashCodeImpliesEquals() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        PopulationValue p2 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testCreatingPopulationFromArray() {
        PopulationValue p = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertFalse(p.isEmpty());
        assertEquals(4, p.getNumberOfSpecies());
        assertEquals(3, p.getNumberOfIndividualsIn(1));
        assertEquals(2, p.getNumberOfIndividualsIn(2));
        assertEquals(0, p.getNumberOfIndividualsIn(3));
        assertEquals(1, p.getNumberOfIndividualsIn(4));
    }

    @Test
    public void testAddIndividualByIndex() {
        PopulationValue p = fromDistributionOfIndividuals(new Integer[]{0});
        PopulationValue actual = p.shiftNumberOfIndividualsIn(1, 1);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{1});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIndividualByIndexThatDoesNotExists() {
        PopulationValue p = fromDistributionOfIndividuals(new Integer[]{0});
        PopulationValue actual = p.shiftNumberOfIndividualsIn(77, 1);
    }

    @Test
    public void testAddIndividualBySpecieName() {
        PopulationValue p = fromDistributionOfIndividuals(new String[]{"s1"}, new Integer[]{0});
        PopulationValue actual = p.shiftNumberOfIndividualsIn("s1", 1);
        PopulationValue expected = fromDistributionOfIndividuals(new String[]{"s1"}, new Integer[]{1});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIndividualBySpecieNameThatDoesNotExist() {
        PopulationValue p = fromDistributionOfIndividuals(new String[]{"s1"}, new Integer[]{0});
        PopulationValue actual = p.shiftNumberOfIndividualsIn("s877", 1);
    }

    @Test
    public void testRemovalOfIndividualsBySpecieIndex() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 1});
        PopulationValue actual = p1.shiftNumberOfIndividualsIn(1, -1);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{2, 2, 1});
        assertNotSame("Population value shall be immutable", p1, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovalOfIndividualsInEmptySpecie() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 0, 1});
        PopulationValue actual = p1.shiftNumberOfIndividualsIn(2, -3);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{3, 0, 1});
        assertNotSame("Population value shall be immutable", p1, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddSpecie() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.addSpecie("s4");
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3", "s4"},
                new Integer[]{3, 2, 1, 0});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecieWithNullName() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.addSpecie(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecieWithEmptyName() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.addSpecie("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatedSpecie() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.addSpecie("s3");
    }

    @Test
    public void testRemoveSpecieByIndex() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.removeSpecie(2);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s3"},
                new Integer[]{3, 1});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByIndexTooLarge() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.removeSpecie(77);
    }

    @Test
    public void testRemoveSpecieByName() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.removeSpecie("s2");
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s3"},
                new Integer[]{3, 1});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByNameThatDoesNotExist() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        PopulationValue actual = p.removeSpecie("s77");
    }

    @Test
    public void testToString() {
        PopulationValue p = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        String actual = p.toString();
        String expected = "[ s1: 3, s2: 2, s3: 1 ]";
        assertEquals(expected, actual);
    }
}
