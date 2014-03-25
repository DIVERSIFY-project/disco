/*
 */

package eu.diversify.disco.controller;

import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.PopulationReader;
import eu.diversify.disco.population.PopulationWriter;


public class Facade {
    
    private final Reference reference;
    private final PopulationReader source;
    private final Controller strategy;
    private final PopulationWriter target;
    private final ProblemBuilder builder;

    public Facade(ProblemBuilder builder, Reference reference, PopulationReader source, Controller strategy, PopulationWriter target) {
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
        final Solution solution = strategy.applyTo(problem);
        System.out.println(solution);
        target.write(solution.getPopulation());
    }

}
