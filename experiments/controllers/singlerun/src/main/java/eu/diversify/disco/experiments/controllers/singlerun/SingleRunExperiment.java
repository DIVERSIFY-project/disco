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
package eu.diversify.disco.experiments.controllers.singlerun;

import eu.diversify.disco.controller.solvers.Solver;
import eu.diversify.disco.controller.solvers.SolverFactory;
import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.controller.problem.Problem;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.controller.solvers.SolverListener;
import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import static eu.diversify.disco.experiments.commons.data.SchemaBuilder.*;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Run all the control strategies against a single example and retrieve the
 * resulting trajectories.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class SingleRunExperiment implements Experiment {

    private static final Field ITERATION = new Field("time", Integer.class);
    private static final Field DIVERSITY = new Field("diversity", Double.class);
    private static final Field REFERENCE = new Field("reference", Double.class);
    private static final Field ERROR = new Field("error", Double.class);
    private static final Field STRATEGY = new Field("strategy", String.class);
    private final HashMap<String, Solver> controllers;
    private final DataSet result;
    private double reference;
    private Population population;

    /**
     * Create a new experiment object.
     *
     * By default, no controller are instantiated, the reference is defined to
     * 1.
     */
    public SingleRunExperiment() {
        this.controllers = new HashMap<String, Solver>();
        this.result = prepareResult();
    }

    /**
     * Build an experiment whose setting is read from the given input stream
     *
     * @param input a input stream where one can read the configuration of the
     * experiment
     */
    public SingleRunExperiment(SingleRunSetup setup) {
        initialisePopulation(setup);

        this.reference = setup.getReference();

        this.controllers = new HashMap<String, Solver>();
        initialiseControlStrategies(setup);

        this.result = prepareResult();

    }

    private void initialisePopulation(SingleRunSetup setup) {
        this.population = aPopulation().build();
        final List<Integer> counts = setup.getPopulation();
        for (int i = 0; i < counts.size(); i++) {
            this.population.addSpecie("s" + (i + 1));
            this.population.setNumberOfIndividualsIn(i + 1, counts.get(i));
        }
    }

    private void initialiseControlStrategies(SingleRunSetup setup) throws IllegalArgumentException {
        SolverFactory factory = new SolverFactory();
        for (String strategy : setup.getStrategies()) {
            this.controllers.put(strategy, factory.instantiate(strategy));
        }
    }

    /**
     * Create the dataset which contain the results
     *
     * @return the dataset
     */
    private DataSet prepareResult() {
        Schema schema = new Schema(Arrays.asList(new Field[]{ITERATION,
                                                             DIVERSITY,
                                                             REFERENCE, ERROR,
                                                             STRATEGY}), "n/a");
        return new DataSet(schema);
    }

    /**
     * Add a new controller associated with a unique name
     *
     * @param name the name of the controller
     * @param controller the controller
     */
    public void addController(String name, IterativeSearch controller) {
        this.controllers.put(name, controller);
    }

    /**
     * @return the map of controllers and the name to which they are associated
     * with
     */
    public Map<String, Solver> getControllers() {
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
    public List<DataSet> run() {

        for (final String controlStrategy : this.controllers.keySet()) {
            System.out.println("Running '" + controlStrategy + "'");
            final Solver controller = this.controllers.get(controlStrategy);
            final Problem problem = aProblem()
                    .withInitialPopulation(this.population)
                    .withDiversityMetric(new TrueDiversity().normalise())
                    .withReferenceDiversity(this.reference)
                    .build();
            controller.subscribe(new SolverListener() {
                private int counter = 0;

                @Override
                public void onInitialSolution(Solution solution) {
                    recordControllerTrajectory(solution, controlStrategy, counter++);
                }

                @Override
                public void onIntermediateSolution(Solution solution) {
                    recordControllerTrajectory(solution, controlStrategy, counter++);
                }

                @Override
                public void onFinalSolution(Solution solution) {
                    recordControllerTrajectory(solution, controlStrategy, counter++);
                }
            });

            controller.solve(problem);
        }

        ArrayList<DataSet> output = new ArrayList<DataSet>();
        output.add(this.result);
        return output;
    }

    /**
     * @return the result obtained from running the experiment
     */
    public DataSet getResults() {
        return this.result;
    }

    private void recordControllerTrajectory(final Solution solution, String key, int iteration) {
        Data data = result.getSchema().newData();
        data.set(ITERATION, iteration);
        data.set(DIVERSITY, solution.getDiversity());
        data.set(REFERENCE, solution.getReference());
        data.set(ERROR, solution.getError());
        data.set(STRATEGY, key);
        this.result.add(data);
    }
}
