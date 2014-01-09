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
 * Implementation of the diversity metric as a quadratic mean of the species population.
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class QuadraticMean implements DiversityMetric {

    public double applyTo(Population population) throws EmptyPopulation {
        if (population.isEmpty()) {
            throw new EmptyPopulation(population);
        }
        double n = population.getIndividualCount();
        double s = population.getSpecies().size();
        
        double sum = 0.;
        for(Specie sp: population.getSpecies()) {
            double ratio = sp.getIndividualCount() / n;
            sum += Math.pow(ratio, 2);
        }
        double diversity = Math.pow(sum / s, -0.5);
        
        double normalized = (diversity - Math.sqrt(s) ) / (s - Math.sqrt(s));
        return normalized;
    }
    
}
