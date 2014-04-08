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
