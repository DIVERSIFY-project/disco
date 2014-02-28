/*
 */
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static eu.diversify.disco.cloudml.ForwardExample.*;

/**
 * Test the forward transformation on various examples
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(Parameterized.class)
public class ForwardAcceptanceTest extends TestCase {

    private final ForwardExample example;
    private Population actualOutput;
    private final Transformation transformation;

    
    public ForwardAcceptanceTest(ForwardExample example) {
        this.example = example;
        this.actualOutput = null;
        this.transformation = new Transformation();
    }

    @Test
    public void testForwardWithExample() {
        this.actualOutput = transformation.forward(this.example.getInput());
        assertEquals(this.example.getExpectedOutput(), this.actualOutput);
    }

    
    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> getExamples() {
        final ArrayList<Object[]> examples = new ArrayList<Object[]>();
     
        examples.add(EMPTY_MODEL.toArray());
        examples.add(ONE_VM_TYPE_BUT_NO_INSTANCE.toArray());
        examples.add(ONE_VM_TYPE_AND_ITS_INSTANCE.toArray()); 
        examples.add(MANY_VM_TYPES_BUT_NO_INSTANCE.toArray());
        
        return examples;
    }

    
}