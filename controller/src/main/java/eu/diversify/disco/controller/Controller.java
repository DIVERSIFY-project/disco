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
import eu.diversify.disco.population.diversity.DiversityMetric;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract interface of the diversity controller
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Controller {

    private final DiversityMetric metric;

    /**
     * TODO: to remove
     */
    public Controller() {
        this.metric = null;
    }

    /**
     * Create a new controller based on a given diversity metric
     *
     * @param metric the metric to use to evaluate the populations
     */
    public Controller(DiversityMetric metric) {
        this.metric = metric;
    }

    public Population apply(Population population) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Adjust the given population to the selected reference
     *
     * @param population the population to be adjusted
     * @param reference the desired level of diversity in the resulting
     * population
     * @return the updated population wrapped with additional information such
     * as actual diversity, error, and step count.
     */
    public Result adjust(Population population, double reference) {
        Result current = evaluate(0, population, reference);
        Result next = refine(current);
        while (next.getIteration() != current.getIteration()) {
            current = next;
            next = refine(next);
        }
        return next;
    }

    /**
     * Refine the given evaluation, by selecting and applying the most promising
     * update on the given population.
     *
     * @param current the current evaluation
     * @return the new evaluation, after refinement
     */
    private Result refine(Result current) {
        Result output = current;
        for (Update update : getLegalUpdates(current.getPopulation())) {
            Result next = evaluate(
                    current.getIteration() + 1,
                    update.applyTo(current.getPopulation()),
                    current.getReference());
            if (next.getError() < output.getError()) {
                output = next;
            }
        }
        return output;
    }

    /**
     * Evaluate a given population using the metric associated with this
     * controller.
     *
     * @param iteration the iteration count
     * @param population the population evaluated
     * @param reference the reference
     * @return
     */
    public Result evaluate(int iteration, Population population, double reference) {
        final double diversity = this.metric.applyTo(population);
        final double error = Math.pow(reference - diversity, 2);
        return new Result(iteration, population, reference, diversity, error);
    }

    
    
    /**
     * Return the list of update operation which are legal on a given population
     * @param population the population whose candidates update are needed
     * @return the list of candidate updates
     */
    protected List<Update> getLegalUpdates(Population population) {
        return new ArrayList<Update>();
    }
}
