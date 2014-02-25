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
package eu.diversify.disco.population.diversity;

import eu.diversify.disco.population.Population;

/**
 * The general interface of diversity metrics
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface DiversityMetric {

    /**
     * Compute the upper bound of diversity for this metric and the given
     * population profile
     *
     * @param population the population profile
     * @return the diversity as a scalar value
     */
    public double minimum(Population population);

    /**
     * Compute the diversity level of the given population
     *
     * @param population the population whose diversity level is needed
     * @return the diversity as a scalar value
     */
    public double applyTo(Population population);

    /**
     * Compute the lower bound of diversity for this metric and the given
     * population profile
     *
     * @param population the population profile
     * @return the diversity as a scalar value
     */
    public double maximum(Population population);
    
    
    /**
     * @return this diversity metric but normalised onto the unit interval [0, 1]
     */
    public DiversityMetric normalise();
}
