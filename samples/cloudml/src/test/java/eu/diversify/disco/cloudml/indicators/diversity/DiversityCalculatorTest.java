/*
 */
package eu.diversify.disco.cloudml.indicators.diversity;

import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicatorTest;
import eu.diversify.disco.cloudml.indicators.robustness.RobustnessCalculator;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertTrue;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class DiversityCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testOrderingRobustness() throws FileNotFoundException, IOException {
        DiversityCalculator robustness = makeDiversityCalculator();
        DeploymentModel fragileDeployment = makeHomogeneousDeployment();
        double r1 = robustness.evaluateOn(fragileDeployment);
        DeploymentModel robustDeployment = makeDiverseDeployment();
        double r2 = robustness.evaluateOn(robustDeployment);
        assertTrue(
                "Robust model shall have greater or equal robustness than fragile ones",
                r1 <= r2);
    }

    protected DiversityCalculator makeDiversityCalculator() {
        return new DiversityCalculator(new TrueDiversity());
    }

    private DeploymentModel makeHomogeneousDeployment() throws FileNotFoundException, IOException {
        JsonCodec codec = new JsonCodec();
        InputStream stream = new FileInputStream("../src/test/resources/sensappAdmin.json");
        DeploymentModel model = (DeploymentModel) codec.load(stream);
        stream.close();
        return model;
    }

    private DeploymentModel makeDiverseDeployment() throws FileNotFoundException, IOException {
        return makeHomogeneousDeployment();
    }

    @Override
    protected DeploymentIndicator makeDeploymentIndicator() {
        return makeDiversityCalculator();
    }

}