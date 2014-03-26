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

package eu.diversify.disco.controller;

import eu.diversify.disco.controller.solvers.Solver;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import eu.diversify.disco.controller.problem.Solution;


public class Controller {
    
    private final Reference reference;
    private final PopulationReader source;
    private final Solver strategy;
    private final PopulationWriter target;
    private final ProblemBuilder builder;

    public Controller(ProblemBuilder builder, Reference reference, PopulationReader source, Solver strategy, PopulationWriter target) {
        this.reference = reference;
        this.source = source;
        this.strategy = strategy;
        this.target = target;
        this.builder = builder;
    }    
    
    public void control() {
        final Problem problem = builder
                .withInitialPopulation(source.read())
                .withReferenceDiversity(reference.getReference())
                .make();
        final Solution solution = strategy.solve(problem);
        System.out.println(solution);
        target.write(solution);
    }

}
