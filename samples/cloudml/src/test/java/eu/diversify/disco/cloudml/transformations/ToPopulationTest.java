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
package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static eu.diversify.disco.cloudml.transformations.ToPopulationExamples.*;

/**
 * Test the toPopulation transformation on various examples
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(Parameterized.class)
public class ToPopulationTest extends TestCase {

    private final ToPopulationExamples example;
    private Population actualOutput;
    private final BidirectionalTransformation transformation;

    
    public ToPopulationTest(ToPopulationExamples example) {
        this.example = example;
        this.actualOutput = null;
        this.transformation = new BidirectionalTransformation();
    }

    @Test
    public void testForwardWithExample() {
        
         // TODO: Handle expected exceptions
        
        this.actualOutput = transformation.toPopulation(this.example.getInput());
        assertEquals(this.example.getExpectedOutput(), this.actualOutput);
    }

    
    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> getExamples() {
        final ArrayList<Object[]> examples = new ArrayList<Object[]>();
     
        // Dummy cases
        examples.add(EMPTY_MODEL.toArray());
        examples.add(ONE_VM_TYPE_BUT_NO_INSTANCE.toArray());
        examples.add(ONE_VM_TYPE_AND_ITS_INSTANCE.toArray()); 
        examples.add(MANY_VM_TYPES_BUT_NO_INSTANCE.toArray());
        examples.add(MANY_VM_TYPES_WITH_TWO_INSTANCES.toArray());
        examples.add(ONE_ARTEFACT_TYPE_BUT_NO_INSTANCE.toArray());
        examples.add(ONE_VM_TYPE_ONE_ARTEFACT_TYPE_BUT_NO_INSTANCE.toArray());
        examples.add(ONE_VM_TYPE_ONE_ARTEFACT_TYPE_AND_THEIR_TWO_INSTANCES.toArray());
        examples.add(TWO_VM_TYPES_TWO_ARTEFACT_TYPES_AND_THE_FOUR_RELATED_INSTANCES.toArray());
        
        // Real cases
        examples.add(HUIS_FAKE_MODEL.toArray());
        examples.add(SENSAPP.toArray());
        examples.add(MDMS.toArray());
        
        return examples;
    }

    
}