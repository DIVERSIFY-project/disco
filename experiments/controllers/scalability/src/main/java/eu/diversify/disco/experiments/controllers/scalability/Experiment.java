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

import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.ControllerFactory;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.controller.Problem;
import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Evaluate the sensitivity of the response time against an increase of the
 * number of specie and/or an increase in the number of individuals in the
 * population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Experiment {

    
    private final HashMap<String, Controller> controllers;
    private final ArrayList<Result> results;
    private final ArrayList<Integer> speciesCounts;
    private final ArrayList<Integer> individualsCount;

    /**
     * Create a new experiment
     */
    public Experiment() {
        this.controllers = new HashMap<String, Controller>();
        this.individualsCount = new ArrayList<Integer>();
        this.individualsCount.add(25);
        this.individualsCount.add(50);
        this.speciesCounts = new ArrayList<Integer>();
        this.speciesCounts.add(2);
        this.speciesCounts.add(4);
        this.results = new ArrayList<Result>();
    }

    /**
     * Create a new experiment from a setup file
     *
     * @param setupFile the file containing the configuration
     */
    public Experiment(String setupFile) throws FileNotFoundException, ControllerInstantiationException {
        this.controllers = new HashMap<String, Controller>();
        this.individualsCount = new ArrayList<Integer>();
        this.speciesCounts = new ArrayList<Integer>();
        this.results = new ArrayList<Result>();

        // Load the configuration file
        final Yaml yaml = new Yaml(new Constructor(Setup.class));
        Setup setup = (Setup) yaml.load(new FileInputStream(setupFile));
        this.setIndividualsCount(setup.getIndividualsCounts());
        this.setSpeciesCounts(setup.getSpeciesCounts());
        final ControllerFactory factory = new ControllerFactory();
        for (String strategy: setup.getStrategies()) {
            this.addController(strategy, factory.instantiate(strategy));
        }

    }

    /**
     * Add a new controller to the experiment, identified by a unique name
     *
     * @param key the name to identify the given controller
     * @param controller the controller to run during the experiment
     */
    public void addController(String key, Controller controller) {
        this.controllers.put(key, controller);
    }

    /**
     * @return the number of individual used in this experiment
     */
    public List<Integer> getIndividualCounts() {
        return Collections.unmodifiableList(this.individualsCount);
    }

    /**
     * Set the numbers of individuals to use during the experiment
     *
     * @param counts the list of individuals count used in this experiments
     */
    public void setIndividualsCount(List<Integer> counts) {
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
    public void setSpeciesCounts(List<Integer> counts) {
        this.speciesCounts.clear();
        for (Integer i : counts) {
            this.speciesCounts.add(i);
        }
    }

    /**
     * @return the list of species size to be tested
     */
    public List<Integer> getSpeciesCounts() {
        return Collections.unmodifiableList(this.speciesCounts);
    }

    /**
     * @return an unmodifiable list of the controller run during the experiments
     */
    public List<Controller> getControllers() {
        return Collections.unmodifiableList(new ArrayList<Controller>(this.controllers.values()));
    }

    /**
     * Run the experiment
     */
    public void run() {

        for (int i = 0; i < this.speciesCounts.size(); i++) {
            for (int j = 0; j < this.individualsCount.size(); j++) {
                // Initialise the population to be tested
                final Population population = new Population();
                population.addSpecie("sp1", this.individualsCount.get(j));
                for (int s = 1; s < speciesCounts.get(i); s++) {
                    population.addSpecie("sp" + (s + 1), 0);
                }

                // Test the population
                for (String key : this.controllers.keySet()) {
                    System.out.println(" - Testing '" + key + "' with s = " + this.speciesCounts.get(i) + " & n = " + this.individualsCount.get(j));
                    final Controller controller = this.controllers.get(key);
                    final Problem problem = new Problem(population, 1.0, new TrueDiversity());
                    
                    final long start = System.currentTimeMillis();
                    controller.applyTo(problem);
                    final long duration = System.currentTimeMillis() - start;
                    this.results.add(new Result(key, individualsCount.get(j), speciesCounts.get(i), duration));
                }
            }
        }
    }

    /**
     * Write the result of the experiment in the given file
     *
     * @param file the path to the file where the experiment result must be
     * stored
     */
    public void saveResultsAs(String file) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File(file));
        out.println("controller, individuals, species, duration");

        for (Result r : this.results) {
            out.println(r.toString());
        }
        out.close();
    }

    /**
     * Hold a single value generated by the experiment
     */
    public static class Result {

        private final String controller;
        private final int individualsCount;
        private final int speciesCount;
        private final long duration;

        public Result(String key, int individualCount, int speciesCount, long duration) {
            this.controller = key;
            this.individualsCount = individualCount;
            this.speciesCount = speciesCount;
            this.duration = duration;
        }

        public String getController() {
            return controller;
        }

        public int getIndividualsCount() {
            return individualsCount;
        }

        public int getSpeciesCount() {
            return speciesCount;
        }

        public long getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return String.format("%s,%d,%d,%d", controller, individualsCount, speciesCount, duration);
        }
    }
}
