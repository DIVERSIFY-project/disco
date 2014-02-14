package eu.diversify.disco.population.random;

import eu.diversify.disco.population.Population;
import java.util.Iterator;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the generator of random populations
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class GeneratorTest extends TestCase {

    @Test
    public void testUsage() {
        Generator generator = new Generator();
        
        Profile profile = new Profile();
        profile.setSpeciesCount(5, 50);
        profile.setIndividualsCount(500, 10000);

        Iterator<Population> populations = generator.many(100, profile);
        int counter = 0;
        while (populations.hasNext()) {
            counter++;
            Population population = populations.next();
            assertTrue("The population does not fit the givenprofile", profile.matches(population));
        }
        assertEquals("Wrong number of populations generated", 100, counter);
    }
}
