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

package eu.diversify.disco.cloudml.transformations.toPopulation;

import eu.diversify.disco.cloudml.transformations.ToPopulation;
import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test the toPopulation transformation on various examples
 */
@RunWith(Parameterized.class)
public class ToPopulationTest extends TestCase {

    private final ToPopulationExample example;
    private Population actualOutput;
    private final ToPopulation transformation;

    public ToPopulationTest(ToPopulationExample example) { 
        this.example = example;
        this.actualOutput = null;
        this.transformation = new ToPopulation();
    }

    @Test
    public void testForwardWithExample() {
        
         // TODO: Handle expected exceptions
        
        this.actualOutput = transformation.applyTo(this.example.getInput());
        assertEquals(this.example.getExpectedOutput(), this.actualOutput);
    }

    
    @Parameters(name="{0}")
    public static Collection<Object[]> getExamples() {
        final ArrayList<Object[]> examples = new ArrayList<Object[]>();
        
        final ToPopulationExampleCatalog catalog = new ToPopulationExampleCatalog();
        for (String name: catalog.getExampleNames()) {
            examples.add(catalog.get(name).toArray());
        }
        
        return examples;
    }

    
}