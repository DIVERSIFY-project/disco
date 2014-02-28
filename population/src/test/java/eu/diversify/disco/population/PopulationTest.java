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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test operation which update the population
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class PopulationTest extends TestCase {

    @Rule
    public final ExpectedException exception;
    private Population initial;
    private Population actual;
    private Population expected;

    public PopulationTest() {
        exception = ExpectedException.none();
    }

    public abstract PopulationBuilder getBuilder();

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
    @Test
    public void testCreatingPopulationFromArray() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        assertFalse(actual.isEmpty());
        assertEquals(4, actual.getNumberOfSpecies());
        assertEquals(3, actual.getNumberOfIndividualsIn(1));
        assertEquals(2, actual.getNumberOfIndividualsIn(2));
        assertEquals(0, actual.getNumberOfIndividualsIn(3));
        assertEquals(1, actual.getNumberOfIndividualsIn(4));
    }

    // Getters test
    @Test
    public void testGetDistribution() {
        initial = new PopulationBuilder()
                .withDistribution(3, 2, 0, 1)
                .make();
        List<Integer> actual = initial.getDistribution();
        List<Integer> expected = Arrays.asList(new Integer[]{3, 2, 0, 1});
        assertEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDistributionIsImmutable() {
        initial = new PopulationBuilder()
                .withDistribution(3, 2, 0, 1)
                .make();
        initial.getDistribution().set(3, 2);
    }

    @Test
    public void testGetSpeciesNames() {
        initial = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .make();
        List<String> actual = initial.getSpeciesNames();
        List<String> expected = Arrays.asList(new String[]{"s1", "s2", "s3",
                                                           "s4"});
        assertEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSpeciesNamesIsImmutable() {
        initial = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .make();
        initial.getSpeciesNames().set(2, "xxxx");
    }

    @Test
    public void testGetVarianceWhenMinimum() {
        initial = new PopulationBuilder()
                .withDistribution(3, 3, 3, 3, 3)
                .make();
        double variance = initial.getVariance();
        assertEquals(0D, variance);
    }

    @Test
    public void testGetVarianceWhenMaximum() {
        initial = new PopulationBuilder()
                .withDistribution(15, 0, 0, 0, 0)
                .make();
        double actual = initial.getVariance();
        double expected = getMaximumVariance(initial);
        assertEquals(expected, actual, 1e-9);
    }

    private double getMaximumVariance(Population population) {
        double s = (double) population.getNumberOfSpecies();
        double n = (double) population.getTotalNumberOfIndividuals();
        double mu = population.getMeanNumberOfIndividuals();
        return (Math.pow(n - mu, 2) + ((s - 1) * (Math.pow(mu, 2)))) / s;
    }

    @Test
    public void testGetVarianceWhenUnknown() {
        initial = new PopulationBuilder()
                .withDistribution(15, 6, 2, 1, 9)
                .make();
        double actual = initial.getVariance();
        assertTrue(actual > 0D);
        assertTrue(actual < getMaximumVariance(initial));
    }

    @Test
    public void testGetPercentagePerSpecieOnTheFullSpecie() {
        initial = getBuilder().withDistribution(3, 0, 0).make();
        expected = getBuilder().clonedFrom(initial).make();
        double percentage = initial.getFractionIn(1);
        assertEquals(initial, expected);
        assertEquals(1D, percentage, 1e-9);
    }

    @Test
    public void testGetPercentagePerSpecieOnEmptySpecie() {
        initial = getBuilder().withDistribution(3, 0, 0).make();
        expected = getBuilder().clonedFrom(initial).make();
        double percentage = initial.getFractionIn(2);
        assertEquals(initial, expected);
        assertEquals(0D, percentage, 1e-9);
    }

    @Test
    public void testGetPercentagePerSpecieOnRegularSpecie() {
        initial = getBuilder().withDistribution(1, 1, 1).make();
        expected = getBuilder().clonedFrom(initial).make();
        double percentage = initial.getFractionIn(2);
        assertEquals(initial, expected);
        assertEquals(1D / 3D, percentage, 1e-9);
    }

    @Test
    public void testGetPercentagePerSpecieOnRegularSpecieBySpecieName() {
        initial = getBuilder().withDistribution(1, 1, 1).make();
        expected = getBuilder().clonedFrom(initial).make();
        double percentage = initial.getFractionIn("sp. #2");
        assertEquals(initial, expected);
        assertEquals(1D / 3D, percentage, 1e-9);
    }

    @Test
    public void testGetMeanNumberOfIndividuals() {
        initial = new PopulationBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(10, 2, 3)
                .make();
        double actual = initial.getMeanNumberOfIndividuals();
        double expected = 5D;
        assertEquals(expected, actual);
    }

    // Mutator tests
    @Test
    public void testSetNumberOfIndividualsInBySpecieIndex() {
        initial = getBuilder().withDistribution(1, 2, 3).make();
        actual = initial.setNumberOfIndividualsIn(1, 54);
        expected = getBuilder().withDistribution(54, 2, 3).make();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNumberOfIndividualsInBySpecieName() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(1, 2, 3)
                .make();
        actual = initial.setNumberOfIndividualsIn("s1", 54);
        expected = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(54, 2, 3)
                .make();
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftNumberOfIndividualsInByIndex() {
        initial = getBuilder().withDistribution(1, 2, 3).make();
        actual = initial.shiftNumberOfIndividualsIn(1, +1);
        expected = getBuilder().withDistribution(2, 2, 3).make();
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftNumberOfIndividualsInBySpecieName() {
        initial = getBuilder()
                .withSpeciesNamed("s1")
                .withDistribution(0)
                .make();
        actual = initial.shiftNumberOfIndividualsIn("s1", 1);
        expected = getBuilder()
                .withSpeciesNamed("s1")
                .withDistribution(1)
                .make();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftNumberOfIndividualsByIndexThatDoesNotExists() {
        initial = getBuilder().withDistribution(0).make();
        actual = initial.shiftNumberOfIndividualsIn(77, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftNumberOfIndividualBySpecieNameThatDoesNotExist() {
        initial = getBuilder()
                .withSpeciesNamed("s1")
                .withDistribution(0)
                .make();
        actual = initial.shiftNumberOfIndividualsIn("s877", 1);
    }

    @Test
    public void testAddSpecie() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.addSpecie("s4");
        expected = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 1, 0)
                .make();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecieWithNullName() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.addSpecie(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecieWithEmptyName() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.addSpecie("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatedSpecie() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.addSpecie("s3");
    }

    // TODO: check that there are no more occurence of "new PopualtionBuilder()"
    @Test
    public void testRemoveSpecieByIndex() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.removeSpecie(2);
        expected = getBuilder()
                .withSpeciesNamed("s1", "s3")
                .withDistribution(3, 1)
                .make();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByIndexTooLarge() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .make();
        actual = initial.removeSpecie(77);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByNegativeIndex() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.removeSpecie(-4);
    }

    @Test
    public void testRemoveSpecieByName() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.removeSpecie("s2");
        expected = getBuilder()
                .withSpeciesNamed("s1", "s3")
                .withDistribution(3, 1)
                .make();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecieByNameThatDoesNotExist() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        actual = initial.removeSpecie("s77");
    }

    @Test
    public void testRenameSpecieByIndex() {
        initial = getBuilder().withSpeciesNamed("s1", "s2").make();
        actual = initial.renameSpecie(2, "sXX");
        expected = getBuilder().withSpeciesNamed("s1", "sXX").make();
        assertEquals(actual, expected);
    }

    @Test
    public void testRenameSpecieByName() {
        initial = getBuilder().withSpeciesNamed("s1", "s2").make();
        actual = initial.renameSpecie("s2", "sXX");
        expected = getBuilder().withSpeciesNamed("s1", "sXX").make();
        assertEquals(actual, expected);
    }

    // Test conversions to other data representation
    @Test
    public void testToString() {
        Population p = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        String actual = p.toString();
        String expected = "[ s1: 3, s2: 2, s3: 1 ]";
        assertEquals(expected, actual);
    }

    @Test
    public void testToArrayOfFraction() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        double[] actual = initial.toArrayOfFractions();
        double[] expected = new double[]{0.5D, 1D / 3, 1D / 6};
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @Test
    public void testToMapWhenEmpty() {
        initial = getBuilder().make();
        Map<String, Integer> expected = new HashMap<String, Integer>();
        assertEquals(expected, initial.toMap());
    }
    
    @Test
    public void testToMapWhenFilled() {
        initial = getBuilder()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(3, 9)
                .make();
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("s1", 3);
        expected.put("s2", 9);
        assertEquals(expected, initial.toMap());
    }

    // Test populations equality
    @Test
    public void testEqualsWhenEquals() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        expected = getBuilder().withDistribution(3, 2, 0, 1).make();
        assertEquals(actual, expected);
    }

    @Test
    public void testEqualsWhenEqualsButSpeciesNamesAreInDifferentOrder() {
        actual = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 2, 0, 1)
                .make();
        expected = getBuilder()
                .withSpeciesNamed("s2", "s1", "s4", "s3")
                .withDistribution(2, 3, 1, 0)
                .make();
        assertEquals(actual, expected);
    }

    @Test
    public void testEqualsWithNull() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        assertFalse(actual.equals(null));
    }

    @Test
    public void testEqualsWithWrongType() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        assertFalse(actual.equals(23));
    }

    @Test
    public void testEqualsWhenIndividualCountAreDifferent() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        expected = getBuilder().withDistribution(3, 3, 0, 1).make();
        assertFalse(actual.equals(expected));
    }

    @Test
    public void testEqualsWhenSpecieNameAreDifferent() {
        actual = getBuilder()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        expected = getBuilder()
                .withSpeciesNamed("sp1", "s2", "s3")
                .withDistribution(3, 2, 1)
                .make();
        assertFalse(actual.equals(expected));
    }

    @Test
    public void testEqualsWhenSpeciesCountAreDifferent() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        expected = getBuilder().withDistribution(3, 2, 0, 1, 5).make();
        assertFalse(actual.equals(expected));
    }

    @Test
    public void testEqualsWhenSame() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        assertTrue(actual.equals(actual));
    }

    @Test
    public void testSameHashCodeImpliesEquals() {
        actual = getBuilder().withDistribution(3, 2, 0, 1).make();
        expected = getBuilder().withDistribution(3, 2, 0, 1).make();
        assertEquals(actual.hashCode(), expected.hashCode());
    }
}
