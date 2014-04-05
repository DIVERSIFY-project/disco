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
package eu.diversify.disco.controller.problem;

import eu.diversify.disco.population.Population;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.AddSpecie;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import eu.diversify.disco.population.diversity.ShannonIndex;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of the ProblemBuilder class
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ProblemBuilderTest extends TestCase {


    @Test
    public void testBasicProblem() {
        Population population = aPopulation()
                .withDistribution(3, 4, 5, 6)
                .build();
        
        Problem problem = aProblem()
                .withInitialPopulation(population)
                .withReferenceDiversity(0.75)
                .withDiversityMetric(new ShannonIndex())
                .build();
        
        assertEquals(population, problem.getInitialPopulation());
        assertEquals(0.75, problem.getReference());
        assertEquals(new ShannonIndex(), problem.getMetric());
        assertTrue(problem.isLegal(new ShiftNumberOfIndividualsIn(1, +3)));
        assertTrue(problem.isLegal(new AddSpecie("Elephants")));
    }
 

    @Test
    public void testWithFixedTotalNumberOfIndividuals() {
        
        Problem problem = aProblem()
                .withInitialPopulation(aPopulation()
                    .withDistribution(3, 4, 5, 6)
                    .withFixedNumberOfIndividuals())
                .withReferenceDiversity(0.56)
                .withDiversityMetric(new ShannonIndex())
                .build();
        
        final ShiftNumberOfIndividualsIn shiftNumberOfIndividualsIn = new ShiftNumberOfIndividualsIn(1, +3);
        
        assertThat("legal action", problem.isLegal(shiftNumberOfIndividualsIn));
    }

    @Test
    public void testWithFixedNumberOfSpecies() {
        
        Problem problem = aProblem()
                .withInitialPopulation(aPopulation()
                    .withDistribution(3, 4, 5, 6)
                    .withFixedNumberOfSpecies())
                .withReferenceDiversity(0.56)
                .withDiversityMetric(new ShannonIndex())
                .build();
 
        final AddSpecie addSpecie = new AddSpecie("Elephants");
 
        assertThat("legal action", problem.isLegal(addSpecie));
        
     }
}
