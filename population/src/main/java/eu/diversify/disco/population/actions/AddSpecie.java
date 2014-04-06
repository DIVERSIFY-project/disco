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
 * Create a new specie in a given population
 */
public class AddSpecie implements Action {

    private static final String NO_SPECIE_NAME = null;
    private final String specieName;

    public AddSpecie() {
        specieName = NO_SPECIE_NAME;
    }

    public AddSpecie(String specieName) {
        if (specieName == null) {
            throw new IllegalArgumentException("Specie name shall not be null");
        }
        this.specieName = specieName;
    }

    @Override
    public Population applyTo(Population subject) {
        if (hasName()) {
            return subject.addSpecie(specieName);
        }
        else {
            return subject.addSpecie();
        }
    }

    @Override
    public boolean preserveTheNumberOfSpecies() {
        return false;
    }

    @Override
    public boolean preserveTheTotalNumberOfIndividuals() {
        return true;
    }

    @Override
    public int impactOnTheNumberOfSpecies() {
        return +1;
    }

    @Override
    public int impactOnTheNumberOfIndividuals() {
        return 0;
    }

    @Override
    public String toString() {
        if (hasName()) {
            return "add specie '" +  specieName + "'";
        } else {
            return "add 1 specie";
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.specieName != null ? this.specieName.hashCode() : 0);
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
        final AddSpecie other = (AddSpecie) obj;
        if ((this.specieName == null) ? (other.specieName != null) : !this.specieName.equals(other.specieName)) {
            return false;
        }
        return true;
    }

    private boolean hasName() {
        return specieName != NO_SPECIE_NAME;
    }
}
