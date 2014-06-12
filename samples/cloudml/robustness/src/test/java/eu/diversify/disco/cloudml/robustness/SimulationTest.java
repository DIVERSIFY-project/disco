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

import junit.framework.TestCase;

import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.disco.cloudml.robustness.Action.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
@RunWith(JUnit4.class)
public class SimulationTest extends TestCase {

  

    @Test
    public void aRandomSequence() {
        final Population population = new DummyPopulation("x", "y", "z");

        final Simulation simulation = new Simulation(population);
        simulation.reviveAll();
        while (simulation.hasSurvivors()) {
            String victim = simulation.pickRandomVictim();
            simulation.kill(victim);
        }
        Extinction sequence = simulation.getTrace();

        assertThat(sequence.length(), is(equalTo(population.headcount() + 1)));
        assertThat(sequence.survivorCount(), is(equalTo(0)));
        assertThat(sequence.deadCount(), is(equalTo(population.headcount())));
    }
}
