

package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.experiments.testing.Tester;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class AcceptanceIT {
    
    @Test
    public void acceptanceTest() throws IOException, InterruptedException {
        new Tester("scalability", "results.csv", "individuals.pdf", "species.pdf").test();
    }

}
