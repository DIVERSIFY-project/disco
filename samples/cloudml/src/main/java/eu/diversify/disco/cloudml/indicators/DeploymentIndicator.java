/*
 */
package eu.diversify.disco.cloudml.indicators;

import org.cloudml.core.DeploymentModel;

/**
 * General interface for any scalar indicator which can be computed on a CloudML
 * deployment model (e.g., robustness, diversity, or cost)
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class DeploymentIndicator {

    /**
     * Evaluate this indicator on the given deployment model
     *
     * @param deployment the deployment model for which the indicator has to be
     * evaluated
     * @return the indicator has a scalar value
     */
    public final double evaluateOn(DeploymentModel deployment) {
        checkThatDeploymentIsValid(deployment);
        return doEvaluation(deployment);
    }

    private void checkThatDeploymentIsValid(DeploymentModel deployment) {
        if (deployment == null) {
            throw new IllegalArgumentException("Unable to evaluate robustness of a 'null' deployment model.");
        }
        if (isEmpty(deployment)) {
            throw new IllegalArgumentException("Unable to evaluate the robustness of an empty deployment model.");
        }
    }
    
    protected abstract double doEvaluation(DeploymentModel deployment);

    private boolean isEmpty(DeploymentModel deploymentModel) {
        return deploymentModel.getArtefactInstances().isEmpty();
    }
}
