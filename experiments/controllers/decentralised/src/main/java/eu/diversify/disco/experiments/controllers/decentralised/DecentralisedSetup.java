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
/*
 */
package eu.diversify.disco.experiments.controllers.decentralised;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.Setup;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.MetricFactory;
import java.util.Random;

public class DecentralisedSetup implements Setup {

    public static final int DEFAULT_STEP_COUNT = 10;
    public static final int DEFAULT_RUN_COUNT = 10;
    public static final int DEFAULT_POPULATION_SIZE = 10;
    public static final int DEFAULT_SPECIES_COUNT = 10;
    public static final double DEFAULT_MUTATION_RATE = 0.3;
    public static final double DEFAULT_NEIGHBOURHOOD_RATIO = 0.1;
    public static final String DEFAULT_DIVERSITY_METRIC = "True Diversity (theta = 2)";
    public static final double DEFAULT_WORLD_SIZE = 100D;
   
    private int stepCount;
    private int runCount;
    private int populationSize;
    private int speciesCount;
    private double mutationRate;
    private double neighbourhoodRatio;
    private String diversityMetric;
    private double worldSize;

    public DecentralisedSetup() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        stepCount = DEFAULT_STEP_COUNT;
        runCount = DEFAULT_RUN_COUNT;
        populationSize = DEFAULT_POPULATION_SIZE;
        speciesCount = DEFAULT_SPECIES_COUNT;
        mutationRate = DEFAULT_MUTATION_RATE;
        neighbourhoodRatio = DEFAULT_NEIGHBOURHOOD_RATIO;
        diversityMetric = DEFAULT_DIVERSITY_METRIC;
        worldSize = DEFAULT_WORLD_SIZE;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        assert populationSize >= 1;
        this.stepCount = stepCount;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        assert populationSize >= 1;
        this.runCount = runCount;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        assert populationSize >= 0;
        this.populationSize = populationSize;
    }

    public int getSpeciesCount() {
        return speciesCount;
    }

    public void setSpeciesCount(int speciesCount) {
        assert speciesCount >= 0;
        this.speciesCount = speciesCount;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        assert mutationRate >= 0D && mutationRate <= 1D;
        this.mutationRate = mutationRate;
    }

    @Override
    public Experiment buildExperiment() {
        return new DecentralisedExperiment(this);
    }

  

    public Position randomPosition() {
        final Random random = new Random();
        return new Position(worldSize * random.nextDouble(), worldSize * random.nextDouble());
    }

    public int randomSpecie() {
        return 1 + new Random().nextInt(speciesCount);
    }

    public boolean mutationShallOccurs() {
        return new Random().nextDouble() < mutationRate;
    }

    public double getNeighbourhoodRadius() {
        return neighbourhoodRatio * worldSize;
    }

    public double getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(double worldSize) {
        this.worldSize = worldSize;
    }
    
    public String getDiversityMetric() {
        return diversityMetric;
    }

    public void setDiversityMetric(String diversityMetric) {
        this.diversityMetric = diversityMetric;
    }

    public double getNeighbourhoodRatio() {
        return neighbourhoodRatio;
    }
    
    public void setNeighbourhoodRatio(double neighbourhoodRatio) {
        assert neighbourhoodRatio >= 0D && neighbourhoodRatio <= 1D;
        this.neighbourhoodRatio = neighbourhoodRatio;
    }

    public double diversityOf(Population population) {
        return getMetric().applyTo(population);
    }

    private DiversityMetric getMetric() {
        return MetricFactory.create(getDiversityMetric());
    }
}
