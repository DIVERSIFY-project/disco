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

import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.actions.Action;

public class HillClimbing extends SearchStrategy {

    public static final int DEFAULT_SCALE_FACTOR = 1;
    
    
    @Override
    public boolean hasMoreImprovement() {
        return next.isStrictlyBetterThan(getCurrentSolution());
    }

    @Override
    protected void findImprovement() {
        Solution best = getCurrentSolution();
        for (Action action: getCurrentSolution().allLegalActions(getScaleFactor())) {
            Solution candidate = getCurrentSolution().refineWith(action);
            best = candidate.orIfStrictlyBetter(best);
        }
        this.next = best;
    }

    protected int getScaleFactor() {
        return DEFAULT_SCALE_FACTOR;
    }
}
