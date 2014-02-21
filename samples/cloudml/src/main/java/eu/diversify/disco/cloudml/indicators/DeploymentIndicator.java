/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Disco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco.  If not, see <http://www.gnu.org/licenses/>.
 */
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
            throw new IllegalArgumentException("Unable to evaluate indicator of a 'null' deployment model.");
        }
        if (isEmpty(deployment)) {
            throw new IllegalArgumentException("Unable to evaluate the indicator of an empty deployment model.");
        }
    }
    
    protected abstract double doEvaluation(DeploymentModel deployment);

    private boolean isEmpty(DeploymentModel deploymentModel) {
        return deploymentModel.getArtefactInstances().isEmpty();
    }
}
