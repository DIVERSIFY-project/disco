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

import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;

/**
 * General Interface for any kind of diversity controller, including iterative
 * search or analytical solutions.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Solver {
    
    /**
     * Solve the given problem if possible.
     *
     * @param problem the problem to be solve, including the diversity metric,
     * the initial population and reference diversity.
     *
     * @return the evaluation of the best/first solution found
     */
    public Solution solve(Problem problem);
}
