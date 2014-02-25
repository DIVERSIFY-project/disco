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

import static junit.framework.TestCase.assertNotSame;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the value population class
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ConcretePopulationTest extends PopulationTest {

    @Override
    public PopulationBuilder getBuilder() {
        return new PopulationBuilder();
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
    public void testAddSpecie() {
        super.testAddSpecie();
        assertSame(getActual(), getInitial());
    }
    
    @Override
    public void testRemoveSpecieByIndex() {
        super.testRemoveSpecieByIndex(); //To change body of generated methods, choose Tools | Templates.
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByName() {
        super.testRemoveSpecieByName(); //To change body of generated methods, choose Tools | Templates.
        assertSame(getActual(), getInitial());
    }
}
