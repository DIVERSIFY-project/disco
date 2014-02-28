/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.cloudml.transformations;

import com.google.common.base.Predicate;
import eu.diversify.disco.cloudml.Runner;
import static com.google.common.collect.Collections2.filter;
import eu.diversify.disco.population.Population;
import junit.framework.TestCase;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ForwardTransformTest extends TestCase {

    @Test
    public void testForwardTrans() {
        DeploymentModel model = new DeploymentModel();
        Runner.initFakeModel(model);
        BidirectionalTransformation transformation = new BidirectionalTransformation();

        Population population = transformation.toPopulation(model);

        assertEquals(3, population.getNumberOfSpecies());
        assertEquals(5, population.getNumberOfIndividualsIn("huge"));
    }

   

    @Test
    public void testWithMDMS() {

        DeploymentModel model = initWithMDMS();



        BidirectionalTransformation transformation = new BidirectionalTransformation();

        Population population = transformation.toPopulation(model);
        int index = population.getSpecieIndex("RingoJS");
        population.setNumberOfIndividualsIn(index, 3);

        transformation.toCloudML(model, population);

        ArtefactInstance ai = filter(model.getArtefactInstances(), new Predicate<ArtefactInstance>() {
            public boolean apply(ArtefactInstance t) {
                return t.getName().startsWith("RingoJS");
            }
        }).iterator().next();

        assertEquals("ec2_mdms", ai.getDestination().getName());

        assertEquals(10, model.getBindingInstances().size());



    }

    public DeploymentModel initWithMDMS() {
        MdmsModelCreator creator = new MdmsModelCreator();
        return creator.create();
    }

//    public <T extends Object> T parseMe(final String xpath, final Object context) {
//        JXPathContext _newContext = JXPathContext.newContext(context);
//        Object _value = _newContext.getValue(xpath);
//        return ((T) _value);
//    }
}
