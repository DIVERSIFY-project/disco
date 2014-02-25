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
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import java.util.Arrays;
import junit.framework.TestCase;
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
        Population population = new PopulationBuilder().withDistribution(3, 2, 1).make();
        Action action = new ShiftNumberOfIndividualsIn(2, 20);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder().withDistribution(3, 22, 1).make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddIndividualsBySpecieName() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        Action action = new ShiftNumberOfIndividualsIn("s2", 20);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 22, 1)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIndividuals() {
        Population population = new PopulationBuilder()
                .withDistribution(3, 2, 1)
                .make();
        Action action = new ShiftNumberOfIndividualsIn(2, -2);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withDistribution(3, 0, 1)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIndividualsBySpecieName() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        Action action = new ShiftNumberOfIndividualsIn("s2", -2);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 0, 1)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddSpecie() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        Action action = new AddSpecie("s4");
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 1, 0)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveSpecieByIndex() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        Action action = new RemoveSpecie(3);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(3, 2)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveSpecieBySpecieName() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        Action action = new RemoveSpecie("s3");
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(3, 2)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNumberOfIndividualsBySpecieName() {
        Population population = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        Action action = new SetNumberOfIndividualsIn("s2", 20);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 20, 1)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNumberOfIndividualsByIndex() {
        Population population = new PopulationBuilder()
                .withDistribution(3, 2, 1)
                .make();
        Action action = new SetNumberOfIndividualsIn(2, 20);
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withDistribution(3, 20, 1)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testScript() {
        Population population = new PopulationBuilder()
                .withDistribution(3, 2, 1)
                .make();
        Action action = new Script(Arrays.asList(new Action[]{
            new ShiftNumberOfIndividualsIn(1, -1),
            new ShiftNumberOfIndividualsIn(2, +1)
        }));
        Population actual = action.applyTo(population);
        Population expected = new PopulationBuilder()
                .withDistribution(2, 3, 1)
                .make();
        assertSame(population, actual);
        assertEquals(expected, actual);
    }
}
