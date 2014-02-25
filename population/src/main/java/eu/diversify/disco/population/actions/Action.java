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
 * General behaviour of actions run against a population
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Action {

    /**
     * Apply this action on the given population. The given population is left
     * unchanged
     *
     * @param subject the population that should undergo this action
     * @return a new population as the result of this action.
     */
    public Population applyTo(Population subject);
    
    
    /**
     * @return true if this action preserve the number of species
     */
    public boolean preserveTheNumberOfSpecies();

    /**
     * @return true if this action preserve the total number of individuals
     */
    public boolean preserveTheTotalNumberOfIndividuals();
}
