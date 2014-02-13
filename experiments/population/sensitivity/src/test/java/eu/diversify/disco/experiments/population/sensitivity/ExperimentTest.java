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
package eu.diversify.disco.experiments.population.sensitivity;

import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.population.diversity.GiniSimpsonIndex;
import eu.diversify.disco.population.diversity.TrueDiversity;
import eu.diversify.disco.population.diversity.ShannonIndex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the sensitivity analysis
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ExperimentTest extends TestCase {

    /**
     * Test the behaviour of the experiment
     */
    @Test
    public void testSensitivityAnalysis() throws FileNotFoundException {
        SensitivitySetup setup = new SensitivitySetup();
        setup.setSize(100);
        setup.setMetrics(Arrays.asList(new String[]{
            "True Diversity (theta=2)",
            "Shannon Index",
            "Gini Simpson Index"
        }));

        SensitivityExperiment experiment = new SensitivityExperiment(setup);
        List<DataSet> results = experiment.run();
        assertEquals(
                "Missing data sets",
                3,
                results.size());
        
        for (DataSet ds: results) {
            check(ds, 100, 3);
        }
    }

    private void check(DataSet ds, int size, int metricCount) {
        if (ds.getName().equals("individuals")) {
            assertEquals(
                    "Missing data points in individuals",
                    size * metricCount,
                    ds.getSize());
        } else if (ds.getName().equals("species")) {
            assertEquals(
                    "Missing data points in the species",
                    (size - 1) * metricCount,
                    ds.getSize());
        } else if (ds.getName().equals("distribution")) {
            assertEquals(
                    "Missing data points in the distribution",
                    (size / 2) * metricCount,
                    ds.getSize());
        } else {
            fail("Unknown result dataset " + ds.getName());
        }
    }
}
