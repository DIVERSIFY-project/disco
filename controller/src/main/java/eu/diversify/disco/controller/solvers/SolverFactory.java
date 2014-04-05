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
import eu.diversify.disco.controller.solvers.searches.AdaptiveHillClimbing;
import eu.diversify.disco.controller.solvers.searches.BreadthFirst;
import eu.diversify.disco.controller.solvers.searches.HillClimbing;
import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import java.util.HashMap;

/**
 * Create controller based on their name
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class SolverFactory {

    private final HashMap<String, Solver> strategies;

    /**
     * Create a new SolverFactory
     */
    public SolverFactory() {
        this.strategies = new HashMap<String, Solver>();
        this.strategies.put("HILL CLIMBING", createHillClimbing());
        this.strategies.put("ADAPTIVE HILL CLIMBING", createAdaptiveHillClimber());
        this.strategies.put("BREADTH-FIRST SEARCH", createBreadthFirst());
    }

    /**
     * Instantiate a controller based on the name if the given strategy can be
     * match against the existing one.
     *
     * @param strategy the name of the strategy to instantiate
     *
     * @return a solver object with the needed strategy strategy
     */
    public Solver instantiate(String strategy) {
        final String escaped = strategy.trim().replaceAll("\\s+", " ").toUpperCase();
        final Solver solver = this.strategies.get(escaped);
        if (solver == null) {
            final String message = String.format("Unknown resolution strategy: '%s'", strategy);
            throw new IllegalArgumentException(message);
        }
        else {
            return solver;
        }
    }

    private Solver createHillClimbing() {
        return new IterativeSearch(new HillClimbing(new IndividualPermutationExplorer()));
    }

    private Solver createAdaptiveHillClimber() {
        return new IterativeSearch(new AdaptiveHillClimbing(new IndividualPermutationExplorer()));
    }

    private Solver createBreadthFirst() {
        return new IterativeSearch(new BreadthFirst(new IndividualPermutationExplorer()));
    }
}
