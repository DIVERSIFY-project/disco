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

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import static eu.diversify.disco.cloudml.robustness.Action.*;

/**
 * Specification of
 */
@RunWith(JUnit4.class)
public class SequenceGroupTest extends TestCase {

    private static final String EOL = System.lineSeparator();

    @Test
    public void groupShouldBeProperlyConvertedToCsv() {

        final Population population = new DummyPopulation("x", "y", "z");
        SequenceGroup sequences = population.emptyGroup();
        
        final Simulator simulator = new Simulator(population);
        
        sequences.add(simulator.run(reviveAll(), kill("x"), kill("y"), kill("z")));
        sequences.add(simulator.run(reviveAll(), kill("z"), kill("y"), kill("x")));

        final String expectedCsv
                = "dead count, action 1, survivor count 1, action 2, survivor count 2" + EOL
                + "0, revive all, 3, revive all, 3" + EOL
                + "1, kill 'x', 2, kill 'z', 2" + EOL
                + "2, kill 'y', 1, kill 'y', 1" + EOL
                + "3, kill 'z', 0, kill 'x', 0" + EOL;

        assertThat(sequences.toCsv(), is(equalTo(expectedCsv)));
    }

}
