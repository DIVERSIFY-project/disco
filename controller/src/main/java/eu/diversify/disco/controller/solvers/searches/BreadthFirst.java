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
package eu.diversify.disco.controller.solvers.searches;

import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.actions.Action;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BreadthFirst extends SearchStrategy {
    private static final String STRATEGY_NAME = "breadth-first search";

    private final HashSet<Solution> explored;
    private final HashSet<Solution> frontier;

    public BreadthFirst() {
        this.frontier = new HashSet<Solution>();
        this.explored = new HashSet<Solution>();
    }

    @Override
    public String getName() {
        return STRATEGY_NAME;
    }
    
    

    @Override
    public void setUp(Problem problem) {
        this.explored.clear();
        this.frontier.clear();
        this.frontier.add(problem.getInitialEvaluation());
        super.setUp(problem); 
    }

    public Set<Solution> getExplored() {
        return Collections.unmodifiableSet(explored);
    }

    public Set<Solution> getFrontier() {
        return Collections.unmodifiableSet(frontier);
    }

    @Override
    public boolean hasMoreImprovement() {
        return !frontier.isEmpty();
    }

    @Override
    protected void findImprovement() {
        final HashSet<Solution> newFrontier = new HashSet<Solution>();
        for (Solution from : frontier) {
            for (Action action : from.allLegalActions(1)) {
                Solution candidate = from.refineWith(action);
                if (neverExplored(candidate) && candidate.isStrictlyBetterThan(from)) {
                    next = candidate.orIfStrictlyBetter(next);
                    newFrontier.add(candidate);
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
    
}
