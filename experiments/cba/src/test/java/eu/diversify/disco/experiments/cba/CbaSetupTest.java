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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yaml.snakeyaml.Yaml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



/**
 * Test the Cost Benefits Analysis experiment (CBA)
 */
@RunWith(JUnit4.class)
public class CbaSetupTest {
    
    private static final int SAMPLE_COUNT = 1;
    private static final String[] DEPLOYMENT_MODEL = new String[]{"./sensappAdmin.json"};
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
        assertThat(setup.getSampleCount(), is(equalTo(SAMPLE_COUNT)));
        assertThat(setup.getControlStrategy(), is(equalTo(CONTROL_STRATEGY)));
        assertThat(setup.getDiversityMetric(), is(equalTo(DIVERSITY_METRIC)));
        assertThat(setup.getDiversityLevels(), contains(DIVERSITY_LEVELS));
        assertThat(setup.getDeploymentModels(), containsInAnyOrder(DEPLOYMENT_MODEL));
    }

}