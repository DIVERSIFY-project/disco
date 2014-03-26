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

package eu.diversify.disco.controller.solvers;

import eu.diversify.disco.controller.exploration.ExplorationStrategy;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.population.actions.Action;

/**
 * Abstract interface of the diversity controller
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class IterativeSearch implements Solver {
    public static final int DEFAULT_SCALE_FACTOR = 1;
   
    private final ExplorationStrategy finder;

    
    /**
     * Create a new Iterative Search, applying a given exploration strategy
     * @param finder the exploration strategy
     */
    public IterativeSearch(ExplorationStrategy finder) {
        this.finder = finder;
    }
    
    /**
     * @return the exploration strategy
     */
    protected ExplorationStrategy getActionFinder() {
        return this.finder;
    }
    
    /**
     * @see eu.diversity.disco.controller.Solver#solve
     */
    @Override
    public Solution solve(Problem problem) {
        return search(problem.getInitialEvaluation());
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
        for (Action action : finder.search(current, getScaleFactor())) {
            Solution next = current.refineWith(action);
            if (next.getError() < output.getError()) {
                output = next;
            }
        }
        return output;
    }
    
    protected int getScaleFactor() {
        return DEFAULT_SCALE_FACTOR;
    }

}
