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
package eu.diversify.disco.population.random;

import eu.diversify.disco.population.Population;

/**
 * Capture the profile of populations that have to be generated
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Profile {

    private final Range speciesCount;
    private final Range individualsCount;

    public Profile() {
        this.speciesCount = new Range(2, 10);
        this.individualsCount = new Range(50, 100);
    }

    public void setSpeciesCount(int min, int max) {
        this.speciesCount.reset(min, max);
    }

    public void setIndividualsCount(int min, int max) {
        this.individualsCount.reset(min, max);
    }
    
    public void setNumberOfIndividuals(Range numberOfIndividuals) {
        this.individualsCount.reset(numberOfIndividuals);
    }

    public Range getNumberOfIndividuals() {
        return this.individualsCount;
    }

    public Range getNumberOfSpecies() {
        return this.speciesCount;
    }
    
    public void setNumberOfSpecies(Range numberOfSpecies) {
        this.speciesCount.reset(numberOfSpecies);
    }

    public boolean matches(Population population) {
        return this.speciesCount.contains(population.getSpecies().size())
                && this.individualsCount.contains(population.getIndividualCount());
    }
}
