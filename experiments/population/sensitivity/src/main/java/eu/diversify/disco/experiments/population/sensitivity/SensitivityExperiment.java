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

package eu.diversify.disco.experiments.population.sensitivity;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.MetricFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The sensitivity analysis of the diversity metrics. Include sensitivity to the
 * distribution of individuals, to a increasing number of specie, and to an
 * increasing number of individuals.
 *
 * FIXME: Should be broken down into three subclasses, each handling a specific 
 * sensitivity experiment. Seems to challenges the commons of experiments.
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class SensitivityExperiment implements Experiment {

    private static final String SPECIE_NAME = "Specie no ";
    private static final Field SPECIE_1 = new Field(SPECIE_NAME + 1, Integer.class);
    private static final Field SPECIES_COUNT = new Field("species count", Integer.class);
    private static final Field INDIVIDUALS_COUNT = new Field("individuals count", Integer.class);
    private static final Field METRIC = new Field("metric", String.class);
    private static final Field NORMALISED = new Field("normalised", Double.class);
    private static final Field ABSOLUTE = new Field("absolute", Double.class);
    private static final Schema SCHEMA = new Schema(Arrays.asList(new Field[]{
        SPECIE_1, SPECIES_COUNT, INDIVIDUALS_COUNT, METRIC, NORMALISED, ABSOLUTE}), "n/a");
    private int size;
    private final HashMap<String, DiversityMetric> metrics;

    /**
     * Create new sensitivity experiment with a given setup
     *
     * @param setup the parameters of the experiment
     */
    public SensitivityExperiment(SensitivitySetup setup) {
        this.size = setup.getSize();
        this.metrics = new HashMap<String, DiversityMetric>();

        for (String metric : setup.getMetrics()) {
            this.metrics.put(metric, MetricFactory.create(metric));
        }
    }

    @Override
    public List<DataSet> run() {
        ArrayList<DataSet> results = new ArrayList<DataSet>();
        results.add(sensitivityToDistribution());
        results.add(sensitivityToSpeciesCount());
        results.add(sensitivityToIndividualsCount());
        return results;
    }

    /**
     * Evaluate the given population against all selected metrics and complete
     * the given dataset accordingly.
     *
     * @param ds the dataset to enhance with the evaluation of the given
     * population
     * @param p the population to evaluate
     */
    private void evaluate(DataSet ds, Population p) {
        for (String key : this.metrics.keySet()) {
            Data d = ds.getSchema().newData();
            d.set(SPECIE_1, p.getSpecie(SPECIE_1.getName()).getHeadcount());
            d.set(SPECIES_COUNT, p.getSpeciesCount());
            d.set(INDIVIDUALS_COUNT, p.getTotalHeadcount());
            DiversityMetric metric = this.metrics.get(key);
            d.set(METRIC, key);
            d.set(ABSOLUTE, metric.applyTo(p));
            d.set(NORMALISED, metric.applyTo(p));
            ds.add(d);
        }
    }

    /**
     * Sensitivity to the distribution of individuals
     *
     * @return the related dataset
     */
    private DataSet sensitivityToDistribution() {
        DataSet dataset = new DataSet(SCHEMA, "distribution");

        // Initial population: p = [x, 0] 
        Population population = aPopulation()
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .withSpeciesNamed(SPECIE_NAME + 1, SPECIE_NAME + 2)
                .withDistribution(this.size, 0)
                .build();
        evaluate(dataset, population);

        /*
         * FIXME: refactor the loop below so that it reflects the following
         * intent: "Iterate until we reach a balanced population p = [x/2, x/2]"
         */
        for (int i = 1; i < this.size / 2; i++) {
            population.getSpecie(1).setHeadcount(this.size - i);
            population.getSpecie(2).setHeadcount(i);
            evaluate(dataset, population);
        }

        return dataset;
    }
    
    

    /**
     * Sensitivity to the number of species
     *
     * @return the related dataset
     */
    private DataSet sensitivityToSpeciesCount() {
        DataSet dataset = new DataSet(SCHEMA, "species");

        // Initial population, p = [x-1, 1]
        Population population = aPopulation()
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .withSpeciesNamed(SPECIE_NAME + 1, SPECIE_NAME + 2)
                .withDistribution(this.size - 1, 1)
                .build();
        evaluate(dataset, population);

        /**
         * FIXME: refactor the loop below so it express the following intent
         * Iterate until p = [1, 1, ..., 1], with x species
         */
        for (int i = 1; i < this.size - 1; i++) {
            population.getSpecie(1).setHeadcount(99 - i);
            population.addSpecie(SPECIE_NAME + (2 + i));
            population.getSpecie(2+i).setHeadcount(1);
            evaluate(dataset, population);
        }

        return dataset;
    }
    

    /**
     * Sensitivity to an increase of the number of individuals
     *
     * @return the dataset containing sensitivity observation
     */
    private DataSet sensitivityToIndividualsCount() {
        DataSet dataset = new DataSet(SCHEMA, "individuals");

        // Initial population
        Population p = aPopulation()
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .withSpeciesNamed(SPECIE_NAME + 1, SPECIE_NAME + 2)
                .withDistribution(this.size, 0)
                .build();
        evaluate(dataset, p);

        /**
         * FIXME: refactor the loop below so as it reflect the following intent
         * Iterate until we reach a balanced population p = [x, x]
         */
        for (int i = 1; i < this.size; i++) {
            p.getSpecie(2).setHeadcount(i);
            evaluate(dataset, p);
        }

        return dataset;
    }
}
