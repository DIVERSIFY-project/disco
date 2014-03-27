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

package eu.diversify.disco.controller.solvers;

import static eu.diversify.disco.controller.problem.ProblemBuilder.*;

import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.NormalisedDiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
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
public class BreadthFirstSearchTest extends SolverTest {

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
        Population source = aPopulation().withDistribution(5, 5).build();
        Problem problem = aProblem()
                .withInitialPopulation(source)
                .withDiversityMetric(new NormalisedDiversityMetric(new TrueDiversity()))
                .withReferenceDiversity(0.)
                .make();
        
        explorer.reset(problem.getInitialEvaluation());
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
                explorer.getExplored().contains(problem.evaluate(source)));
        
        assertEquals(
                "Pushback did not compute enough new state to add in the frontier",
                2,
                explorer.getFrontier().size());
        
        
    }
    
}
