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

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;

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
 
    // Fields
    private Population initialPopulation;
    private double referenceDiversity;
    private DiversityMetric metric;
    
    public static ProblemBuilder aProblem() {
        return new ProblemBuilder();
    } 

    private ProblemBuilder() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        initialPopulation = aPopulation().withDistribution(0).build();
        referenceDiversity = DEFAULT_REFERENCE_DIVERSITY_LEVEL;
        metric = DEFAULT_DIVERSITY_METRIC;
    }

    public ProblemBuilder withInitialPopulation(Population initialPopulation) {
        this.initialPopulation = initialPopulation;
        return this;
    }
    
      public ProblemBuilder withInitialPopulation(PopulationBuilder population) {
        this.initialPopulation = population.build();
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

    public Problem build() {
        Problem result = new Problem(initialPopulation, referenceDiversity, metric);
        return result;
    }
}
