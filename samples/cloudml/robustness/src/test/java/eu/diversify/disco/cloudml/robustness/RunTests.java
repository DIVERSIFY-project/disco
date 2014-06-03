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

package eu.diversify.disco.cloudml.robustness;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the Run objects
 */
@RunWith(JUnit4.class)
public class RunTests extends TestCase {

    @Test
    public void extractRobustnessShouldDetectFloatingPointValues() {
        String inputText = "Robustness: 89.23 %";
        double robustness = Run.extractRobustness(inputText);
        
        assertThat(robustness, is(closeTo(89.23, 0.01)));
    }
    
    @Test
    public void extractRobustnessShouldhandleMultiLinesText() {
        String inputText = 
                "Some header text\r\n"
                + "Robustness: 89.23 %\r\n"
                + "Some footer text";
        double robustness = Run.extractRobustness(inputText);
        
        assertThat(robustness, is(closeTo(89.23, 0.01)));
    }
    

}