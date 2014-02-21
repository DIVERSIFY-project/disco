package eu.diversify.disco.cloudml.indicators.robustness;

import eu.diversify.disco.cloudml.indicators.robustness.RobustnessCalculator;
import eu.diversify.disco.cloudml.CloudML;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicatorTest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.CloudMLElement;
import org.cloudml.core.DeploymentModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Generic test suite for robustness calculations
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class RobustnessCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testOrderingRobustness() throws FileNotFoundException, IOException {
        RobustnessCalculator robustness = makeRobustnessCalculator();
        DeploymentModel fragileDeployment = makeFragileDeployment();
        double r1 = robustness.evaluateOn(fragileDeployment);
        DeploymentModel robustDeployment = makeRobustDeployment();
        double r2 = robustness.evaluateOn(robustDeployment);
        assertTrue(
                "Robust model shall have greater or equal robustness than fragile ones",
                r1 <= r2);
    }

    protected abstract RobustnessCalculator makeRobustnessCalculator();

    private DeploymentModel makeFragileDeployment() throws FileNotFoundException, IOException {
        JsonCodec codec = new JsonCodec();
        InputStream stream = new FileInputStream("../src/test/resources/sensappAdmin.json");
        DeploymentModel model = (DeploymentModel) codec.load(stream);
        stream.close();
        return model;
    }

    private DeploymentModel makeRobustDeployment() throws FileNotFoundException, IOException {
        return makeFragileDeployment();
    }

    @Override
    protected DeploymentIndicator makeDeploymentIndicator() {
        return new DummyRobustnessCalculator();
    }
}