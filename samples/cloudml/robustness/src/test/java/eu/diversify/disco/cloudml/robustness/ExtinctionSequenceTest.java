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
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the extinction sequences
 */
@RunWith(JUnit4.class)
public class ExtinctionSequenceTest extends TestCase {

    @Test
    public void fromMapShouldBuildTheSimplestExtinctionSequence() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 1);
        map.put(1, 0);

        ExtinctionSequence es = ExtinctionSequence.fromMap(map);

        assertThat(es.getUpperBound(), is(equalTo(1)));
        assertThat(es.getAliveCount(0), is(equalTo(1)));
        assertThat(es.getAliveCount(1), is(equalTo(0)));
    }
    
    @Test
    public void fromMapShouldBuildACorrectExtinctionSequence() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(10, 0);

        ExtinctionSequence es = ExtinctionSequence.fromMap(map);

        assertThat(es.getUpperBound(), is(equalTo(10)));
        assertThat(es.getAliveCount(0), is(equalTo(10)));
        assertThat(es.getAliveCount(10), is(equalTo(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectDataWithLessThanTwoPoints() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);

        ExtinctionSequence.fromMap(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectNegativeExtinctionLevels() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(-3, 5);
        map.put(10, 0);

        ExtinctionSequence.fromMap(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectNegativeLifeLevels() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(3, -5);
        map.put(10, 0);

        ExtinctionSequence.fromMap(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectMismatchBetweenLifeAndExtinctionLevel() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(14, 0);

        ExtinctionSequence.fromMap(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectSerieWhereMinimumLifeLevelIsNotZero() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(10, 2);

        ExtinctionSequence.fromMap(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromMapShouldRejectSerieWhereMinimumExtinctionLevelIsNotZero() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(2, 10);
        map.put(10, 0);

        ExtinctionSequence.fromMap(map);
    }

    // Should reject non monotonic series of points
    // Should Reject negative evalues
    @Test
    public void shouldProvideRobustnessAsAPercentage() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(10, 0);

        ExtinctionSequence es = ExtinctionSequence.fromMap(map);
        final double robustness = es.getRobustness();

        assertThat(es.toString(), robustness, is(equalTo(100D)));
    }

    @Test
    public void getRobustnessShouldBe75WhenDecayIsLinear() {
        Map<Integer, Integer> map = aSampleExtinctionSequenceDataSet();

        ExtinctionSequence es = ExtinctionSequence.fromMap(map);
        final double robustness = es.getRobustness();

        assertThat(es.toString(), robustness, is(both(greaterThanOrEqualTo(0D)).and(lessThanOrEqualTo(100D))));
        assertThat(es.toString(), robustness, is(equalTo(75D)));
    }

    @Test
    public void toCsvShouldProduceAValidCsvSnippet() {
        Map<Integer, Integer> map = aSampleExtinctionSequenceDataSet();

        final ExtinctionSequence es = ExtinctionSequence.fromMap(map);
        final String csvSnippet = es.toCsv();

        final String eol = System.lineSeparator();
        final String expectation
                = "killed\\alive, 1" + eol
                + "0, 10" + eol
                + "5, 5" + eol
                + "10, 0" + eol;

        assertThat(csvSnippet, is(equalTo(expectation)));

    }

    @Test
    public void fromCsvShouldBuildACorrectExtinctionSequence() {
        final String eol = System.lineSeparator();
        final String csvText
                = "killed\\alive, 1" + eol
                + "0, 10" + eol
                + "5, 5" + eol
                + "10, 0" + eol;

        final ExtinctionSequence es = ExtinctionSequence.fromCsv(csvText);

        assertThat(es.getAliveCount(0), is(equalTo(10)));
        assertThat(es.getAliveCount(5), is(equalTo(5)));
        assertThat(es.getAliveCount(10), is(equalTo(0)));
    }

    @Test
    public void toCsvFileShouldCreateAFileOnDisk() throws IOException {
        Map<Integer, Integer> map = aSampleExtinctionSequenceDataSet();

        final ExtinctionSequence es = ExtinctionSequence.fromMap(map);
        final String csvFileName = "test.csv";
        
        es.toCsvFile(csvFileName);
        
        final File csvFile = new File(csvFileName);
 
        assertThat(csvFileName + " should exist", csvFile.exists());
        
        csvFile.delete();
    }

    private Map<Integer, Integer> aSampleExtinctionSequenceDataSet() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 10);
        map.put(5, 5);
        map.put(10, 0);
        return map;
    }

}
