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
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the implementation of the Breadth-first search strategy.
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class BreadthFirstSearchTest extends ControllerTest {

    @Override
    public IterativeSearch factory() {
        return new BreadthFirstExplorer();
    }
    
    
    /**
     * Check the behaviour of pushing back the frontier
     */
    @Test
    public void testPushback() {
        final BreadthFirstExplorer explorer = new BreadthFirstExplorer();
        
        final Population p1 = new Population();
        p1.addSpecie("sp1", 5);
        p1.addSpecie("sp2", 5);
                
        Problem c1 = new Problem(p1, 0.25, new TrueDiversity());

        
        explorer.reset(c1.evaluate(p1));
        assertTrue(
                "Frontier not set up properly",
                !explorer.getFrontier().isEmpty()
                );
        assertTrue(
                "Explored set not set up properly",
                explorer.getExplored().isEmpty()
                );
        
        explorer.pushback();
        
        assertEquals(
                "Pushback did not augment properly the set of explored populations",
                1,
                explorer.getExplored().size());
        
        assertTrue(
                "Pushback did not augment the set of explored populations",
                explorer.getExplored().contains(c1.evaluate(p1)));
        
        assertEquals(
                "Pushback did not compute enough new state to add in the frontier",
                2,
                explorer.getFrontier().size());
        
        
    }
    
}
