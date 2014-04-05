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
package eu.diversify.disco.controller.solvers.searches;

import eu.diversify.disco.controller.exploration.ExplorationStrategy;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.Solution;

public abstract class SearchStrategy {

    private final ExplorationStrategy finder;
    private Solution current;
    protected Solution next;

    public SearchStrategy(ExplorationStrategy finder) {
        this.finder = finder;
    }

    public void setUp(Problem problem) {
        this.current = problem.getInitialEvaluation();
        this.next = current;
        findImprovement();
    }

    public ExplorationStrategy getFinder() {
        return this.finder;
    }

    public final Solution getCurrentSolution() {
        return current;
    }

    public abstract boolean hasMoreImprovement();

    public final Solution improve() {
        if (current == null) {
            throw new IllegalStateException("No problem have been setup yet.");
        }
        current = next;
        findImprovement();
        return current;
    }

    protected abstract void findImprovement();
}
