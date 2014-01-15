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

package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import java.util.List;

/**
 * Abstract interface of the diversity controller
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class Controller {

    private final DiversityMetric metric;

    /**
     * Create a new controller based on a given diversity metric
     *
     * @param metric the metric to use to evaluate the populations
     */
    public Controller(DiversityMetric metric) {
        this.metric = metric;
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
    public Evaluation applyTo(Population population, double reference) {
        Case c = new Case(population, reference, metric);
        return search(c.evaluate(population));
    }

    /**
     * Refine the given population until the quality does improve anymore.
     *
     * @param current the evaluation of the current population
     * @return the evaluation resulting from the successive refinements
     */
    protected Evaluation search(Evaluation current) {
        Evaluation next = refine(current);
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
    protected Evaluation refine(Evaluation current) {
        Evaluation output = current;
        for (Update update : getLegalUpdates(current.getPopulation())) {
            Evaluation next = current.refineWith(update);
            if (next.getError() < output.getError()) {
                output = next;
            }
        }
        return output;
    }

    /**
     * Return the list of update operation which are legal on a given population
     *
     * @param population the population whose candidates update are needed
     * @return the list of candidate updates
     */
    protected abstract List<Update> getLegalUpdates(Population population);
}