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
 * Remove a given specie, identified either by its index or by its name
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class RemoveSpecie extends SpecieAccess {

    public RemoveSpecie(int specieIndex) {
        super(specieIndex);
    }

    public RemoveSpecie(String specieName) {
        super(specieName);
    }

    @Override
    public Population applyTo(Population subject) {
        return subject.removeSpecie(getSpecieIndex(subject));
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
        return -1;
    }

    @Override
    public int impactOnTheNumberOfIndividuals() {
        return 0;
    }

    @Override
    public String toString() {
        return "del. " + getSpecieName();
    }
    
    
    
    
    
}
