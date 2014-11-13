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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;



/**
 * Test the Cost Benefits Analysis experiment (CBA)
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class CbaSetupTest extends TestCase {
    
    private static final int SAMPLE_COUNT = 1;
    private static final String DEPLOYMENT_MODEL = "./sensappAdmin.json";
    private static final String CONTROL_STRATEGY = "Adaptive Hill Climbing";
    private static final String DIVERSITY_METRIC = "Shannon Index";
    private static final Double[] DIVERSITY_LEVELS = new Double[] { 0., 0.25, 0.5, 0.75, 1. };
    
    @Test
    public void testLoadSetupFromYaml() throws FileNotFoundException {
        CbaSetup setup = loadSetupFromYaml("../src/test/resources/test_setup.yml");
        checkSetup(setup); 
    }
  
    private CbaSetup loadSetupFromYaml(String pathToFile) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        CbaSetup setup = (CbaSetup) yaml.loadAs(new FileInputStream(pathToFile), CbaSetup.class);
        return setup;
    }

    private void checkSetup(CbaSetup setup) {
        assertEquals(SAMPLE_COUNT, setup.getSampleCount());
        assertEquals(CONTROL_STRATEGY, setup.getControlStrategy());
        assertEquals(DIVERSITY_METRIC, setup.getDiversityMetric());
        assertEquals(Arrays.asList(DIVERSITY_LEVELS), setup.getDiversityLevels());
        assertEquals(DEPLOYMENT_MODEL, setup.getDeploymentModel());
    }

}