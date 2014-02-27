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

package eu.diversify.disco.experiments.commons;

import eu.diversify.disco.experiments.commons.Runner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the usage of the Runner class.
 * 
 * In this settings, each experiment comes with a R script, used to plot thee
 * resulting data and produce the final PDF files.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class RunnerIT extends TestCase {
  
    @Before
    public void setUp() throws IOException {
        Files.copy(
                Paths.get("../src/test/resources/test.yml"), 
                Paths.get("./test.yml"), 
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(
                Paths.get("../src/test/resources/view.r"), 
                Paths.get("./view.r"), 
                StandardCopyOption.REPLACE_EXISTING);
    }
    
    @Test
    public void testScenarioUsage() throws FileNotFoundException, IOException, InterruptedException {
        final Runner runner = new Runner();
        runner.run(SetupMock.class, "test.yml");
        
        File csvFile = new File("test.csv");
        assertTrue(
                "no CSV file produced by the experiment",
                csvFile.exists()
                );
        
        File pdfFile = new File("test.pdf");
        assertTrue(
                "No PDF file produced by the experiment",
                pdfFile.exists()
                );
        
    }
    
    @After
    public void removePdfAndCsvFiles() throws IOException {
        Files.deleteIfExists(Paths.get("test.pdf"));
        Files.deleteIfExists(Paths.get("test.csv"));
    }
    
}
