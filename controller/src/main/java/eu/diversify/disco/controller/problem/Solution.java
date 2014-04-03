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
package eu.diversify.disco.controller.problem;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.actions.Action;

/**
 * Encapsulate the various data outputted by the controller, including the
 * number of iterations, the final error and the resulting population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Solution {

    private final Problem problem;
    private final Population population;
    private final double diversity;
    private final double error; 
    
    /**
     * Create a new result object
     *
     * @param problem the case against which the population was evaluated
     * @param population the resulting population
     * @param reference the reference value
     * @param diversity the diversity of the resulting population
     */
    public Solution(Problem problem, Population population, double diversity, double error) {
        this.problem = problem;
        this.population = population;
        this.diversity = diversity;
        this.error = error;
    }

    public Problem getProblem() {
        return this.problem;
    }

    /**
     * Check whether the selected action applies in the context of the problem
     * under resolution
     *
     * @param action the action to be evaluated
     * @return true if the action is legal
     */
    public boolean canBeRefinedWith(Action action) {
        return problem.isLegal(action);
    }

    /**
     * Refine this evaluation by applying the given update on the population
     *
     * @param update the update to apply to refine this evaluation
     * @return the evaluation (a newly created object) of the population
     * resulting from applying the given update on the population.
     */
    public Solution refineWith(Action action) {
        Solution evaluation = problem.evaluate(action.applyTo(population));
        return evaluation;
    }

    public boolean isBetterThan(Solution other) {
        return this.error < other.error;
    }

    /**
     * @return the resulting population
     */
    public Population getPopulation() {
        return this.population;
    }

    /**
     * @return the reference diversity level used to reach this result
     */
    public double getReference() {
        return this.problem.getReference();
    }

    /**
     * @return the diversity level of the resulting population
     */
    public double getDiversity() {
        return this.diversity;
    }

    /**
     * @return the error level of the resulting population
     */
    public double getError() {
        return this.error;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Solution) {
            final Solution evaluation = (Solution) o;
            result = evaluation.problem.equals(problem)
                    && evaluation.population.equals(population);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.problem != null ? this.problem.hashCode() : 0);
        hash = 53 * hash + (this.population != null ? this.population.hashCode() : 0);
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.diversity) ^ (Double.doubleToLongBits(this.diversity) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" - Population: ").append(population.toString()).append("\n");
        builder.append(" - Reference: ").append(this.getReference()).append("\n");
        builder.append(" - Diversity: ").append(diversity).append("\n");
        builder.append(" - Error: ").append(error).append("\n");
        return builder.toString();
    }
}
