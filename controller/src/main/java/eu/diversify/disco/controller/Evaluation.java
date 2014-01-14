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
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;

/**
 * Encapsulate the various data outputted by the controller, including the
 * number of iterations, the final error and the resulting population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Evaluation {

    private final Case problem;
    private Evaluation previous;
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
    public Evaluation(Case problem, Population population, double diversity, double error) {
        this.problem = problem;
        this.previous = null;
        this.population = population;
        this.diversity = diversity;
        this.error = error;
    }

    /**
     * Refine this evaluation by applying the given update on the population
     *
     * @param update the update to apply to refine this evaluation
     * @return the evaluation (a newly created object) of the population
     * resulting from applying the given update on the population.
     */
    public Evaluation refineWith(Update u) {
        Evaluation evaluation = problem.evaluate(u.applyTo(population));
        evaluation.previous = this;
        return evaluation;
    }

    /**
     * Check whether this evaluation was refined from another one.
     *
     * @return true if this evaluation is a refinement
     */
    public boolean hasPrevious() {
        return this.previous != null;
    }

    /**
     * @return the previous evaluation which was refined to obtained this one.
     */
    public Evaluation getPrevious() {
        return this.previous;
    }

    /**
     * @return the current iteration count
     */
    public int getIteration() {
        int count = 1;
        if (hasPrevious()) {
            count += this.getPrevious().getIteration();
        }
        return count;
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
        if (o instanceof Evaluation) {
            final Evaluation evaluation = (Evaluation) o;
            result = evaluation.problem.equals(problem)
                    && evaluation.population.equals(population);
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" - Iteration count: ").append(this.getIteration()).append("\n");
        builder.append(" - Population: ").append(population.toString()).append("\n");
        builder.append(" - Reference: ").append(this.getReference()).append("\n");
        builder.append(" - Diversity: ").append(diversity).append("\n");
        builder.append(" - Error: ").append(error).append("\n");
        return builder.toString();
    }
}
