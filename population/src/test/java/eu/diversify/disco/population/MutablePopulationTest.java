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

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static eu.diversify.disco.population.actions.ScriptBuilder.*;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.RemoveSpecie;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import java.util.List;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test the value population class
 *
 * Override all mutators test and check they do modify the initial population
 */
@RunWith(JUnit4.class)
public class MutablePopulationTest extends PopulationTest {

    @Override
    public PopulationBuilder aSamplePopulation() {
        return aPopulation();
    }

    @Override
    public void testShiftNumberOfIndividualsInByIndex() {
        super.testShiftNumberOfIndividualsInByIndex();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testShiftNumberOfIndividualsInBySpecieName() {
        super.testShiftNumberOfIndividualsInBySpecieName();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testSetNumberOfIndividualsInBySpecieIndex() {
        super.testSetNumberOfIndividualsInBySpecieIndex();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testSetNumberOfIndividualsInBySpecieName() {
        super.testSetNumberOfIndividualsInBySpecieName();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRenameSpecieByIndex() {
        super.testRenameSpecieByIndex();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRenameSpecieByName() {
        super.testRenameSpecieByName();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testAddSpecie() {
        super.testAddSpecie();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByIndex() {
        super.testRemoveSpecieByIndex();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByName() {
        super.testRemoveSpecieByName();
        assertSame(getActual(), getInitial());
    }

    @Test
    public void testLegalActions() {
        MutablePopulation population = (MutablePopulation) aPopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(2, 0, 0)
                .build();

        List<Action> actual = population.allLegalActions(1);

        Action[] expected = new Action[]{
            aScript().addSpecie().build(),
            new ShiftNumberOfIndividualsIn("s1", +1),
            new ShiftNumberOfIndividualsIn("s1", -1),
            aScript().shift("s1", -1).shift("s2", +1).build(),
            aScript().shift("s1", -1).shift("s3", +1).build(),
            new RemoveSpecie("s2"),
            new ShiftNumberOfIndividualsIn("s2", +1),
            new RemoveSpecie("s3"),
            new ShiftNumberOfIndividualsIn("s3", +1)
        };
        assertThat("actions count", actual.size(), is(equalTo(expected.length)));
        assertThat("legal actions", actual, containsInAnyOrder(expected));
    }

    @Test
    public void testThatLegalActionsCannotEmptyThePopulation() {
        MutablePopulation population = (MutablePopulation) aPopulation()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(1, 0)
                .build();

        List<Action> actual = population.allLegalActions(1);

        Action[] expected = new Action[]{
            aScript().addSpecie().build(),
            new ShiftNumberOfIndividualsIn("s1", +1),
            aScript().shift("s1", -1).shift("s2", +1).build(),
            new RemoveSpecie("s2"),
            new ShiftNumberOfIndividualsIn("s2", +1),};

        assertThat("legal actions", actual.size(), is(equalTo(expected.length)));
        assertThat("legal actions", actual, containsInAnyOrder(expected));
    }
}
