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
import eu.diversify.disco.population.Specie;
import eu.diversify.disco.population.diversity.exceptions.EmptyPopulation;

/**
 * An alternative diversity metrics based on the standard deviation of relative
 * species size
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class StandardDeviation extends DiversityMetric {

    @Override
    public double absolute(Population population) throws EmptyPopulation {
        if (population.isEmpty()) {
            throw new EmptyPopulation(population);
        }
        
        final double n = (double) population.getIndividualCount();
        final int s = population.getSpecies().size();
        final double mu = 1. / s;

        double total = 0D;
        for (Specie specie : population.getSpecies()) {
            total += Math.pow(specie.getIndividualCount()/n - mu, 2);
        }
        
        final double sd =  Math.sqrt(total / s);
        return 1D / (1D + sd);
    }

    @Override
    public double maximum(Population population) {
        return 1D;
    }

    @Override
    public double minimum(Population population) {
        final double n = (double) population.getIndividualCount();
        final int s = population.getSpecies().size();
        final double mu = 1. / s;

        final double sd = Math.sqrt((Math.pow(1D - mu, 2) + (s - 1) * (mu * mu)) / s);
        return 1D / (1D + sd);
    }
}
