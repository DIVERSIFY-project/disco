/*
 */

package eu.diversify.disco.experiments.controllers.decentralised;

import java.util.Random;


public class Run {

    private final DecentralisedSetup setup;
    private final Group individuals;
    private final double reference;
    
    public Run(DecentralisedSetup setup) {
        this.setup = setup;
        reference = new Random().nextDouble();
        individuals = new Group();
        for (int i = 0 ; i<setup.getPopulationSize() ; i++) {
            individuals.add(new Individual(setup));
        }
    }
       
    public double getError() {
        return Math.pow(reference - setup.diversityOf(individuals.asPopulation()), 2);
    }

    public Group getIndividuals() {
        return individuals;
    }

    void oneStep() {
        Group nextGeneration = new Group();
        for(Individual individual: individuals) {
            nextGeneration.add(individual.mutate(individuals, reference));  
        }
        this.individuals.replaceWith(nextGeneration);
    }

}
