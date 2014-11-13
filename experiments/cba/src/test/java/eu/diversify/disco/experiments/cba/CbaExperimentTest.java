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
package eu.diversify.disco.experiments.cba;


import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yaml.snakeyaml.Yaml;

@RunWith(JUnit4.class)
public class CbaExperimentTest {

    public static final String TEST_RESOURCES_PATH = "../src/test/resources/";
    public static final String TEST_SETUP = "test_setup.yml";
    public static final String SAMPLE_DEPLOYMENT_MODEL = "sensappAdmin.json";
    public static final String CREDENTIALS = "credentials";

    @Before
    public void setupInputFiles() throws IOException {
        Files.copy(
                Paths.get(TEST_RESOURCES_PATH + SAMPLE_DEPLOYMENT_MODEL),
                Paths.get("./" + SAMPLE_DEPLOYMENT_MODEL));

        Files.copy(
                Paths.get(TEST_RESOURCES_PATH + CREDENTIALS),
                Paths.get("./" + CREDENTIALS));
    }

    @Test
    public void testExperiment() throws FileNotFoundException {
        CbaSetup setup = makeCbaSetup();
        Experiment experiment = setup.buildExperiment();
        List<DataSet> results = experiment.run();
        checkResults(setup, results);
    }
    
    
    @After
    public void tearDownTestFiles() {
        File deploymentModel = new File("./" + SAMPLE_DEPLOYMENT_MODEL);
        deploymentModel.delete();
        File credentials = new File("./" + CREDENTIALS);
        credentials.delete();
    }

    private CbaSetup makeCbaSetup() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        CbaSetup setup = (CbaSetup) yaml.loadAs(new FileInputStream(TEST_RESOURCES_PATH + TEST_SETUP), CbaSetup.class);
        return setup;
    }

    private void checkResults(CbaSetup setup, List<DataSet> results) {
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("Wrong number of resulting data sets", 1, results.size());
        DataSet result = results.get(0);
        assertEquals(
                "Wrong number of data",
                setup.getSampleCount() * setup.getDiversityLevels().size(),
                result.getSize());
        for (int i = 0; i < result.getSize(); i++) {
            final Data data = result.getData(i);
            assertFalse(data.isMissing(CbaExperiment.RUN));
            assertFalse(data.isMissing(CbaExperiment.EXPECTED_DIVERSITY));
            assertFalse(data.isMissing(CbaExperiment.ACTUAL_DIVERSITY));
            assertFalse(data.isMissing(CbaExperiment.COST));
            assertFalse(data.isMissing(CbaExperiment.ROBUSTNESS));
        }
    }
}
