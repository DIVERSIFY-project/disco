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
import eu.diversify.disco.population.Specie;
import eu.diversify.disco.population.diversity.exceptions.EmptyPopulation;

/**
 * Implementation of the Gini-Simpson index
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class GiniSimpsonIndex extends DiversityMetric {

    @Override
    public double absolute(Population population) throws EmptyPopulation {
        if (population.isEmpty()) {
            throw new EmptyPopulation(population);
        }
        double result = 0.;
        final double n = population.getIndividualCount();
        for (Specie specie: population.getSpecies()) {
            result += Math.pow(specie.getIndividualCount() / n, 2);
        }
        return 1 - result;
    }

    @Override
    public double maximum(Population population) {
        return 1 - 1. / population.getSpecies().size();
    }

    @Override
    public double minimum(Population population) {
        return 0;
    }
    
}
