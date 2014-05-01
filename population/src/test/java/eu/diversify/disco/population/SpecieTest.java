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

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static eu.diversify.disco.population.PopulationBuilder.*;

@RunWith(JUnit4.class)
public class SpecieTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void constructorShoudlRejectNullAsName() {
        new Specie(new MutablePopulation(), null, 23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectEmptyStringAsName() {
        new Specie(new MutablePopulation(), "", 23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNegativeHeadcount() {
        new Specie(new MutablePopulation(), "Lions", -23);
    }

    @Test
    public void shiftHeadcountShouldUpdateTheHeadcount() {
        final int initialHeadcount = 25;
        final int offset = 5;

        Specie specie = defaultSpecie();
        specie.setHeadcount(initialHeadcount);

        specie.shiftHeadcountBy(offset);

        assertThat("", specie.getHeadcount(), is(equalTo(initialHeadcount + offset)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shiftHeadcountShouldRejectOffsetThatWouldResultInANegativeHeadcount() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(10);

        specie.shiftHeadcountBy(-20);
    }

    @Test
    public void setHeadcountShouldUpdateTheHeadcount() {
        Specie specie = defaultSpecie();

        specie.setHeadcount(25);

        assertThat("", specie.getHeadcount(), is(equalTo(25)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeadcountShouldRejectNegativeHeadcount() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(-23);
    }

    @Test
    public void setNameShouldUpdateTheName() {
        Specie specie = defaultSpecie();

        specie.setName("Hippopotamus");

        assertThat("specie name", specie.getName(), is(equalTo("Hippopotamus")));
    }

    @Test
    public void isNamedShouldReturnTrueIfTheGivenNameMatchesTheActualName() {
        Specie specie = defaultSpecie();
        specie.setName("Tigers");

        assertThat("Is named tigers", specie.isNamed("Tigers"));
    }

    @Test
    public void isNamedShouldReturnFalseWhenTheGivenNameDoesNotMatchTheActualName() {
        Specie specie = defaultSpecie();

        assertThat("Is named tigers", !specie.isNamed("Tigers"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameShouldRejectNull() {
        Specie specie = defaultSpecie();
        specie.setName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setNameShouldRejectEmptyString() {
        Specie specie = defaultSpecie();
        specie.setName("");
    }

    @Test
    public void equalsShouldReturnTrueWhenBothNameAndHeadcountAreEquals() {
        final int headcount = 23;
        final String name = "foo";
     
        final Specie s1 = defaultSpecie();
        s1.setHeadcount(headcount);
        s1.setName(name);
        
        final Specie s2 = defaultSpecie();
        s2.setHeadcount(headcount);
        s2.setName(name);

        assertThat("Equals species", s1.equals(s2));
    }

    @Test
    public void equalsShouldReturnTrueWhenApplyToItself() {
        Specie s1 = defaultSpecie();

        assertThat("not equals", s1, is(equalTo(s1)));
    }

    @Test
    public void equalsShouldDetectNameThatAreDifferent() {
        Specie s1 = defaultSpecie();
        Specie s2 = defaultSpecie();
        s2.setName("Tigers");

        assertThat("not equals", s1, is(not(equalTo(s2))));
    }

    @Test
    public void equalsShouldDetectHeadcountThatAreDifferent() {
        Specie s1 = defaultSpecie();
        Specie s2 = defaultSpecie();
        s2.setHeadcount(0);

        assertThat("not equals", s1, is(not(equalTo(s2))));
    }

    @Test
    public void equalsShouldReturnFalseWhenAppliedOnNull() {
        Specie s1 = defaultSpecie();
        Specie s2 = null;

        assertThat("not equals", s1, is(not(equalTo(s2))));
    }

    @Test
    public void isEmptyShouldReturnFalseWhenHeadCountIsGreaterThanZero() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(23);

        assertThat("empty", !specie.isEmpty());
    }

    @Test
    public void isEmptyShouldReturnTrueWhenHeadcountIsZero() {
        Specie specie = defaultSpecie();
        specie.setHeadcount(0);

        assertThat("Not empty", specie.isEmpty());
    }

    private Specie defaultSpecie() {
       Population population = aPopulation().withDistribution(1, 2, 3, 4).build();
       return population.getSpecie(1);
    }
}