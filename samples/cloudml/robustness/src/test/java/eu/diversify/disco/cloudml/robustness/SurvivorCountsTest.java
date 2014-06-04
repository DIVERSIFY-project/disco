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