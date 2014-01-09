/**
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


package eu.diversify.disco.population.exceptions;

import eu.diversify.disco.population.Population;

/**
 * Exception raised if one tries to create to specie with the same unique ID.
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class DuplicateSpecieId extends RuntimeException {
    
    private Population population;
    private String duplicateName;

    
    /**
     * Create a new DuplicateSpecieId exception with the population at sake and 
     * ID which is already used.
     * 
     * @param population the erroneous population 
     * @param duplicateName the specie name already used the given population
     */
    public DuplicateSpecieId(Population population, String duplicateName) {
        this.population = population;
        this.duplicateName = duplicateName;
    }
        
    
}
