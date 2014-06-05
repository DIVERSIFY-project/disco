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
package eu.diversify.disco.cloudml.robustness;

import java.io.File;
import java.io.IOException;
import java.util.*;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.disco.cloudml.robustness.testing.AgreeWith.agreeWith;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of the extinction sequences
 */
@RunWith(JUnit4.class)
public class ExtinctionSequenceTest extends TestCase {

    @Test
    public void fromMapShouldAcceptMultipleSequences() {
        Map<Integer, List<Integer>> survivorCounts = aMultipleExtinctionSequence();

        ExtinctionSequence es = ExtinctionSequence.fromMap(survivorCounts);

        assertThat(es, agreeWith(survivorCounts));
    }

    private Map<Integer, List<Integer>> aMultipleExtinctionSequence() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{3, 3, 3}));
        survivorCounts.put(1, Arrays.asList(new Integer[]{2, 1, 1}));
        survivorCounts.put(2, Arrays.asList(new Integer[]{1, 0, 0}));
        survivorCounts.put(3, Arrays.asList(new Integer[]{0, 0, 0}));
        return survivorCounts;
    }

    @Test
    public void fromMapShouldBuildTheSimplestExtinctionSequence() {
        Map<Integer, List<Integer>> survivorCounts = aMinimalSingleExtinctionSequence();
        ExtinctionSequence es = ExtinctionSequence.fromMap(survivorCounts);

        assertThat(es, agreeWith(survivorCounts));
    }

    private Map<Integer, List<Integer>> aMinimalSingleExtinctionSequence() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{1}));
        survivorCounts.put(1, Arrays.asList(new Integer[]{0}));
        return survivorCounts;
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectDataWithLessThanTwoPoints() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{10}));

        ExtinctionSequence.fromMap(survivorCounts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectNegativeExtinctionLevels() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{10}));
        survivorCounts.put(-3, Arrays.asList(new Integer[]{5}));
        survivorCounts.put(10, Arrays.asList(new Integer[]{0}));

        ExtinctionSequence.fromMap(survivorCounts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectNegativeLifeLevels() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{10}));
        survivorCounts.put(3, Arrays.asList(new Integer[]{-5}));
        survivorCounts.put(10, Arrays.asList(new Integer[]{0}));

        ExtinctionSequence.fromMap(survivorCounts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectMismatchBetweenLifeAndExtinctionLevel() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{10}));
        survivorCounts.put(14, Arrays.asList(new Integer[]{0}));

        ExtinctionSequence.fromMap(survivorCounts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectSerieWhereMinimumLifeLevelIsNotZero() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{10}));
        survivorCounts.put(10, Arrays.asList(new Integer[]{2}));

        ExtinctionSequence.fromMap(survivorCounts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectSerieWhereMinimumExtinctionLevelIsNotZero() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(2, Arrays.asList(new Integer[]{10}));
        survivorCounts.put(10, Arrays.asList(new Integer[]{0}));

        ExtinctionSequence.fromMap(survivorCounts);
    }

    // Should reject non monotonic series of points
    // Should Reject negative evalues
    @Test
    public void shouldProvideRobustnessAsAPercentage() {
        Map<Integer, List<Integer>> survivorCounts = aMinimalSingleExtinctionSequence();
        ExtinctionSequence es = ExtinctionSequence.fromMap(survivorCounts);
        final double robustness = es.getRobustness();

        assertThat(es.toString(), robustness, is(equalTo(100D)));
    }

    @Test
    public void getRobustnessShouldBe100WhenDecayIsLinear() {
        Map<Integer, List<Integer>> survivorCounts = aSingleLinearExtinctionSequence();

        ExtinctionSequence es = ExtinctionSequence.fromMap(survivorCounts);
        final double robustness = es.getRobustness();

        assertThat(es.toString(), robustness, is(both(greaterThanOrEqualTo(0D)).and(lessThanOrEqualTo(100D))));
        assertThat(es.toString(), robustness, is(equalTo(100D)));
    }

    private Map<Integer, List<Integer>> aSingleLinearExtinctionSequence() {
        Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();
        survivorCounts.put(0, Arrays.asList(new Integer[]{10}));
        survivorCounts.put(5, Arrays.asList(new Integer[]{5}));
        survivorCounts.put(10, Arrays.asList(new Integer[]{0}));
        return survivorCounts;
    }

    @Test
    public void toCsvShouldProduceAValidCsvSnippet() {
        Map<Integer, List<Integer>> map = aSingleLinearExtinctionSequence();

        final ExtinctionSequence es = ExtinctionSequence.fromMap(map);
        final String csvSnippet = es.toCsv();

        final String eol = System.lineSeparator();
        final String expectation
                = "killed\\alive,1" + eol
                + "0,10" + eol
                + "5,5" + eol
                + "10,0" + eol;

        assertThat(csvSnippet, is(equalTo(expectation)));

    }

    @Test
    public void fromCsvShouldBuildACorrectExtinctionSequence() {
        final String eol = System.lineSeparator();
        final String csvText
                = "killed\\alive,1" + eol
                + "0,10" + eol
                + "5,5" + eol
                + "10,0" + eol;

        final ExtinctionSequence es = ExtinctionSequence.fromCsv(csvText);

        assertThat(es, agreeWith(aSingleLinearExtinctionSequence()));
    }

    @Test
    public void toCsvFileShouldCreateAFileOnDisk() throws IOException {
        Map<Integer, List<Integer>> map = aSingleLinearExtinctionSequence();

        final ExtinctionSequence es = ExtinctionSequence.fromMap(map);
        final String csvFileName = "test.csv";

        es.toCsvFile(csvFileName);

        final File csvFile = new File(csvFileName);

        assertThat(csvFileName + " should exist", csvFile.exists());

        csvFile.delete();
    }

}
