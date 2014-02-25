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
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.Population;

/**
 * Set the number of individual in a given specie, identified either by its
 * index or by its name.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class SetNumberOfIndividualsIn extends SpecieAccess {

    private final int numberOfIndividuals;

    public SetNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        super(specieIndex);
        this.numberOfIndividuals = numberOfIndividuals;
    }

    public SetNumberOfIndividualsIn(String specieName, int numberOfIndividuals) {
        super(specieName);
        this.numberOfIndividuals = numberOfIndividuals;
    }

    @Override
    public Population applyTo(Population subject) {
        return subject.setNumberOfIndividualsIn(getSpecieIndex(subject), numberOfIndividuals);
    }

    @Override
    public boolean preserveTheNumberOfSpecies() {
        return true;
    }

    @Override
    public boolean preserveTheTotalNumberOfIndividuals() {
        return false;
    }
    
}
