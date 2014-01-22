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
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.Problem;
import eu.diversify.disco.controller.Solution;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;

/**
 * Wrap the diversity controller and the transformation into a single object
 *
 * @author Franck Chauvel
 * @author Hui Song
 *
 * @since 0.1
 */
public class DiversityController {

    private final DiversityMetric metric;
    private final Controller controller;
    private final Transformation transformation;
    private double reference;

    /**
     * Create a new diversity controller which ensure a given diversity level
     *
     * @param reference the diversity level to maintain
     */
    public DiversityController(double reference) {
        this.reference = reference;
        this.metric = new TrueDiversity();
        this.controller = new AdaptiveHillClimber();
        this.transformation = new Transformation();
    }

    /**
     * Update the given CloudML model so as to maintain the predefined diversity
     * level
     *
     * @param current the model which has to be diversified
     *
     * @return the resulting model
     */
    public CloudML applyTo(CloudML current) {
        Population population = transformation.forward(current);
        final Problem problem = new Problem(population, this.reference, this.metric);
        Solution result = this.controller.applyTo(problem);
        transformation.backward(current, result.getPopulation());
        return current;
    }
}
