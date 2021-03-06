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
import eu.diversify.disco.population.Specie;

/**
 * Shift (i.e., add) the number of individual in a given specie, identified by
 * either its name or its index.
 */
public class ShiftNumberOfIndividualsIn extends SpecieAccess {

    private final int offset;

    public ShiftNumberOfIndividualsIn(int specieIndex, int offset) {
        super(specieIndex);
        this.offset = offset;
    }

    public ShiftNumberOfIndividualsIn(String specieName, int offset) {
        super(specieName);
        this.offset = offset;
    }

    @Override
    public Population applyTo(Population subject) {
        return subject.getSpecie(getSpecieIndex(subject)).shiftHeadcountBy(offset);
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
    public int impactOnTheNumberOfSpecies() {
        return 0;
    }

    @Override
    public int impactOnTheNumberOfIndividuals() {
        return offset;
    }

    @Override
    public int impactOnSpecie(String specieName, Population target) {
        if (target.hasAnySpecieNamed(specieName)
                && aimsAt(specieName)) {
            return offset;
        }
        return 0;
    }

    
    @Override
    public String toString() {
        String operator = (offset < 0) ? " - " : " + " ; 
        return getSpecieName() + operator + Math.abs(offset);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.offset;
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
        final ShiftNumberOfIndividualsIn other = (ShiftNumberOfIndividualsIn) obj;
        return super.equals(other) && this.offset == other.offset;
    }
}
