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
public abstract class IterativeSearch implements Controller {
    
    
    /**
     * @see eu.diversity.disco.controller.Controller#applyTo
     */
    public Solution applyTo(Problem problem) {
        return search(problem.getInitialEvaluation());
    }

    /**
     * @see eu.diversity.disco.controller.Controller#applyTo
     */
    public final Solution applyTo(DiversityMetric metric, Population population, double reference) {
        return applyTo(new Problem(population, reference, metric));
    }

    /**
     * Refine the given population until the quality does improve anymore.
     *
     * @param current the evaluation of the current population
     * @return the evaluation resulting from the successive refinements
     */
    protected Solution search(Solution current) {
        Solution next = refine(current);
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
    protected Solution refine(Solution current) {
        Solution output = current;
        for (Update update : getLegalUpdates(current.getPopulation())) {
            Solution next = current.refineWith(update);
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
