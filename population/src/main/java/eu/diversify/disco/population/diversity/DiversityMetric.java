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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
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
public abstract class DiversityMetric {

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
    public abstract double absolute(Population population) throws EmptyPopulation;

    /**
     * Compute the maximum diversity level for a population with characteristic
     * similar to the given one.
     *
     * @param population the prototype population, with the characteristic of
     * interest
     * 
     * @return the maximum diversity level
     */
    public abstract double maximum(Population population);

    
    /**
     * Compute the minimum diversity level for a population with characteristic
     * similar to the given one.
     *
     * @param population the prototype population, with the characteristic of
     * interest
     * 
     * @return the minimum diversity level
     */
    public abstract double minimum(Population population);

    /**
     * Normalise the metric over the unit interval [0, 1].
     *
     * @param population the population of interest
     *
     * @return the normalised diversity as a value between 0 and 1.
     */
    public double normalised(Population population) throws EmptyPopulation {
        final double diversity = absolute(population);
        final double min = minimum(population);
        final double max = maximum(population);
        return (diversity - min) / (max - min);
    }
}
