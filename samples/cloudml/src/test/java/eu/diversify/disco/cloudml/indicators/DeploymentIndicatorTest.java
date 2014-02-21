/*
 */
package eu.diversify.disco.cloudml.indicators;

import eu.diversify.disco.cloudml.indicators.robustness.RobustnessCalculator;
import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the general behaviour of indicators
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class DeploymentIndicatorTest extends TestCase {
    
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(expected = IllegalArgumentException.class)
    public void testIndicatorAppliedOfNull() {
        DeploymentIndicator indicator = makeDeploymentIndicator();
        indicator.evaluateOn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRobustnessOfEmptyCloudMLDeployment() {
        DeploymentIndicator indicator = makeDeploymentIndicator();
        indicator.evaluateOn(new DeploymentModel());
    }

    protected abstract DeploymentIndicator makeDeploymentIndicator();
    
}