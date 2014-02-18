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
 * Implementation of the diversity metric as a quadratic mean of the species
 * population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class TrueDiversity extends DiversityMetric {

    private final double theta;

    /**
     * Create a new True Diversity metric, by default based on a quadratic mean
     */
    public TrueDiversity() {
        this.theta = 2;
    }
    
    
    /**
     * @return the theta parameter
     */
    public double getTheta() {
        return this.theta;
    }

    /**
     * Create a customised true diversity metric
     *
     * @param theta the theta parameter which select the underlying means
     * (geometric, arithmetic, quadratic, etc.)
     */
    public TrueDiversity(double theta) {
        this.theta = theta;
    }

    @Override
    public double absolute(Population population) throws EmptyPopulation {
        if (population.isEmpty()) {
            throw new EmptyPopulation(population);
        }
        double n = population.getIndividualCount();
        double s = population.getSpecies().size();

        double sum = 0.;
        for (Specie sp : population.getSpecies()) {
            double ratio = sp.getIndividualCount() / n;
            sum += Math.pow(ratio, theta);
        }
        return Math.pow(sum / s, -1. / theta);
    }

    @Override
    public double maximum(Population population) {
        return population.getSpecies().size();
    }

    @Override
    public double minimum(Population population) {
        return Math.pow(population.getSpecies().size(), 1./theta);
    }
}