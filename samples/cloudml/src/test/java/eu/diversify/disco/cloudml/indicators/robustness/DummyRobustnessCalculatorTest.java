/*
 */

package eu.diversify.disco.cloudml.indicators.robustness;

import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.robustness.DummyRobustnessCalculator;
import eu.diversify.disco.cloudml.indicators.robustness.RobustnessCalculator;


/**
 * Test the dummy robustness calculator
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class DummyRobustnessCalculatorTest extends RobustnessCalculatorTest {

    @Override
    protected RobustnessCalculator makeRobustnessCalculator() {
        return new DummyRobustnessCalculator();
    }

   
}
