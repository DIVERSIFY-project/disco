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
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;

/**
 * Represent an instance of the control problem including the initial
 * population, the reference diversity level and diversity metric in use.
 *
 * A case is a value object which cannot be change over the time.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Case {

    private final DiversityMetric metric;
    private final double reference;
    private final Population initialPopulation;

    /**
     * Create a new case from the initial population, the reference diversity
     * level and the diversity metric to use
     *
     * @param initialPopulation the population which has to be diversified
     * @param reference the reference diversity level
     * @param metric the diversity metric of interest
     */
    public Case(Population initialPopulation, double reference, DiversityMetric metric) {
        this.metric = metric;
        this.reference = reference;
        this.initialPopulation = initialPopulation;
    }

    
    /**
     * @return  the diversity metric in use
     */
    public DiversityMetric getMetric() {
        return metric;
    }

    /**
     * @return the reference diversity value
     */
    public double getReference() {
        return reference;
    }

    /**
     * @return the initial population
     */
    public Population getInitialPopulation() {
        return initialPopulation;
    }
    
    
    /**
     * Evaluate the population p, seen as a solution for this case
     * @param p the population to evaluate
     * @return an evaluation of the given population
     */
    public Evaluation evaluate(Population p) {
        final double diversity = this.metric.normalised(p);
        final double error = Math.pow(this.reference - diversity, 2);
        return new Evaluation(this, p, diversity, error);
    }
    
    
    
    
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Case) {
            Case c = (Case) o;
            result = this.initialPopulation.equals(c.initialPopulation) 
                    && this.reference == c.reference 
                    && this.metric.equals(c.metric);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.metric != null ? this.metric.hashCode() : 0);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.reference) ^ (Double.doubleToLongBits(this.reference) >>> 32));
        hash = 97 * hash + (this.initialPopulation != null ? this.initialPopulation.hashCode() : 0);
        return hash;
    }
    
    
}
