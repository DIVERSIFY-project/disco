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
package eu.diversify.disco.samples.commons;

/*
 */
import eu.diversify.disco.controller.Reference;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DiversityControllerTest extends TestCase {

    final Mockery context;

    public DiversityControllerTest() {
        context = new JUnit4Mockery();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testInteractions() {
        final Population input = new PopulationBuilder()
                .withDistribution(1, 3, 2, 10, 3, 0)
                .make();
        final Population output = input;
        
        final DiversityMetric metric = new TrueDiversity().normalise();
        final ModelReader<Population> reader = context.mock(ModelReader.class);
        final DiversityExtraction<Population> extraction = context.mock(DiversityExtraction.class);
        final Reference reference = context.mock(Reference.class);
        final DiversityInjection<Population> injection = context.mock(DiversityInjection.class);
        final ModelWriter<Population> writer = context.mock(ModelWriter.class);
        final DiversityControllerListener listener = context.mock(DiversityControllerListener.class);
        
        final DiversityController<Population> controller = new DiversityController<Population>(metric, reader, extraction, reference, injection, writer);
        controller.addListeners(listener);
        
        context.checking(new Expectations() {{
          atLeast(1).of(reader).read(); will(returnValue(input));
          atLeast(1).of(extraction).applyTo(with(input)); will(returnValue(input));
          atLeast(1).of(reference).getReference(); will(returnValue(0.5));
          exactly(1).of(injection).applyTo(with(any(Population.class)), with(input)); will(returnValue(output));
          exactly(1).of(writer).write(with(output));
          exactly(1).of(listener).onPopulationExtracted(with(input));
          exactly(1).of(listener).onPopulationDiversified();
          exactly(1).of(listener).onDiversityInjected(with(any(Solution.class))); 
        }});
        
        controller.control();
        
    }
}