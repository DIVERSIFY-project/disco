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

import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.solvers.AbstractSolver;

/*
 * Iterative search for a solution, by succesive improvements
 */
public class IterativeSearch extends AbstractSolver {

    private final SearchStrategy search;

    public IterativeSearch(SearchStrategy search) {
        super();
        this.search = search;
    }

    public SearchStrategy getSearchFactory() {
        return search;
    }

    @Override
    public String getName() {
        return search.getName();
    }
    
 
   
    @Override
    public Solution solve(Problem problem) {
        search.setUp(problem);
        publishInitialSolution(search.getCurrentSolution());
        while (search.hasMoreImprovement()) {
            publishIntermediateSolution(search.improve());
        }
        publishFinalSolution(search.getCurrentSolution());
        return search.getCurrentSolution();
    }


}
