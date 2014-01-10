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


package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Check the behaviour of the result object
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class EvaluationTest extends TestCase {
    
    
    /**
     * Test the formatting of the result
     */
    @Test
    public void testFormatting() {
        final Population p = new Population();
        p.addSpecie("Lion", 5);
        p.addSpecie("Sludge", 4);
        
        final Evaluation result = new Evaluation(30, p, 0.5, 0.45, 0.025);
        final String text = result.toString();
        assertEquals(
                "Wrong formatting of evaluation",
                " - Iteration count: 30\n - Population: {Lion: 5, Sludge: 4}\n - Reference: 0.5\n - Diversity: 0.45\n - Error: 0.025\n",
                text
                );
    }
}