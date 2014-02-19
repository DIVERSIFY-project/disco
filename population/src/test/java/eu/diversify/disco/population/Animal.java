/*
 */
package eu.diversify.disco.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * A dummy class, as an example of individuals
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Animal implements Individual {

    private final ArrayList<DynamicPopulation> observers;
    private final ArrayList<String> species;

    public Animal(String specie) {
        this.observers = new ArrayList<DynamicPopulation>();
        this.species = new ArrayList<String>();
        this.species.add(specie);
    }

    public void join(DynamicPopulation population) {
        if (!this.observers.contains(population)) {
            this.observers.add(population);
            for (String specie : this.species) {
                population.addIndividualIn(specie);
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
            for (DynamicPopulation population : this.observers) {
                population.addIndividualIn(specie);
            }
        }
    }

    @Override
    public void leave(DynamicPopulation population) {
        if (!this.observers.contains(population)) {
            this.observers.remove(population);
            for (String specie : this.species) {
                population.removeIndividualFrom(specie);
            }
        }
    }

    @Override
    public void setSpecies(List<String> species) {
        for (DynamicPopulation population : this.observers) {
            population.removeIndividualFrom(new HashSet<String>(this.species));
        }
        this.species.clear();
        this.species.addAll(species);
        for (DynamicPopulation population : this.observers) {
            population.addIndividualIn(new HashSet<String>(this.species));
        }
    }
}
