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


package eu.diversify.disco.cloudml.robustness;

import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Specification of user acceptance tests
 */
@RunWith(JUnit4.class)
public class AcceptanceIT extends TestCase {

    
    @Test 
    public void robustnessShouldBeOneForASingleVM() throws IOException, InterruptedException {
        String fileName = createModelWithASingleVM();
        
        Run run = Run.withArguments(fileName);
        
        run.noErrorShouldBeReported();
        run.displayedRobustnessShouldBe(100D);
        run.extinctionSequenceShouldBeAvailableIn("extinction_sequence.csv");
        
        run.deleteGeneratedFiles();
    }

    private String createModelWithASingleVM() {
        return "test.json";
    }
    

}