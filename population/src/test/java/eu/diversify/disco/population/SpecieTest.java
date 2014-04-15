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
package eu.diversify.disco.population;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class SpecieTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void rejectNullAsNameInConstructor() {
        new Specie(null, 23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectEmptyStringAsNameInConstructor() {
        new Specie("", 23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectNegativeHeadcountInConstructor() {
        new Specie("Lions", -23);
    }

    @Test
    public void testShiftHeadcountBy() {
        final int initialHeadcount = 25;
        final int offset = 5;

        Specie specie = defaultSpecie();
        specie.setHeadcount(initialHeadcount);

        specie.shiftHeadcountBy(offset);

        assertThat("", specie.getHeadcount(), is(equalTo(initialHeadcount + offset)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftHeadcountBelowZero() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(10);

        specie.shiftHeadcountBy(-20);
    }

    @Test
    public void testSetHeadcount() {
        Specie specie = defaultSpecie();

        specie.setHeadcount(25);

        assertThat("", specie.getHeadcount(), is(equalTo(25)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHeadCountWithNegativeValue() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(-23);
    }

    @Test
    public void testSetName() {
        Specie specie = defaultSpecie();

        specie.setName("Hippopotamus");

        assertThat("specie name", specie.getName(), is(equalTo("Hippopotamus")));
    }

    @Test
    public void testIsNamed() {
        Specie specie = defaultSpecie();
        specie.setName("Tigers");

        assertThat("Is named tigers", specie.isNamed("Tigers"));
    }

    @Test
    public void testIsNamedWhenNotNamed() {
        Specie specie = defaultSpecie();

        assertThat("Is named tigers", !specie.isNamed("Tigers"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWithNull() {
        Specie specie = defaultSpecie();
        specie.setName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWithEmptyString() {
        Specie specie = defaultSpecie();
        specie.setName("");
    }

    @Test
    public void testEqualsWhenEquals() {
        Specie s1 = defaultSpecie();
        Specie s2 = defaultSpecie();

        assertThat("Equals species", s1.equals(s2));
    }

    @Test
    public void testEqualsItself() {
        Specie s1 = defaultSpecie();

        assertThat("not equals", s1, is(equalTo(s1)));
    }

    @Test
    public void testNotEqualsWhenNamesAreDifferent() {
        Specie s1 = defaultSpecie();
        Specie s2 = defaultSpecie();
        s2.setName("Tigers");

        assertThat("not equals", s1, is(not(equalTo(s2))));
    }

    @Test
    public void testNotEqualsWhenHeadcountsAreDifferent() {
        Specie s1 = defaultSpecie();
        Specie s2 = defaultSpecie();
        s2.setHeadcount(0);

        assertThat("not equals", s1, is(not(equalTo(s2))));
    }

    @Test
    public void testNotEqualsNull() {
        Specie s1 = defaultSpecie();
        Specie s2 = null;

        assertThat("not equals", s1, is(not(equalTo(s2))));
    }

    @Test
    public void testIsEmptyWhenFull() {
        Specie specie = defaultSpecie();

        assertThat("Not empty", !specie.isEmpty());
    }

    @Test
    public void testIsEmptyWhemEmpty() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(0);

        assertThat("Not empty", specie.isEmpty());
    }

    private Specie defaultSpecie() {
        return new Specie("Lions", 25);
    }
}