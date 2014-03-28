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
 * Generalise the behaviour of actions which access a specie
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class SpecieAccess implements Action {

    public static final String UNDEFINED_SPECIE_NAME = "###!!!UNDEFINED!!!###";
    public static final int UNDEFINED_INDEX = -1;
    
    private final int specieIndex;
    private final String specieName;

    public SpecieAccess(int specieIndex) {
        this.specieIndex = specieIndex;
        this.specieName = UNDEFINED_SPECIE_NAME;
    }
    
    public SpecieAccess(String specieName) {
        this.specieIndex = UNDEFINED_INDEX;
        this.specieName = specieName;
    }

    protected int getSpecieIndex(Population population) {
        int index = specieIndex;
        if (hasSpecieIndex(index)) {
            index = population.getSpecieIndex(specieName);
        }
        return index;
    }
    
    protected String getSpecieName() {
        return (hasSpecieName()) ? specieName : String.format("s%d", specieIndex);
    }

    private boolean hasSpecieName() {
        return !specieName.equals(UNDEFINED_SPECIE_NAME);
    }

    private boolean hasSpecieIndex(int index) {
        return index == UNDEFINED_INDEX;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.specieIndex;
        hash = 41 * hash + (this.specieName != null ? this.specieName.hashCode() : 0);
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
        final SpecieAccess other = (SpecieAccess) obj;
        if (this.specieIndex != other.specieIndex) {
            return false;
        }
        if ((this.specieName == null) ? (other.specieName != null) : !this.specieName.equals(other.specieName)) {
            return false;
        }
        return true;
    }
    
    
}
