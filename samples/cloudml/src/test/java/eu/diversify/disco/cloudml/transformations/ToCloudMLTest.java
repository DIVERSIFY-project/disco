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
package eu.diversify.disco.cloudml.transformations;

import static eu.diversify.disco.cloudml.transformations.ToCloudMLExample.*;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Test the behaviour of the conversion from a CloudML model and a population
 * level, back into a CloudML level;
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(Parameterized.class)
public class ToCloudMLTest extends TestCase {

    private final ToCloudMLExample example;
    private final ToCloudML transformation;
    private DeploymentModel actual;

    public ToCloudMLTest(ToCloudMLExample example) {
        this.example = example;
        this.transformation = new ToCloudML();
        this.actual = null; // To be set during the test
    }

    @Test
    public void testToCloudMLWith() {
        try {
            actual = transformation.applyTo(example.getInputDeployment(),
                                            example.getInputPopulation());
            if (example.shallRaiseAnException()) {
                String message = String.format("Should have raised '%s",
                                               example.getExpectedException().getName());
                fail(message);
            }
            // FIXME: equals is not enough! Shall use model comparator
            assertEquals(example.getInputDeployment(), actual);

        } catch (Exception e) {
            if (example.shallRaise(e.getClass())) {
                assertTrue(true);

            }
            else {
                String message = String.format("Expected '%s' but '%s' caught!",
                                               example.getExpectedException().getName(),
                                               e.getClass().getName());
                fail(message);
            }
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getExamples() {
        final ArrayList<Object[]> examples = new ArrayList<Object[]>();

        examples.add(WITH_NULL_DEPLOYMENT.toArray());
        examples.add(WITH_NULL_POPULATION.toArray());
        
        return examples;
    }
}