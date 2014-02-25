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
package eu.diversify.disco.population.decorators;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.decorators.Individual;
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

    private final ArrayList<Population> observers;
    private final ArrayList<String> species;

    public Animal(String specie) {
        this.observers = new ArrayList<Population>();
        this.species = new ArrayList<String>();
        this.species.add(specie);
    }

    @Override
    public void join(Population population) {
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
            for (Population population : this.observers) {
                if (!population.hasAnySpecieNamed(specie)) {
                    population.addSpecie(specie);
                }
                population.shiftNumberOfIndividualsIn(specie, 1);
            }
        }
    }

    @Override
    public void leave(Population population) {
        if (this.observers.contains(population)) {
            this.observers.remove(population);
            for (String specie: this.species) {
                population.shiftNumberOfIndividualsIn(specie, -1);
            }
        }
    }

    @Override
    public void setSpecies(List<String> species) {
        for (Population population : this.observers) {
            for (String specie: this.species) {
                population.shiftNumberOfIndividualsIn(specie, -1);                
            }
        }
        this.species.clear();
        this.species.addAll(species);
        for (Population population : this.observers) {
            for (String specie: this.species) {
                population.shiftNumberOfIndividualsIn(specie, +1);
            }
        }
    }

}
