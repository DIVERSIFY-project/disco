/*
 */
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;
import static eu.diversify.disco.population.PopulationValue.*;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Check the behaviour of actions to be performed on population value
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class TestAction extends TestCase {

    @Test
    public void testAddIndividualsIn() {
        PopulationValue population = fromDistributionOfIndividuals(new Integer[]{3, 2, 1});
        Action action = new ShiftNumberOfIndividualsIn(2, 20);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{3, 22, 1});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddIndividualsBySpecieName() {
        PopulationValue population = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        Action action = new ShiftNumberOfIndividualsIn("s2", 20);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 22, 1});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIndividuals() {
        PopulationValue population = fromDistributionOfIndividuals(new Integer[]{3, 2, 1});
        Action action = new ShiftNumberOfIndividualsIn(2, -2);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{3, 0, 1});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIndividualsBySpecieName() {
        PopulationValue population = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        Action action = new ShiftNumberOfIndividualsIn("s2", -2);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 0, 1});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddSpecie() {
        PopulationValue population = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        Action action = new AddSpecie("s4");
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3", "s4"},
                new Integer[]{3, 2, 1, 0});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveSpecieByIndex() {
        PopulationValue population = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        Action action = new RemoveSpecie(3);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2"},
                new Integer[]{3, 2});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveSpecieBySpecieName() {
        PopulationValue population = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        Action action = new RemoveSpecie("s3");
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2"},
                new Integer[]{3, 2});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNumberOfIndividualsBySpecieName() {
        PopulationValue population = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 2, 1});
        Action action = new SetNumberOfIndividualsIn("s2", 20);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(
                new String[]{"s1", "s2", "s3"},
                new Integer[]{3, 20, 1});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNumberOfIndividualsByIndex() {
        PopulationValue population = fromDistributionOfIndividuals(new Integer[]{3, 2, 1});
        Action action = new SetNumberOfIndividualsIn(2, 20);
        PopulationValue actual = action.applyTo(population);
        PopulationValue expected = fromDistributionOfIndividuals(new Integer[]{3, 20, 1});
        assertNotSame(population, actual);
        assertEquals(expected, actual);
    }
    
}
