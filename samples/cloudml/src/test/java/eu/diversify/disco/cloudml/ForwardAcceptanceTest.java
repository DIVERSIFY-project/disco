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

/**
 * Test the forward transformation on various examples
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(Parameterized.class)
public class ForwardAcceptanceTest extends TestCase {

    private final String name;
    private final CloudML input;
    private final Population expectedOutput;
    private Population actualOutput;
    private final Transformation transformation;

    
    public ForwardAcceptanceTest(String name, CloudML input, Population expected) {
        this.name = name;
        this.input = input;
        this.expectedOutput = expected;
        this.transformation = new Transformation();
    }

    @Test
    public void testForwardWithExample() {
        this.actualOutput = transformation.forward(this.input);
        assertEquals(this.expectedOutput, this.actualOutput);
    }

    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> getExamples() {
        final CloudMlCatalog cloudmls = new CloudMlCatalog();
        final PopulationCatalog populations = new PopulationCatalog();

        final ArrayList<Object[]> examples = new ArrayList<Object[]>();
        examples.add(createExample(
                "empty CloudML model",
                cloudmls.createEmptyModel(), 
                populations.createEmptyPopulation()));

        return examples;
    }

    private static Object[] createExample(String exampleName, CloudML cloudml, Population population) {
        return new Object[] { exampleName, cloudml, population };
    }
    
}