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
package eu.diversify.disco.controller.problem;

import eu.diversify.disco.controller.problem.constraints.Constraint;
import eu.diversify.disco.controller.problem.constraints.FixedNumberOfSpecies;
import eu.diversify.disco.controller.problem.constraints.FixedTotalNumberOfIndividuals;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.HashSet;

/**
 * Simplifies the construction of Problem object, with all the possible variants
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ProblemBuilder {

    public static final double DEFAULT_REFERENCE_DIVERSITY_LEVEL = 0.75;
    public static final DiversityMetric DEFAULT_DIVERSITY_METRIC = new TrueDiversity();
    public static final boolean DEFAULT_TOTAL_NUMBER_OF_INDIVIDUALS_FIXED = false;
    public static final boolean DEFAULT_NUMBER_OF_SPECIES_FIXED = false;
    private static final Constraint FIXED_NUMBER_OF_INDIVIDUALS = new FixedTotalNumberOfIndividuals();
    private static final Constraint FIXED_NUMBER_OF_SPECIES = new FixedNumberOfSpecies();
    // Fields
    private Population initialPopulation;
    private double referenceDiversity;
    private DiversityMetric metric;
    private final HashSet<Constraint> constraints;
    
    public static ProblemBuilder aProblem() {
        return new ProblemBuilder();
    } 

    public ProblemBuilder() {
        constraints = new HashSet<Constraint>();
        setDefaultValues();
    }

    private void setDefaultValues() {
        initialPopulation = aPopulation().withDistribution(0).build();
        referenceDiversity = DEFAULT_REFERENCE_DIVERSITY_LEVEL;
        metric = DEFAULT_DIVERSITY_METRIC;
        constraints.clear();
    }

    public ProblemBuilder withInitialPopulation(Population initialPopulation) {
        this.initialPopulation = initialPopulation;
        return this;
    }

    public ProblemBuilder withReferenceDiversity(double diversityLevel) {
        this.referenceDiversity = diversityLevel;
        return this;
    }

    public ProblemBuilder withDiversityMetric(DiversityMetric metric) {
        this.metric = metric;
        return this;
    }

    public ProblemBuilder withFixedTotalNumberOfIndividuals() {
        constraints.add(FIXED_NUMBER_OF_INDIVIDUALS);
        return this;
    }

    public ProblemBuilder withFixedNumberOfSpecies() {
        constraints.add(FIXED_NUMBER_OF_SPECIES);
        return this;
    }

    public Problem make() {
        Problem result = new Problem(initialPopulation, referenceDiversity, metric, constraints);
        return result;
    }
}
