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

import java.io.FileNotFoundException;

import static eu.diversify.disco.cloudml.robustness.Action.*;

import junit.framework.TestCase;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SensApp;

import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
@RunWith(JUnit4.class)
public class SimulatorTest extends TestCase {

    @Test
    public void aPredifinedSequence() {
        final Population population = new DummyPopulation("x", "y", "z");

        final Simulator simulator = new Simulator(population);
        final Extinction sequence = simulator.run(reviveAll(), kill("x"), kill("y"), kill("z"));

        assertThat(sequence.length(), is(equalTo(population.headcount() + 1)));
        assertThat(sequence.survivorCount(), is(equalTo(0)));
        assertThat(sequence.deadCount(), is(equalTo(population.headcount())));
    }

    @Test
    public void impactOfAction() {
        final Population population = new DummyPopulation("x", "y", "z");

        final Simulator simulator = new Simulator(population);
        final Extinction sequence = simulator.run(reviveAll(), kill("x"), kill("y"), kill("z"));

        assertThat(sequence.impactOf(kill("x")), is(equalTo(1)));
    }

    @Test
    public void robustnessShouldBeAvailableForEachExtinction() {
        final Population population = new DummyPopulation("x", "y", "z");

        final Simulator simulator = new Simulator(population);
        final Extinction sequence = simulator.run(reviveAll(), kill("x"), kill("y"), kill("z"));

        assertThat(sequence.robustness(), is(equalTo(100D)));
    }

    @Test
    public void groupShouldContainTheRightNumberOfSequence() {
        final Population population = new DummyPopulation("x", "y", "z");
        final Simulator simulator = new Simulator(population);
        final SequenceGroup group = simulator.randomExtinctions(10);

        assertThat(group.size(), is(equalTo(10)));
    }

    @Test
    public void groupShouldProvideRobustnessDistribution() {
        final Population population = new DummyPopulation("x", "y", "z");
        final Simulator simulator = new Simulator(population);
        final SequenceGroup group = simulator.randomExtinctions(10);
        Distribution robustness = group.robustness();

        assertThat(group.summary(), robustness.mean(), is(closeTo(100D, 1e-6)));
    }

    @Test
    public void groupShouldProvideARankingOfIndividuals() {
        final Population population = new DummyPopulation("x", "y", "z");
        final Simulator simulator = new Simulator(population);
        final SequenceGroup group = simulator.randomExtinctions(10);

        assertThat(group.summary(), group.ranking().size(), is(equalTo(population.headcount())));
    }
    

    


}
