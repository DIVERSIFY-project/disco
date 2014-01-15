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

package eu.diversify.disco.experiments.population.sensitivity;

import eu.diversify.disco.population.diversity.GiniSimpsonIndex;
import eu.diversify.disco.population.diversity.TrueDiversity;
import eu.diversify.disco.population.diversity.ShannonIndex;
import java.io.File;
import java.io.FileNotFoundException;
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
    public void testSensitivityToIndividuals() throws FileNotFoundException {
        Individuals experiment = new Individuals();
        
        experiment.addMetric("Quadratic Mean", new TrueDiversity());
        experiment.addMetric("Shannon Index", new ShannonIndex());
        experiment.addMetric("Gini-Simpson Index", new GiniSimpsonIndex());
        
        experiment.setPopulationSize(100);
        
        experiment.run();
        assertEquals(
                "Wrong number of results",
                100 * 3 * 2,
                experiment.getResults().size());
        
        experiment.saveResultAs("individuals.csv");
        File result = new File("individuals.csv");
        assertTrue(
                "No file produced",
                result.exists()
                );
    }
    
    
    @Test
    public void testSpeciesCountSensitivity() throws FileNotFoundException {
        Species experiment = new Species();
        
        experiment.addMetric("Quadratic Mean", new TrueDiversity());
        experiment.addMetric("Shannon Index", new ShannonIndex());
        experiment.addMetric("Gini-Simpson Index", new GiniSimpsonIndex());
        
        experiment.setPopulationSize(100);
        
        experiment.run();
        assertEquals(
                "Wrong number of results",
                99 * 3 * 2,
                experiment.getResults().size());
        
        experiment.saveResultAs("species.csv");
        File result = new File("species.csv");
        assertTrue(
                "No file produced",
                result.exists()
                );
    }
    
    
}
