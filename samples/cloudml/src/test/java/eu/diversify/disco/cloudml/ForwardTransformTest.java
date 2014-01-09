/**
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
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.population.Population;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ForwardTransformTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ForwardTransformTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ForwardTransformTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testForwardTrans()
    {
        CloudML model = new CloudML();
        Runner.initFakeModel(model);
        Transformation transformation = new Transformation();
        
        Population population = transformation.forward(model);
        
        assertEquals(3, population.getSpecies().size());
        
        assertEquals(5,population.getSpecie("huge").getIndividualCount());
    }
}
