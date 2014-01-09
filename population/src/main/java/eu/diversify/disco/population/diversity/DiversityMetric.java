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
package eu.diversify.disco.population.diversity;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.exceptions.EmptyPopulation;

/**
 * General interface of diversity metrics, modelled as a function from
 * population to Real values.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface DiversityMetric {

    /**
     * Measure the diversity level of a given population. A diversity of 1 means
     * that all species share the same number of individuals whereas a diversity
     * of 0 mean that all individuals are in a single species.
     *
     * @param population the population whose diversity level is needed
     * 
     * @return a real value on the unit interval representing the diversity
     * level.
     */
    public double applyTo(Population population) throws EmptyPopulation ;
}
