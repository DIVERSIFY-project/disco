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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Compare the sensitivity of diversity metrics regarding the addition/removal
 * of the number of individual in a given population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Individuals {

    private static final String SPECIE_1 = "specie #1";
    private static final String SPECIE_2 = "specie #2";
    private static final String ABSOLUTE = "absolute";
    private static final String NORMALISED = "normalised";
    private int size;
    private final HashMap<String, DiversityMetric> metrics;
    private final ArrayList<Result> results;

    /**
     * Create a new experiment, with a default value of 100 individual
     * distributed over two specie
     */
    public Individuals() {
        this.size = 100;
        this.metrics = new HashMap<String, DiversityMetric>();
        this.results = new ArrayList<Result>();
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
        this.results.clear();

        for (int i = 0; i < size; i++) {
            final int specie1 = size - i;
            final int specie2 = i;

            final Population population = new Population();
            population.addSpecie(SPECIE_1, specie1);
            population.addSpecie(SPECIE_2, specie2);

            for (String key : metrics.keySet()) {
                final DiversityMetric metric = metrics.get(key);
                this.results.add(new Result(specie1, specie2, key, ABSOLUTE, metric.absolute(population)));
                this.results.add(new Result(specie1, specie2, key, NORMALISED, metric.normalised(population)));
            }
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

        out.println("specie #1, specie #2, metric, kind, value");
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
