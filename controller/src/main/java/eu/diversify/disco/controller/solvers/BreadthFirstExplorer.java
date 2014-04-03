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
package eu.diversify.disco.controller.solvers;

import eu.diversify.disco.controller.exploration.IndividualPermutationExplorer;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.population.actions.Action;
import java.util.HashSet;

/**
 * Implementation of a breadth-first search strategy.
 *
 * It maintains a set of populations whose neighbours have already by evaluated
 * and a set of populations whose neighbours are unknown, so called the
 * "frontier". At each iteration, we push back the frontier by evaluating all
 * possible neighbours, potentially augmenting the set of explored population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class BreadthFirstExplorer extends IterativeSearch {

    private final HashSet<Solution> explored;
    private final HashSet<Solution> frontier;
    private Solution best = null;

    /**
     * Create a new BreadthFirstExplorer tailored for a given diversity metric
     *
     * @param metric the metric of interest
     */
    public BreadthFirstExplorer() {
        super(new IndividualPermutationExplorer());
        this.explored = new HashSet<Solution>();
        this.frontier = new HashSet<Solution>();
    }

    @Override
    public Solution solve(Problem problem) {
        reset(problem.getInitialEvaluation());
        while (!frontier.isEmpty()) {
            pushback();
        }
        return best;
    }

    /**
     * Expand the frontier.
     *
     * For each candidate evaluation in the frontier, explore the possible
     * neighbours, pushing the frontier back for those that brings a benefit.
     */
    public void pushback() {
        final HashSet<Solution> newFrontier = new HashSet<Solution>();
        for (Solution from : frontier) {
            for (Action action : getActionFinder().search(from, 1)) {
                Solution next = from.refineWith(action);
                if (next.isBetterThan(from)&& neverExplored(next)) {
                    updateBestSolutionIfNeeded(next);
                    newFrontier.add(next);
                }
            }
            this.explored.add(from);
        }
        this.frontier.clear();
        this.frontier.addAll(newFrontier);
    }

    private boolean neverExplored(Solution solution) {
        return !this.explored.contains(solution)
                && !this.frontier.contains(solution);
    }

    private void updateBestSolutionIfNeeded(Solution next) {
        if (next.getError() < best.getError()) {
            this.best = next;
        }
    }

    /**
     * Reset the explorer with an empty set of explored populations, a frontier
     * the given seed as best evaluation found so far and as unique frontier to
     * explore.
     *
     * @param seed the evaluation to start from
     */
    void reset(Solution seed) {
        this.best = seed;
        this.explored.clear();
        this.frontier.clear();
        frontier.add(best);
    }

    /**
     * @return the set of population evaluated so far
     */
    HashSet<Solution> getExplored() {
        return this.explored;
    }

    /**
     * @return the frontier populations, whose neighbours have not yet been
     * evaluated
     */
    HashSet<Solution> getFrontier() {
        return this.frontier;
    }
}
