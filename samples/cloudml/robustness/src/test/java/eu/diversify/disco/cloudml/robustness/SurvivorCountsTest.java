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
package eu.diversify.disco.cloudml.robustness;

import java.util.Collections;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of the distribution of survivor counts
 */
@RunWith(JUnit4.class)
public class SurvivorCountsTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void emptySurvivorCountsShouldBeRejected() {
        new SurvivorCounts(0, Collections.EMPTY_LIST);
    }
    
    @Test
    public void meanShouldReturnTheAverageValue() {
        final int deadCount = 0;
        SurvivorCounts sut = new SurvivorCounts(deadCount, 1, 2, 3);

        assertThat("mean survivor count", sut.mean(), is(closeTo(2D, 1e-6)));
    }

    @Test
    public void minimumShouldReturnTheSmallestValue() {
        final int deadCount = 0;
        SurvivorCounts sut = new SurvivorCounts(deadCount, 1, 2, 3);

        assertThat("min survivor count", sut.minimum(), is(equalTo(1)));
    }

    @Test
    public void maximumhouldReturnTheLargestValue() {
        final int deadCount = 0;
        SurvivorCounts sut = new SurvivorCounts(deadCount, 1, 2, 3);

        assertThat("maximum survivor count", sut.maximum(), is(equalTo(3)));
    }

    @Test
    public void varianceShouldReturnTheVariance() {
        final int deadCount = 0;
        SurvivorCounts sut = new SurvivorCounts(deadCount, 2, 3, 4, 3);

        assertThat("variance survivor count", sut.variance(), is(closeTo(0.5, 1e-6)));
    }

    @Test
    public void standardDeviationShouldReturnTheStandardDeviation() {
        final int deadCount = 0;
        SurvivorCounts sut = new SurvivorCounts(deadCount, 3, 3, 3, 3);

        assertThat("sd survivor count", sut.standardDeviation(), is(closeTo(0D, 1e-6)));
    }
}