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

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.QuadraticMean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Sensitivity Analysis with respect to the number of species over which the
 * population is distributed.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Species {

    // Constants
    private static final String ABSOLUTE = "absolute";
    private static final String NORMALISED = "normalised";

    // Fields
    private final HashMap<String, DiversityMetric> metrics;
    private final ArrayList<Result> results;
    private int size;

    /**
     * Create a experiment with a default population of 100 individuals
     */
    public Species() {
        this.metrics = new HashMap<String, DiversityMetric>();
        this.results = new ArrayList<Result>();
        this.size = 100;
    }

    /**
     * Add a new diversity metric to be evaluated
     *
     * @param key the name associated with the given metric
     * @param metric the metric of interest
     */
    public void addMetric(String key, DiversityMetric metric) {
        this.metrics.put(key, metric);
    }

    /**
     * Set or reset the size of the population to use during the experiment
     *
     * @param size the an initial population size, as a number of individuals
     */
    public void setPopulationSize(int size) {
        this.size = size;
    }

    /**
     * Run the experiment
     */
    public void run() {
        results.clear();

        Population population = new Population();
        population.addSpecie("Specie #1", 99);
        population.addSpecie("Specie #2", 1);

        for (int i = 1; i < size; i++) {
            for (String key : this.metrics.keySet()) {
                DiversityMetric metric = this.metrics.get(key);
                results.add(new Result(population.getSpecies().size(), 0, key, ABSOLUTE, metric.absolute(population)));
                results.add(new Result(population.getSpecies().size(), 0, key, NORMALISED, metric.normalised(population)));
            }
            population.getSpecie("Specie #1").setIndividualCount(99 - i);
            population.addSpecie("Specie #" + (2 + i), 1);
        }

    }

    /**
     * Save the result of this experiment in the given file. If the given file
     * already exists, its content will be overridden.
     *
     * @param file the name of the file to be written.
     * @throws FileNotFoundException
     */
    public void saveResultAs(String file) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File(file));

        out.println("species count, useless, metric, kind, value");
        for (Result result : this.results) {
            out.println(result);
        }

        out.close();
    }

    /**
     * @return a view of the results associated with this experiment
     */
    public List<Result> getResults() {
        return Collections.unmodifiableList(results);
    }
}
