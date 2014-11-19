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
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.Population;

/**
 * Set the number of individual in a given specie, identified either by its
 * index or by its name.
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
        return subject.getSpecie(getSpecieIndex(subject)).setHeadcount(numberOfIndividuals);
    }

    @Override
    public boolean preserveTheNumberOfSpecies() {
        return true;
    }

    @Override
    public boolean preserveTheTotalNumberOfIndividuals() {
        return false;
    }

    @Override
    public int impactOnSpecie(String specieName, Population target) {
        if (target.hasAnySpecieNamed(specieName)
                && aimsAt(specieName)) {
            return target.getSpecie(specieName).getHeadcount() - numberOfIndividuals;
        }
        return 0;
    }

    @Override
    public int impactOnTheNumberOfSpecies() {
        return 0;
    }

    @Override
    public int impactOnTheNumberOfIndividuals() {
        throw new IllegalStateException("Unable to anticipate the impact of set the number of individuals to a particuar value");
    }
    

    @Override
    public String toString() {
        return getSpecieName() + " <- " + numberOfIndividuals ;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.numberOfIndividuals;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SetNumberOfIndividualsIn other = (SetNumberOfIndividualsIn) obj;
        return super.equals(other) && numberOfIndividuals == other.numberOfIndividuals;
    }
    
    
    
}
