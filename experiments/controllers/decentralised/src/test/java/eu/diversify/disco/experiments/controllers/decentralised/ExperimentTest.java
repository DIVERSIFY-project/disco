/*
 */
package eu.diversify.disco.experiments.controllers.decentralised;

import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import static junit.framework.TestCase.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(JUnit4.class)
public class ExperimentTest extends TestCase {

    public static final String[] OUTPUT_DATASET_NAMES = new String[]{
        "diversity", "population"
    };

    @Test
    public void testDataSetNames() {
        final DecentralisedSetup setup = defaultSetup();

        final List<DataSet> results = setup.buildExperiment().run();

        verifyDataSetNames(results);
    }

    private DecentralisedSetup defaultSetup() {
        return new DecentralisedSetup();
    }

    private void verifyDataSetNames(List<DataSet> results) {
        assertThat("wrong number of data set", results, hasSize(OUTPUT_DATASET_NAMES.length));
        for (String name : OUTPUT_DATASET_NAMES) {
            findDataSetNamed(results, name);
        }
    }

    private DataSet findDataSetNamed(Collection<DataSet> results, String name) {
        for (DataSet ds : results) {
            if (ds.getName().equals(name)) {
                return ds;
            }
        }
        fail("Missing data set '" + name + "'");
        return null;
    }

    @Test
    public void testDiversityDataSetEntryCount() {
        final int STEP_COUNT = 50;
        final int RUN_COUNT = 25;

        final DecentralisedSetup setup = defaultSetup();
        setup.setStepCount(STEP_COUNT);
        setup.setRunCount(RUN_COUNT);

        final List<DataSet> results = setup.buildExperiment().run();

        verifyDiversityLength(results, STEP_COUNT, RUN_COUNT);
    }

    private void verifyDiversityLength(List<DataSet> results, int runCount, int stepCount) {
        final DataSet diversity = findDataSetNamed(results, "diversity");
        assertThat("Wrong number of entries in 'diversity'", diversity.getSize(), is(equalTo(expectedDiversityLength(stepCount, runCount))));
    }

    private int expectedDiversityLength(int stepCount, int runCount) {
        return stepCount * runCount;
    }

    @Test
    public void testPopulationDataSetEntryCount() {
        final int STEP_COUNT = 13;
        final int RUN_COUNT = 35;
        final int POPULATION_SIZE = 45;

        final DecentralisedSetup setup = defaultSetup();
        setup.setStepCount(STEP_COUNT);
        setup.setRunCount(RUN_COUNT);
        setup.setPopulationSize(POPULATION_SIZE);

        final List<DataSet> results = setup.buildExperiment().run();

        verifyPopulationLength(results, RUN_COUNT, STEP_COUNT, POPULATION_SIZE);
    }

    private void verifyPopulationLength(List<DataSet> results, int runCount, int stepCount, int populationSize) {
        final DataSet population = findDataSetNamed(results, "population");
        assertThat("Wrong number of entries in 'population'", population.getSize(), is(equalTo(expectedPopulationLength(stepCount, runCount, populationSize))));
    }

    private int expectedPopulationLength(int stepCount, int runCount, int populationSize) {
        return stepCount * runCount * populationSize;
    }

    @Test
    public void testIndividualSpecies() {
        final int SPECIES_COUNT = 15;

        final DecentralisedSetup setup = defaultSetup();
        setup.setSpeciesCount(SPECIES_COUNT); 

        final List<DataSet> results = setup.buildExperiment().run();
        verifySpeciesAreWithin(results, SPECIES_COUNT);
    }

    private void verifySpeciesAreWithin(List<DataSet> results, int speciesCount) {
        DataSet population = findDataSetNamed(results, "population");
        for(int i=0 ; i<population.getSize() ; i++) {
            Data data = population.getData(i);
            assertThat("wrong specie value", (Integer) data.get("specie"), is(both(greaterThan(0)).and(lessThanOrEqualTo(speciesCount))));
        }
    }
}