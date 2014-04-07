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

    private final Schema diversitySchema;
    private final Schema populationSchema;

    public DecentralisedExperiment(DecentralisedSetup setup) {
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
        final DataSet diversity = diversitySchema.newDataSet("diversity");
        
        addDiversityRecord(diversity, 1, 1, 0.50);
        addDiversityRecord(diversity, 1, 2, 0.25);
        addDiversityRecord(diversity, 2, 1, 0.45);
        addDiversityRecord(diversity, 2, 2, 0.20);
        
        results.add(diversity);

        final DataSet population = populationSchema.newDataSet("population");
        addPopulationRecord(population, 1, 1, 10.0, 15.0, 1);
        addPopulationRecord(population, 1, 1, 12.0, 12.0, 2);
        addPopulationRecord(population, 1, 2, 10.0, 14.0, 2);
        addPopulationRecord(population, 1, 2, 12.0, 12.0, 2);
        addPopulationRecord(population, 2, 1, 10.0, 15.0, 1);
        addPopulationRecord(population, 2, 1, 10.0, 10.0, 2);
        addPopulationRecord(population, 2, 2, 10.0, 15.0, 1);
        addPopulationRecord(population, 2, 2, 12.0, 12.0, 2);
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