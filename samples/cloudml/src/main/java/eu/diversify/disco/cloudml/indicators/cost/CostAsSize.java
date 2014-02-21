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
