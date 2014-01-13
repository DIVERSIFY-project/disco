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

    private final Evaluation previous;
    private final Population population;
    private final double reference;
    private final double diversity;
    private final double error;

    /**
     * Create a new result object
     *
     * @param population the resulting population
     * @param reference the reference value
     * @param diversity the diversity of the resulting population
     */
    public Evaluation(Population population, double reference, double diversity, double error) {
        this.previous = null;
        this.population = population;
        this.reference = reference;
        this.diversity = diversity;
        this.error = error;
    }

    /**
     * Create a new evaluation object, linked to the evaluation it was a
     * refinement of.
     *
     * @param previous the previous evaluation which led to this new one.
     * @param population the resulting population
     * @param reference the reference value
     * @param diversity the diversity of the resulting population
     */
    public Evaluation(Evaluation previous, Population population, double reference, double diversity, double error) {
        this.previous = previous;
        this.population = population;
        this.reference = reference;
        this.diversity = diversity;
        this.error = error;
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
        return this.reference;
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
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" - Iteration count: ").append(this.getIteration()).append("\n");
        builder.append(" - Population: ").append(population.toString()).append("\n");
        builder.append(" - Reference: ").append(reference).append("\n");
        builder.append(" - Diversity: ").append(diversity).append("\n");
        builder.append(" - Error: ").append(error).append("\n");
        return builder.toString();
    }
}
