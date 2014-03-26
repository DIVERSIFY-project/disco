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

import eu.diversify.disco.controller.solvers.AdaptiveHillClimber;
import eu.diversify.disco.controller.solvers.HillClimber;
import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.population.Population;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yaml.snakeyaml.Yaml;

/**
 * Run the various control strategies on a single case and collect their
 * trajectory.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ExperimentTest extends TestCase {
    
    @Test
    public void testLoadingSetup() throws FileNotFoundException, ControllerInstantiationException {
        final String testSetup = "../src/test/resources/setup.yml";
                
        Yaml yaml = new Yaml();
        SingleRunSetup setup = (SingleRunSetup) yaml.loadAs(new FileInputStream(testSetup), SingleRunSetup.class);
        
        SingleRunExperiment experiment = new SingleRunExperiment(setup);
        List<DataSet> results = experiment.run(); 
        assertTrue("Wrong number of results", results.get(0).getSize() > 0);
    }

}
