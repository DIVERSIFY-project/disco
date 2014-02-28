/*
 */
package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.cloudml.transformations.BidirectionalTransformation;
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
public class ToPopulationAcceptanceTest extends TestCase {

    private final ToPopulationExamples example;
    private Population actualOutput;
    private final BidirectionalTransformation transformation;

    
    public ToPopulationAcceptanceTest(ToPopulationExamples example) {
        this.example = example;
        this.actualOutput = null;
        this.transformation = new BidirectionalTransformation();
    }

    @Test
    public void testForwardWithExample() {
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
        examples.add(SENSAPP.toArray());
        
        return examples;
    }

    
}