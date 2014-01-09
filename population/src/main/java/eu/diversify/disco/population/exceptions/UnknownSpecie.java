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
 * Exception thrown when one searches for a species that does not exist.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class UnknownSpecie extends RuntimeException {

    private final Population population;
    private final String specieName;

    /**
     * Create a new UnknownSpecie exception pointing to the population of
     * interest and the faulty specie name.
     *
     * @param population the population of interest
     * @param specieName the name of the unknown specie
     */
    public UnknownSpecie(Population population, String specieName) {
        this.population = population;
        this.specieName = specieName;
    }
}
