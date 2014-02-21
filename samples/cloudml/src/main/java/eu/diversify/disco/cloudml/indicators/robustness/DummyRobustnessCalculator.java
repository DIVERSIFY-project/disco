/*
 */

package eu.diversify.disco.cloudml.indicators.robustness;

import org.cloudml.core.DeploymentModel;

/**
 * A dummy robustness calculator for integration purpose
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class DummyRobustnessCalculator extends RobustnessCalculator {

    @Override
    public double doEvaluation(DeploymentModel deploymentModel) {
        return 0.5;
    }

}
