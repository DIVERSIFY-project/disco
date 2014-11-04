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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.diversify.disco.cloudml.indicators.cost;

import org.cloudml.core.Deployment;

/**
 * Calculate the cost as a linear function of the size of the deployment model,
 * i.e., its number of instances;
 */
public class CostAsSize extends CostCalculator {

    private static final double DEFAULT_COST_PER_INSTANCE = 2D;
    private static final double DEFAULT_COST_PER_RELATIONSHIP = 1D;
    private static final double DEFAULT_COST_PER_EXECUTE_ON = 1D;

    private final double costPerInstances;
    private final double costPerRelationship;
    private final double costPerExecuteOn;

    /**
     * Create a cost calculation using default costs for components,
     * relationships and 'executeOn'.
     */
    public CostAsSize() {
        this(DEFAULT_COST_PER_INSTANCE,
             DEFAULT_COST_PER_RELATIONSHIP,
             DEFAULT_COST_PER_EXECUTE_ON);
    }

    /**
     * Create a specialised cost calculator, where the cost per component,
     * relationship and executeOn are given.
     *
     * @param costPerInstance the cost of one single component instance
     * @param costPerRelationship the cost of one single relationship instance
     * @param costOfExecuteOn the cost of one single 'execute on' relationship
     */
    public CostAsSize(double costPerInstance, double costPerRelationship, double costOfExecuteOn) {
        this.costPerInstances = rejectIfNegative(costPerInstance, "component instance");
        this.costPerRelationship = rejectIfNegative(costPerRelationship, "relationship instance");
        this.costPerExecuteOn = rejectIfNegative(costOfExecuteOn, "excecute on");
    }

    /**
     * Abort the execution if the given cost value is negative. 
     */
    private double rejectIfNegative(double cost, String roleName) {
        if (cost < 0D) {
            final String errorMessage
                    = String.format("Expecting positive cost value for %s but found %.3f", roleName, cost);
            throw new IllegalArgumentException(errorMessage);
        }
        return cost;
    }

    @Override
    public double doEvaluation(Deployment deployment) {
        return deployment.getComponentInstances().size() * costPerInstances
                + deployment.getRelationshipInstances().size() * costPerRelationship
                + deployment.getExecuteInstances().size() * costPerExecuteOn;
    }

    /**
     * @return the cost of a single component instance
     */
    public double getCostPerInstances() {
        return costPerInstances;
    }

    /**
     * @return the cost of a single relationship instance
     */
    public double getCostPerRelationship() {
        return costPerRelationship;
    }

    /**
     * @return the cost of a single 'execute on' relationship
     */
    public double getCostPerExecuteOn() {
        return costPerExecuteOn;
    }

}
