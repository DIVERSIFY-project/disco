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
 * Create a new specie in a given population
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class AddSpecie implements Action {

    private final String specieName;
    
    public AddSpecie(String specieName) {
        if (specieName == null) {
            throw new IllegalArgumentException("Specie name shall not be null");
        }
        this.specieName = specieName;
    }

    @Override
    public Population applyTo(Population subject) {
        return subject.addSpecie(specieName);
    }

    @Override
    public boolean preserveTheNumberOfSpecies() {
        return false;
    }

    @Override
    public boolean preserveTheTotalNumberOfIndividuals() {
        return true;
    }
    
}
