/*
 */

package eu.diversify.disco.cloudml.indicators.cost;

import org.cloudml.core.DeploymentModel;

/**
 * Calculate the cost as a linear function of the size of the deployment model,
 * i.e., its number of instances;
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class CostAsSize extends  CostCalculator {
    public static final double WEIGHT_OF_INSTANCES = 2D;
    public static final double WEIGHT_OF_BINDINGS = 1D;

    @Override
    public double doEvaluation(DeploymentModel deployment) {
        return deployment.getArtefactInstances().size() * WEIGHT_OF_INSTANCES 
                + deployment.getBindingInstances().size() * WEIGHT_OF_BINDINGS;
    }

}
