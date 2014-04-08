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
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Schema;
import java.util.List;

import static eu.diversify.disco.experiments.commons.data.SchemaBuilder.*;
import java.util.ArrayList;

/**
 * The decentralised experiment
 */
public class DecentralisedExperiment implements Experiment {

    public static final String FIELD_RUN = "run";
    public static final String FIELD_STEP = "step";
    public static final String FIELD_ERROR = "error";
    public static final String FIELD_X = "x";
    public static final String FIELD_Y = "y";
    public static final String FIELD_SPECIE = "specie";
    private final DecentralisedSetup setup;
    private final Schema diversitySchema;
    private final Schema populationSchema;

    DecentralisedExperiment(DecentralisedSetup setup) {
        this.setup = setup;
        diversitySchema = aSchema()
                .with(aField().named("run").ofType(INTEGER))
                .with(aField().named("step").ofType(INTEGER))
                .with(aField().named("error").ofType(DOUBLE))
                .build();

        populationSchema = aSchema()
                .with(aField().named("run").ofType(INTEGER))
                .with(aField().named("step").ofType(INTEGER))
                .with(aField().named("x").ofType(DOUBLE))
                .with(aField().named("y").ofType(DOUBLE))
                .with(aField().named("specie").ofType(INTEGER))
                .build();
    }

    @Override
    public List<DataSet> run() {
        final ArrayList<DataSet> results = new ArrayList<DataSet>();

        final DataSet population = populationSchema.newDataSet("population");
        final DataSet diversity = diversitySchema.newDataSet("diversity");
        for (int r = 0; r < setup.getRunCount(); r++) {
            final Run run = new Run(setup);
            for (int s = 0; s < setup.getStepCount(); s++) {
                run.oneStep();
                addDiversityRecord(diversity, r, s, run.getError());
                for (int i=0 ; i < setup.getPopulationSize() ; i++) {
                    Individual individual = run.getIndividuals().get(i);
                    addPopulationRecord(population, r, s, individual.getPosition().getX(), individual.getPosition().getY(), individual.getSpecie());
                }
            }
        }

        results.add(diversity);
        results.add(population);

        return results;
    }

    private void addDiversityRecord(DataSet diversity, int run, int step, double error) {
        Data data = diversity.newData();
        data.set(FIELD_RUN, run);
        data.set(FIELD_STEP, step);
        data.set(FIELD_ERROR, error);
        diversity.add(data);
    }

    private void addPopulationRecord(final DataSet population, int run, int step, double x, double y, int specie) {
        Data data = population.newData();
        data.set(FIELD_RUN, run);
        data.set(FIELD_STEP, step);
        data.set(FIELD_X, x);
        data.set(FIELD_Y, y);
        data.set(FIELD_SPECIE, specie);
        population.add(data);
    }
}
