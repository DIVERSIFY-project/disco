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
/**
 *
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

import org.junit.Test;
import static eu.diversify.disco.population.PopulationBuilder.*;
import static junit.framework.TestCase.assertNotSame;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Shall override the all mutators test and check for immutability
 */
@RunWith(JUnit4.class)
public class ImmutablePopulationTest extends PopulationTest {

    @Override
    public void testShiftNumberOfIndividualsInByIndex() {
        super.testShiftNumberOfIndividualsInByIndex();
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testShiftNumberOfIndividualsInBySpecieName() {
        super.testShiftNumberOfIndividualsInBySpecieName();
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testSetNumberOfIndividualsInBySpecieIndex() {
        super.testSetNumberOfIndividualsInBySpecieIndex();
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testSetNumberOfIndividualsInBySpecieName() {
        super.testSetNumberOfIndividualsInBySpecieName();
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testAddSpecie() {
        super.testAddSpecie();
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testAddSpecieWithoutName() {
        super.testAddSpecie();
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByIndex() {
        super.testRemoveSpecieByIndex(); //To change body of generated methods, choose Tools | Templates.
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByName() {
        super.testRemoveSpecieByName(); //To change body of generated methods, choose Tools | Templates.
        assertNotSame(getActual(), getInitial());
    }

    @Override
    public PopulationBuilder aSamplePopulation() {
        return aPopulation().immutable();
    }

    @Test
    public void testCloning() {
        Population sut = aPopulation().withDistribution(1, 2, 3, 4).build();
        Population clone = sut.deepCopy().immutable().build();

        Population updated = clone.getSpecie(3).setHeadcount(0);
        
        Population updatedTwice = updated.getSpecie(3).setHeadcount(1);

        assertThat("immutable cloned not changed", clone.getSpecie(3).getHeadcount(), is(equalTo(3)));
        assertThat("new population changed", updated.getSpecie(3).getHeadcount(), is(equalTo(0)));
        assertThat("new population changed", updatedTwice.getSpecie(3).getHeadcount(), is(equalTo(1)));

    }
}
