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
package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.ControllerFactory;
import eu.diversify.disco.controller.Problem;
import eu.diversify.disco.controller.Solution;
import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.naming.spi.DirStateFactory.Result;

/**
 * Evaluate the sensitivity of the response time against an increase of the
 * number of specie and/or an increase in the number of individuals in the
 * population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ScalabilityExperiment implements Experiment {

    private static final Field STRATEGY = new Field("strategy", String.class);
    private static final Field INDIVIDUALS_COUNT = new Field("individual count", Integer.class);
    private static final Field SPECIES_COUNT = new Field("species count", Integer.class);
    private static final Field DURATION = new Field("duration", Long.class);
    private static final Field ERROR = new Field("error", Double.class);
    private static final Schema SCHEMA = new Schema(Arrays.asList(new Field[]{STRATEGY, INDIVIDUALS_COUNT, SPECIES_COUNT, DURATION, ERROR}), "n/a");

    private final HashMap<String, Controller> controllers;
    private final ArrayList<Result> results;
    private final ArrayList<Integer> speciesCounts;
    private final ArrayList<Integer> individualsCount;

    /**
     * Create a new experiment from a setup file
     *
     * @param setupFile the file containing the configuration
     */
    public ScalabilityExperiment(ScalabilitySetup setup) throws ControllerInstantiationException {
        this.controllers = new HashMap<String, Controller>();
        this.individualsCount = new ArrayList<Integer>();
        this.speciesCounts = new ArrayList<Integer>();
        this.results = new ArrayList<Result>();

        // Load the configuration file
        this.setIndividualsCount(setup.getIndividualsCounts());
        this.setSpeciesCounts(setup.getSpeciesCounts());
        final ControllerFactory factory = new ControllerFactory();
        for (String strategy : setup.getStrategies()) {
            this.addController(strategy, factory.instantiate(strategy));
        }
    }

    /**
     * Add a new controller to the experiment, identified by a unique name
     *
     * @param key the name to identify the given controller
     * @param controller the controller to run during the experiment
     */
    private void addController(String key, Controller controller) {
        this.controllers.put(key, controller);
    }

    /**
     * Set the numbers of individuals to use during the experiment
     *
     * @param counts the list of individuals count used in this experiments
     */
    private void setIndividualsCount(List<Integer> counts) {
        this.individualsCount.clear();
        for (int c : counts) {
            this.individualsCount.add(c);
        }
    }

    /**
     * Set the many species size which must be evaluated during the experiment
     *
     * @param counts an array of integer representing the species to be tested
     */
    private void setSpeciesCounts(List<Integer> counts) {
        this.speciesCounts.clear();
        for (Integer i : counts) {
            this.speciesCounts.add(i);
        }
    }

    /**
     * Run the experiment
     */
    @Override
    public List<DataSet> run() {
        DataSet dataset = new DataSet(SCHEMA);

        int total = this.speciesCounts.size() * this.individualsCount.size() * this.controllers.size();
        System.out.println("Preparing for " + total + " run(s).");
        System.out.println("This may take several minutes ... ");

        for (int i = 0; i < this.speciesCounts.size(); i++) {
            for (int j = 0; j < this.individualsCount.size(); j++) {

                System.out.println("Scale [" + this.speciesCounts.get(i) + " x " + this.individualsCount.get(j) + "]: ");

                // Initialise the population to be tested
                final Population population = new Population();
                population.addSpecie("sp1", this.individualsCount.get(j));
                for (int s = 1; s < speciesCounts.get(i); s++) {
                    population.addSpecie("sp" + (s + 1), 0);
                }

                // Test the population with all selected control strategies
                for (String key : this.controllers.keySet()) {
                    final Controller controller = this.controllers.get(key);
                    final Problem problem = new Problem(population, 1.0, new TrueDiversity());

                    final long start = System.currentTimeMillis();
                    final Solution solution = controller.applyTo(problem);
                    final long duration = System.currentTimeMillis() - start;

                    Data data = SCHEMA.newData();
                    data.set(STRATEGY, key);
                    data.set(SPECIES_COUNT, population.getSpecies().size());
                    data.set(INDIVIDUALS_COUNT, population.getIndividualCount());
                    data.set(ERROR, solution.getError());
                    data.set(DURATION, duration);
                    dataset.add(data);
                    System.out.println("\t - " + key + " in " + duration + " ms");
                }

            }
        }

        ArrayList<DataSet> results = new ArrayList<DataSet>();
        results.add(dataset);
        return results;
    }
}
