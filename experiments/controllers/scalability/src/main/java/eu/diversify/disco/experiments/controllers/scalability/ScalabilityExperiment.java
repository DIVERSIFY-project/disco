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

package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.controller.solvers.Solver;
import eu.diversify.disco.controller.solvers.SolverFactory;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Evaluate the sensitivity of the response time against an increase of the
 * number of specie and/or an increase in the number of individuals in the
 * population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ScalabilityExperiment implements Experiment {

    // TODO: This experiment should be multi-threaded to reduce the running time
    private static final Field STRATEGY = new Field("strategy", String.class);
    private static final Field INDIVIDUALS_COUNT = new Field("individual count", Integer.class);
    private static final Field SPECIES_COUNT = new Field("species count", Integer.class);
    private static final Field DURATION = new Field("duration", Long.class);
    private static final Field ERROR = new Field("error", Double.class);
    private static final Schema SCHEMA = new Schema(Arrays.asList(new Field[]{
        STRATEGY,
        INDIVIDUALS_COUNT,
        SPECIES_COUNT,
        DURATION,
        ERROR}), "n/a");
    private final HashMap<String, Solver> controllers;
    private final ArrayList<Integer> speciesCounts;
    private final ArrayList<Integer> individualsCounts;

    /**
     * Create a new experiment from a setup file
     *
     * @param setupFile the file containing the configuration
     */
    public ScalabilityExperiment(ScalabilitySetup setup) {
        this.controllers = new HashMap<String, Solver>();
        this.individualsCounts = new ArrayList<Integer>();
        this.speciesCounts = new ArrayList<Integer>();

        // Load the configuration file
        this.setIndividualsCount(setup.getIndividualsCounts());
        this.setSpeciesCounts(setup.getSpeciesCounts());
        final SolverFactory factory = new SolverFactory();
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
    private void addController(String key, Solver controller) {
        this.controllers.put(key, controller);
    }

    /**
     * Set the numbers of individuals to use during the experiment
     *
     * @param counts the list of individuals count used in this experiments
     */
    private void setIndividualsCount(List<Integer> counts) {
        this.individualsCounts.clear();
        for (int c : counts) {
            this.individualsCounts.add(c);
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
        ArrayList<DataSet> results = new ArrayList<DataSet>();
        displayDurationWarning();
        results.add(runExperiment());
        return results;
    }

    private DataSet runExperiment() {
        DataSet dataset = new DataSet(SCHEMA);
        for (Integer speciesCount : speciesCounts) {
            for (Integer individualsCount : individualsCounts) {
                System.out.println("Scale [" + speciesCount + " x " + individualsCount + "]: ");
                Population population = initialisePopulation(individualsCount, speciesCount);
                for (String key : this.controllers.keySet()) {
                    dataset.add(runControlStrategy(key, population));
                }
            }
        }
        return dataset;
    }

    private Population initialisePopulation(int numberOfIndividuals, int numberOfSpecies) {
        final Population population = aPopulation()
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .build();
        population.addSpecie("sp1");
        population.setNumberOfIndividualsIn(1, numberOfIndividuals);
        for (int i = 2; i <= numberOfSpecies; i++) {
            String name = String.format("sp%d", i);
            population.addSpecie(name);
            population.setNumberOfIndividualsIn(i, 0);
        }
        return population;
    }

    private Data runControlStrategy(String key, final Population population) {
        final Solver controller = this.controllers.get(key);
        final Problem problem = aProblem()
                .withInitialPopulation(population)
                .withDiversityMetric(new TrueDiversity().normalise())
                .withReferenceDiversity(1.0)
                .build();
        final long start = System.currentTimeMillis();
        final Solution solution = controller.solve(problem);
        final long duration = System.currentTimeMillis() - start;
        Data data = logPopulationDetails(key, population, solution, duration);
        return data;
    }

    private Data logPopulationDetails(String key, final Population population, final Solution solution, final long duration) {
        Data data = SCHEMA.newData();
        data.set(STRATEGY, key);
        data.set(SPECIES_COUNT, population.getNumberOfSpecies());
        data.set(INDIVIDUALS_COUNT, population.getTotalNumberOfIndividuals());
        data.set(ERROR, solution.getError());
        data.set(DURATION, duration);
        System.out.println("\t - " + key + " in " + duration + " ms");
        return data;
    }

    private void displayDurationWarning() {
        int total = this.speciesCounts.size() * this.individualsCounts.size() * this.controllers.size();
        System.out.println("Preparing for " + total + " run(s).");
        System.out.println("This may take several minutes ... ");
    }
}
