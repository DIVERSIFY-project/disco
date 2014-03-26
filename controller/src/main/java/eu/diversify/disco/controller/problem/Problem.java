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
package eu.diversify.disco.controller.problem;

import eu.diversify.disco.controller.problem.constraints.Constraint;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.diversity.DiversityMetric;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represent an instance of the control problem including the initial
 * population, the reference diversity level and diversity metric in use.
 *
 * A problem is a value object which cannot be change over the time.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Problem {

    private final DiversityMetric metric;
    private final double reference;
    private final Population initialPopulation;
    private final ArrayList<Constraint> constraints;

    /**
     * Create a new case from the initial population, the reference diversity
     * level and the diversity metric to use
     *
     * @param initialPopulation the population which has to be diversified
     * @param reference the reference diversity level
     * @param metric the diversity metric of interest
     * @param constraints additional constraints, which can reduce the space of
     * potential solutions
     */
    public Problem(Population initialPopulation, double reference, DiversityMetric metric, Collection<Constraint> constraints) {
        checkIfPopulationIsValid(initialPopulation);
        checkIfDiversityMetricIsValid(metric);
        checkIfReferenceIsWithinTheRangeOfTheMetric(initialPopulation, metric, reference);
        checkIfConstraintsAreValid(constraints);
        this.metric = metric;
        this.reference = reference;
        this.initialPopulation = aPopulation()
                .clonedFrom(initialPopulation)
                .immutable()
                .build();
        this.constraints = new ArrayList<Constraint>(constraints);
    }

    private void checkIfPopulationIsValid(Population population) {
        if (population == null) {
            throw new IllegalArgumentException("'null' cannot be used as the initial population of a problem");
        }
        if (population.isEmpty()) {
            throw new IllegalArgumentException("Empty population are irrelevant for diversity control.");
        }
    }

    private void checkIfDiversityMetricIsValid(DiversityMetric metric) {
        if (metric == null) {
            throw new IllegalArgumentException("'null' cannot be used as a diversity metric.");
        }
    }

    private void checkIfReferenceIsWithinTheRangeOfTheMetric(Population initialPopulation, DiversityMetric metric, double reference) {
        double min = metric.minimum(initialPopulation);
        double max = metric.maximum(initialPopulation);
        if (reference < min || reference > max) {
            String message = String.format(
                    "The given reference level is irrelevant for the selected diversity metric (d in [%.2f, %.2f])",
                    min,
                    max);
            throw new IllegalArgumentException(message);
        }
    }
    
    private void checkIfConstraintsAreValid(Collection<Constraint> constraints) {
        if (constraints == null) {
            throw new IllegalArgumentException("Constraints cannot be null");
        }
    }

    /**
     * @return the diversity metric in use
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
     *
     * @param p the population to evaluate
     * @return an evaluation of the given population
     */
    public Solution evaluate(Population p) {
        final double diversity = this.metric.applyTo(p);
        final double error = Math.pow(this.reference - diversity, 2);
        return new Solution(this, p, diversity, error);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Problem) {
            Problem c = (Problem) o;
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

    /**
     * @return the evaluation of the initial population
     */
    public Solution getInitialEvaluation() {
        return this.evaluate(this.initialPopulation);
    }

    public boolean isLegal(Action action) {
        boolean result = true;
        for (Constraint constraint : constraints) {
            result = result && constraint.isLegal(action);
        }
        return result;
    }

   
}
