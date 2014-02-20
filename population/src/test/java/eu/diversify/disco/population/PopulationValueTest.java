package eu.diversify.disco.population;

import static eu.diversify.disco.population.PopulationValue.*;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertTrue;
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
    public void testEqualsWhenEquals() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        PopulationValue p2 = fromDistributionOfIndividuals(new Integer[]{3, 2, 0, 1});
        assertTrue(p1.equals(p2));
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
        PopulationValue actual = p.addIndividualIn(1);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{1});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIndividualByIndexThatDoesNotExists() {
        PopulationValue p = fromDistributionOfIndividuals(new Integer[]{0});
        PopulationValue actual = p.addIndividualIn(77);
    }

    @Test
    public void testAddIndividualBySpecieName() {
        PopulationValue p = fromDistributionOfIndividuals(new String[]{"s1"}, new Integer[]{0});
        PopulationValue actual = p.addIndividualIn("s1");
        PopulationValue expected = fromDistributionOfIndividuals(new String[]{"s1"}, new Integer[]{1});
        assertNotSame(p, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIndividualBySpecieNameThatDoesNotExist() {
        PopulationValue p = fromDistributionOfIndividuals(new String[]{"s1"}, new Integer[]{0});
        PopulationValue actual = p.addIndividualIn("s877");
    }

    @Test
    public void testRemovalOfIndividualsBySpecieIndex() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 1});
        PopulationValue actual = p1.removeIndividualFrom(1);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{2, 2, 1});
        assertNotSame("Population value shall be immutable", p1, actual);
        assertEquals(expected, actual);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemovalOfIndividualsBySpecieIndexThatDoesNotExist() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 2, 1});
        PopulationValue actual = p1.removeIndividualFrom(77);
    }

    @Test
    public void testRemovalOfIndividualsBySpecieName() {
        PopulationValue p1 = fromDistributionOfIndividuals(new String[]{"s1", "s2", "s3"}, new Integer[]{3, 2, 1});
        PopulationValue actual = p1.removeIndividualFrom("s1");
        PopulationValue expected = fromDistributionOfIndividuals(new String[]{"s1", "s2", "s3"}, new Integer[]{2, 2, 1});
        assertNotSame("Population value shall be immutable", p1, actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovalOfIndividualsBySpecieNameThatDoesNotExist() {
        PopulationValue p1 = fromDistributionOfIndividuals(new String[]{"s1", "s2", "s3"}, new Integer[]{3, 2, 1});
        PopulationValue actual = p1.removeIndividualFrom("xxx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovalOfIndividualsInEmptySpecie() {
        PopulationValue p1 = fromDistributionOfIndividuals(new Integer[]{3, 0, 1});
        PopulationValue actual = p1.removeIndividualFrom(2);
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
}
