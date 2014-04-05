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

import eu.diversify.disco.controller.problem.Solution;
import java.util.ArrayList;

public abstract class AbstractSolver implements Solver {

    private final ArrayList<SolverListener> listeners;

    public AbstractSolver() {
        listeners = new ArrayList<SolverListener>();
    }

    @Override
    public void subscribe(SolverListener listener) {
        listeners.add(listener);
    }

    protected void publishInitialSolution(Solution solution) {
        for (SolverListener listener : listeners) {
            listener.onInitialSolution(solution);
        }
    }

    protected void publishIntermediateSolution(Solution solution) {
        for (SolverListener listener : listeners) {
            listener.onIntermediateSolution(solution);
        }
    }

    protected void publishFinalSolution(Solution solution) {
        for (SolverListener listener : listeners) {
            listener.onFinalSolution(solution);
        }
    }
}
