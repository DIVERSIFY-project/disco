/*
 */
package eu.diversify.disco.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A dummy class, as an example of individuals
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Animal implements Individual {

    private final ArrayList<IPopulation> observers;
    private final ArrayList<String> species;

    public Animal(String specie) {
        this.observers = new ArrayList<IPopulation>();
        this.species = new ArrayList<String>();
        this.species.add(specie);
    }

    @Override
    public void join(IPopulation population) {
        if (!this.observers.contains(population)) {
            this.observers.add(population);
            for (String specie : this.species) {
                if (!population.hasAnySpecieNamed(specie)) {
                    population.addSpecie(specie);
                }
                population.shiftNumberOfIndividualsIn(specie, 1);
            }
        }
    }

    @Override
    public List<String> getSpecies() {
        return Collections.unmodifiableList(this.species);
    }

    @Override
    public void addSpecie(String specie) {
        if (!this.species.contains(specie)) {
            this.species.add(specie);
            for (IPopulation population : this.observers) {
                if (!population.hasAnySpecieNamed(specie)) {
                    population.addSpecie(specie);
                }
                population.shiftNumberOfIndividualsIn(specie, 1);
            }
        }
    }

    @Override
    public void leave(IPopulation population) {
        if (!this.observers.contains(population)) {
            this.observers.remove(population);
            for (String specie : this.species) {
                population.shiftNumberOfIndividualsIn(specie, -1);
            }
        }
    }

    @Override
    public void setSpecies(List<String> species) {
        for (IPopulation population : this.observers) {
            for (String specie: this.species) {
                population.shiftNumberOfIndividualsIn(specie, -1);                
            }
        }
        this.species.clear();
        this.species.addAll(species);
        for (IPopulation population : this.observers) {
            for (String specie: this.species) {
                population.shiftNumberOfIndividualsIn(specie, +1);
            }
        }
    }

}
