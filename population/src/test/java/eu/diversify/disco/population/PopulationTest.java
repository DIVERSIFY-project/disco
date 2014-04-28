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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.AddSpecie;
import eu.diversify.disco.population.actions.RemoveSpecie;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test operation which update the population
 */
@RunWith(JUnit4.class)
public abstract class PopulationTest extends TestCase {
    public static final double MAX_ROUNDOFF_ERROR = 1e-10;

    @Rule
    public final ExpectedException exception;
    private Population initial;
    private Population actual;
    private Population expected;

    public PopulationTest() {
        exception = ExpectedException.none();
    }

    public abstract PopulationBuilder aSamplePopulation();

    public Population getInitial() {
        return this.initial;
    }

    public Population getActual() {
        return this.actual;
    }

    public Population getExpected() {
        return this.expected;
    }
    
    
    

    // Construction from various data types
    // FIXME: Move to PopulationBuilderTest
    @Test
    public void testCreatingPopulationFromArray() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        verifyDistribution(actual, 3, 2, 0, 1);

    }
    
    private void verifyDistribution(Population sut, int... distribution) {
        assertThat("not null", sut, is(not(nullValue())));
        for(int i=0 ; i<distribution.length ;i++) {
            assertThat("distribution", actual.getDistribution().get(i), is(equalTo(distribution[i])));
        }
    }
    
    @Test
    public void testDeepCopyPreserveConstraint() {
        Population population = aSamplePopulation()
                .withFixedNumberOfSpecies()
                .withDistribution(3, 2, 0, 1)
                .build();
        
        actual = population.deepCopy().build();
        final Action action = new AddSpecie();
        
        assertThat("illegal addition of specie", actual.allows(action), is(false));   
    }

    // Getters test
    @Test
    public void testGetSpecieByIndex() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 0, 1)
                .build();
        
        Specie specie = initial.getSpecie(1);
        
        assertThat("specie name", specie.getName(), is(equalTo("s1")));
        assertThat("specie headcount", specie.getHeadcount(), is(equalTo(3)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSpecieByIndexThatDoesNotExists() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 0, 1)
                .build();
        
        initial.getSpecie(23);
    }
    
    @Test
    public void testGetSpecieByName() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 0, 1)
                .build();
        
        Specie specie = initial.getSpecie("s1");
        
        assertThat("specie name", specie.getName(), is(equalTo("s1")));
        assertThat("specie headcount", specie.getHeadcount(), is(equalTo(3)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSpecieByNameThatDoesNotExists() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 0, 1)
                .build();
        
        initial.getSpecie("foo");
    }
    
    @Test
    public void testGetDistribution() {
        initial = aPopulation()
                .withDistribution(3, 2, 0, 1)
                .build();
        List<Integer> actual = initial.getDistribution();
        List<Integer> expected = Arrays.asList(new Integer[]{3, 2, 0, 1});
        assertEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDistributionIsImmutable() {
        initial = aPopulation()
                .withDistribution(3, 2, 0, 1)
                .build();
        initial.getDistribution().set(3, 2);
    }

    @Test
    public void testGetSpeciesNames() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .build();
        List<String> actual = initial.getSpeciesNames();
        List<String> expected = Arrays.asList(new String[]{"s1", "s2", "s3",
                                                           "s4"});
        assertEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSpeciesNamesIsImmutable() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .build();
        initial.getSpeciesNames().set(2, "xxxx");
    }

    @Test
    public void testGetVarianceWhenMinimum() {
        initial = aPopulation()
                .withDistribution(3, 3, 3, 3, 3)
                .build();
        double variance = initial.getVariance();
        assertEquals(0D, variance);
    }

    @Test
    public void testGetVarianceWhenMaximum() {
        initial = aPopulation()
                .withDistribution(15, 0, 0, 0, 0)
                .build();
        double actual = initial.getVariance();
        double expected = getMaximumVariance(initial);
        assertEquals(expected, actual, 1e-9);
    }

    private double getMaximumVariance(Population population) {
        double s = (double) population.getSpeciesCount();
        double n = (double) population.getTotalHeadcount();
        double mu = population.getMeanHeadcount();
        return (Math.pow(n - mu, 2) + ((s - 1) * (Math.pow(mu, 2)))) / s;
    }

    @Test
    public void testGetVarianceWhenUnknown() {
        initial = aPopulation()
                .withDistribution(15, 6, 2, 1, 9)
                .build();
        double actual = initial.getVariance();
        assertTrue(actual > 0D);
        assertTrue(actual < getMaximumVariance(initial));
    }

    @Test
    public void testGetPercentagePerSpecieOnTheFullSpecie() {
        initial = aSamplePopulation().withDistribution(3, 0, 0).build();
        
        double percentage = initial.getSpecie(1).getFraction();
        
        assertThat("fraction", percentage, is(closeTo(1D, MAX_ROUNDOFF_ERROR)));
    }

    @Test
    public void testGetPercentagePerSpecieOnEmptySpecie() {
        initial = aSamplePopulation().withDistribution(3, 0, 0).build();

        double percentage = initial.getSpecie(2).getFraction();
        
        assertThat("fraction", percentage, is(closeTo(0D, MAX_ROUNDOFF_ERROR)));
    }

    @Test
    public void testGetPercentagePerSpecieOnRegularSpecie() {
        initial = aSamplePopulation().withDistribution(1, 1, 1).build();

        double percentage = initial.getSpecie(2).getFraction();
        
        assertThat("fraction", percentage, is(closeTo(1D / 3D, MAX_ROUNDOFF_ERROR)));
    }

    @Test
    public void testGetMeanNumberOfIndividuals() {
        initial = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(10, 2, 3)
                .build();
        double actual = initial.getMeanHeadcount();
        double expected = 5D;
        assertEquals(expected, actual);
    }

    // Mutator tests
    @Test
    public void testSetNumberOfIndividualsInBySpecieIndex() {
        initial = aSamplePopulation().withDistribution(1, 2, 3).build();
        actual = initial.getSpecie(1).setHeadcount(54);
        expected = aSamplePopulation().withDistribution(54, 2, 3).build();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNumberOfIndividualsInBySpecieName() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(1, 2, 3)
                .build();
        actual = initial.getSpecie("s1").setHeadcount(54);
        expected = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(54, 2, 3)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftNumberOfIndividualsInByIndex() {
        initial = aSamplePopulation().withDistribution(1, 2, 3).build();
        actual = initial.getSpecie(1).shiftHeadcountBy(+1);
        expected = aSamplePopulation().withDistribution(2, 2, 3).build();
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftNumberOfIndividualsInBySpecieName() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1")
                .withDistribution(0)
                .build();
        actual = initial.getSpecie("s1").shiftHeadcountBy(+1);
        expected = aSamplePopulation()
                .withSpeciesNamed("s1")
                .withDistribution(1)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftNumberOfIndividualsByIndexThatDoesNotExists() {
        initial = aSamplePopulation().withDistribution(0).build();
        actual = initial.getSpecie(77).shiftHeadcountBy(+1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftNumberOfIndividualBySpecieNameThatDoesNotExist() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1")
                .withDistribution(0)
                .build();
        actual = initial.getSpecie("doesNotExist").shiftHeadcountBy(+1);
    }

    @Test
    public void testAddSpecieWithoutName() {
        initial = aSamplePopulation()
                .withDistribution(1, 2, 3)
                .build();
        actual = initial.addSpecie(); 
        
        expected = aSamplePopulation()
                .withDistribution(1, 2, 3, 0)
                .build();
        
        assertThat("default name", actual, is(equalTo(expected)));
    }
    
    @Test
    public void testAddSpecie() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.addSpecie("s4");
        expected = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 1, 0)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecieWithNullName() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.addSpecie(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecieWithEmptyName() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.addSpecie("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatedSpecie() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.addSpecie("s3");
    }

    // TODO: check that there are no more occurence of "new PopualtionBuilder()"
    @Test
    public void testRemoveSpecieByIndex() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.removeSpecie(2);
        expected = aSamplePopulation()
                .withSpeciesNamed("s1", "s3")
                .withDistribution(3, 1)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByIndexTooLarge() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .build();
        actual = initial.removeSpecie(77);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByNegativeIndex() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.removeSpecie(-4);
    }

    @Test
    public void testRemoveSpecieByName() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.removeSpecie("s2");
        expected = aSamplePopulation()
                .withSpeciesNamed("s1", "s3")
                .withDistribution(3, 1)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByNameThatDoesNotExist() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        actual = initial.removeSpecie("specie_that_does_not_exist");
    }

    @Test
    public void testRenameSpecieByIndex() {
        final String newName = "sXX";
        initial = aSamplePopulation().withSpeciesNamed("s1", "s2").build();
        actual = initial.getSpecie(2).setName(newName);
        expected = aSamplePopulation().withSpeciesNamed("s1", newName).build();
        assertEquals(actual, expected);
    }

    @Test
    public void testRenameSpecieByName() {
        final String newName = "sXX";
        initial = aSamplePopulation().withSpeciesNamed("s1", "s2").build();
        actual = initial.getSpecie("s2").setName(newName);
        expected = aSamplePopulation().withSpeciesNamed("s1", newName).build();
        assertEquals(actual, expected);
    }

    @Test
    public void testDifferenceWith() {
        Population source = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(1, 2, 3)
                .build();
        Population target = aPopulation()
                .withSpeciesNamed("s1", "s2", "s4")
                .withDistribution(3, 2, 1)
                .build();

        List<Action> actualActions = source.differenceWith(target);
        Action[] expectedActions = new Action[]{
            new ShiftNumberOfIndividualsIn("s1", +2),
            new RemoveSpecie("s3"),
            new AddSpecie("s4"),
            new ShiftNumberOfIndividualsIn("s4", +1)
        };
        assertThat("actions count", actualActions, hasSize(expectedActions.length));
        assertThat("content", actualActions, containsInAnyOrder(expectedActions)); 
    }

    @Test
    public void testSortSpeciesNamesAlphabetically() {
        Population source = aSamplePopulation()
                .withSpeciesNamed("s3", "s2", "s1")
                .withDistribution(1, 2, 3)
                .build();
        List<String> actualNames = source.sortSpeciesNamesAlphabetically();
        List<String> expectedNames = Arrays.asList(new String[]{"s1", "s2", "s3"});

        assertThat("species name order", actualNames, is(equalTo(expectedNames)));
    }
    
    
    @Test
    public void testIsUniformlyDistributed() {
        Population population = aSamplePopulation()
                .withDistribution(5, 5, 5, 5, 5)
                .build();
        
        assertThat("uniformly distributed", population.isUniformlyDistributed());
    }
    
    @Test
    public void testIsNotUniformlyDistributed() {
        Population population = aSamplePopulation()
                .withDistribution(10, 3, 1, 12, 5)
                .build();
        
        assertThat("uniformly distributed", !population.isUniformlyDistributed());
    }

    // Test conversions to other data representation
    @Test
    public void testToString() {
        Population p = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        String actual = p.toString();
        String expected = "[ s1: 3, s2: 2, s3: 1 ]";
        assertEquals(expected, actual);
    }

    @Test
    public void testToArrayOfFraction() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        double[] actual = initial.toArrayOfFractions();
        double[] expected = new double[]{0.5D, 1D / 3, 1D / 6};
        
        assertThat("fractions", actual, is(equalTo(expected)));
    }

    @Test
    public void testToMapWhenEmpty() {
        initial = aSamplePopulation().build();
        Map<String, Integer> expected = new HashMap<String, Integer>();
        assertEquals(expected, initial.toMap());
    }

    @Test
    public void testToMapWhenFilled() {
        initial = aSamplePopulation()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(3, 9)
                .build();
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("s1", 3);
        expected.put("s2", 9);
        assertEquals(expected, initial.toMap());
    }

    // Test populations equality
    @Test
    public void testEqualsWhenEquals() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        expected = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        assertEquals(actual, expected);
    }

    @Test
    public void testEqualsWhenEqualsButSpeciesNamesAreInDifferentOrder() {
        actual = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 0, 1)
                .build();
        expected = aSamplePopulation()
                .withSpeciesNamed("s2", "s1", "s4", "s3")
                .withDistribution(2, 3, 1, 0)
                .build();
        assertEquals(actual, expected);
    }

    @Test
    public void testEqualsWithNull() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        assertFalse(actual.equals(null));
    }

    @Test
    public void testEqualsWithWrongType() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        assertFalse(actual.equals(23));
    }

    @Test
    public void testEqualsWhenIndividualCountAreDifferent() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        expected = aSamplePopulation().withDistribution(3, 3, 0, 1).build();
        assertFalse(actual.equals(expected));
    }

    @Test
    public void testEqualsWhenSpecieNameAreDifferent() {
        actual = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        expected = aSamplePopulation()
                .withSpeciesNamed("sp1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .build();
        assertFalse(actual.equals(expected));
    }

    @Test
    public void testEqualsWhenSpeciesCountAreDifferent() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        expected = aSamplePopulation().withDistribution(3, 2, 0, 1, 5).build();
        assertFalse(actual.equals(expected));
    }

    @Test
    public void testEqualsWhenSame() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        assertTrue(actual.equals(actual));
    }

    @Test
    public void testSameHashCodeImpliesEquals() {
        actual = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        expected = aSamplePopulation().withDistribution(3, 2, 0, 1).build();
        assertEquals(actual.hashCode(), expected.hashCode());
    }
}
