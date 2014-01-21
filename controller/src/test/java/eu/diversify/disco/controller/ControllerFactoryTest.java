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
package eu.diversify.disco.controller;

import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.controller.exceptions.UnknownStrategyException;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the controller factory
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ControllerFactoryTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test the
     */
    @Test
    public void testUsageScenario() throws ControllerInstantiationException {
        final ControllerFactory factory = new ControllerFactory();

        final Controller ahc = factory.instantiate("Adaptive Hill Climbing");
        assertEquals(
                "Wrong controller instantiation",
                AdaptiveHillClimber.class,
                ahc.getClass());


        final Controller bfs = factory.instantiate("Breadth-First Search");
        assertEquals(
                "Wrong controller instantiation",
                BreadthFirstExplorer.class,
                bfs.getClass());

        final Controller hc = factory.instantiate("Hill Climbing");
        assertEquals(
                "Wrong controller instantiation",
                HillClimber.class,
                hc.getClass());


        // Check senstivity to extra spaces and case
        final Controller hc2 = factory.instantiate(" Hill   CLIMBING ");
        assertEquals(
                "Wrong controller instantiation",
                HillClimber.class,
                hc2.getClass());

    }

    @Test(expected = UnknownStrategyException.class)
    public void testUnknownStrategyDetection() throws ControllerInstantiationException {
        final ControllerFactory factory = new ControllerFactory();

        final Controller hc = factory.instantiate(" Hill   CLIMBING 2 ");
        assertEquals(
                "Wrong controller instantiation",
                HillClimber.class,
                hc.getClass());
    }
}
