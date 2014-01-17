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

package eu.diversify.disco.experiments.controllers.singlerun;

import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.Evaluation;
import eu.diversify.disco.population.Population;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Run all the control strategies against a single example and retrieve the
 * resulting trajectories.
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class Experiment {

    private final HashMap<String, Controller> controllers;
    private final HashMap<String, Evaluation> results;
    private double reference;
    private Population population;

    /**
     * Create a new runner object.
     *
     * By default, no controller are instantiated, the reference is defined to
     * 1.
     */
    public Experiment() {
        this.controllers = new HashMap<String, Controller>();
        this.results = new HashMap<String, Evaluation>();
    }

    /**
     * Add a new controller associated with a unique name
     *
     * @param name the name of the controller
     * @param controller the controller
     */
    public void addController(String name, Controller controller) {
        this.controllers.put(name, controller);
    }

    /**
     * @return the map of controllers and the name to which they are associated
     * with
     */
    public Map<String, Controller> getControllers() {
        return Collections.unmodifiableMap(this.controllers);
    }

    /**
     * @return the initial population associated with this experiment
     */
    public Population getInitialPopulation() {
        return this.population;
    }

    /**
     * Update the initial population used in the experiment
     *
     * @param population the initial population
     */
    public void setInitialPopulation(Population population) {
        this.population = population;
    }

    /**
     * @return the reference value used in the experiment
     */
    public double getReference() {
        return this.reference;
    }

    /**
     * Update the reference used in the experiment
     *
     * @param reference the reference to use
     */
    public void setReference(double reference) {
        this.reference = reference;
    }

    /**
     * Run the experiment
     */
    public void run() {
        for (String key : this.controllers.keySet()) {
            System.out.println("Running '" + key + "'");
            final Controller controller = this.controllers.get(key);
            final Evaluation eval = controller.applyTo(this.population, this.reference);
            System.out.println(eval);
            this.results.put(key, eval);
        }
    }

    /**
     * Save the results in the given CSV file.
     *
     * If the file does already exist, the existing contents are overridden.
     *
     * @param fileName the file of the name where the results should be written
     */
    public void saveResultsAs(String fileName) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File(fileName));
        out.println("controller, step, diversity");

        for (String key : this.results.keySet()) {
            Evaluation result = this.results.get(key);
            while (result.hasPrevious()) {
                out.printf("%s, %d, %2.2e\n", key, result.getIteration(), result.getDiversity());
                result = result.getPrevious();
            }
        }

        out.close();
    }
}
