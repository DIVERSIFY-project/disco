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

import eu.diversify.disco.controller.solvers.Solver;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.diversity.TrueDiversity;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ControllerTest extends TestCase {

    final Mockery context;

    public ControllerTest() {
        this.context = new JUnit4Mockery();
    }
   
    @Test
    public void testControl() {
        
        final Population input = aPopulation()
                .withDistribution(5, 4, 3, 2, 1)
                .build();
        
        final ProblemBuilder builder = aProblem()
                .withDiversityMetric(new TrueDiversity().normalise());
        final double reference = 0.5;
        
        final Solution output = builder
                .withInitialPopulation(input)
                .withReferenceDiversity(reference)
                .build()
                .getInitialEvaluation();
        
        final Reference user = context.mock(Reference.class);
        final PopulationReader reader = context.mock(PopulationReader.class);
        final PopulationWriter writer = context.mock(PopulationWriter.class);
        final Solver strategy = context.mock(Solver.class);
        
        final Controller facade = new Controller(builder, user, reader, strategy, writer);
        
        context.checking(new Expectations() {{
            exactly(1).of(user).getReference(); will(returnValue(reference));
            exactly(1).of(reader).read(); will(returnValue(input));
            exactly(1).of(strategy).solve(with(any(Problem.class))); will(returnValue(output)); 
            exactly(1).of(writer).write(with(any(Solution.class)));
        }});
        
        facade.control();
    }

}