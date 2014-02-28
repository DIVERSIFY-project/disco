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
package eu.diversify.disco.cloudml;

import com.google.common.base.Predicate;
import static com.google.common.collect.Collections2.filter;
import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.population.Population;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathContext;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ForwardTransformTest extends TestCase {

    @Test
    public void testWithEmptyCloudMlModel() {
        DeploymentModel model = new DeploymentModel();
        Transformation transformation = new Transformation();
    }

    @Test
    public void testForwardTrans() {
        DeploymentModel model = new DeploymentModel();
        Runner.initFakeModel(model);
        Transformation transformation = new Transformation();

        Population population = transformation.forward(model);

        assertEquals(3, population.getNumberOfSpecies());
        assertEquals(5, population.getNumberOfIndividualsIn("huge"));
    }

    @Test
    public void testLoadSenseApp() {
        Transformation transformation = new Transformation();
        DeploymentModel model = initWithSenseApp();

        assertEquals(2, model.getNodeTypes().size());
        Population population = transformation.forward(model);

        assertEquals(6, population.getNumberOfSpecies());
    }

    @Test
    public void testProvision() {
        Transformation transformation = new Transformation();
       
        DeploymentModel model = initWithSenseApp();
        ArtefactInstance ai = transformation.provision(model.getArtefactTypes().get("SensAppGUIWar"), "noname");
        Collection<Binding> bd = transformation.fixBinding(model, ai);

        assertTrue(bd.isEmpty());
        assertEquals(6, model.getBindingInstances().size());

    }

    @Test
    public void testWithMDMS() {

        DeploymentModel model = initWithMDMS();



        Transformation transformation = new Transformation();

        Population population = transformation.forward(model);
        int index = population.getSpecieIndex("RingoJS");
        population.setNumberOfIndividualsIn(index, 3);

        transformation.backward(model, population);

        ArtefactInstance ai = filter(model.getArtefactInstances(), new Predicate<ArtefactInstance>() {
            public boolean apply(ArtefactInstance t) {
                return t.getName().startsWith("RingoJS");
            }
        }).iterator().next();

        assertEquals("ec2_mdms", ai.getDestination().getName());

        assertEquals(10, model.getBindingInstances().size());



    }

    public DeploymentModel initWithSenseApp() {
        JsonCodec jsonCodec = new JsonCodec();
        DeploymentModel root = null;
        try {
            root = (DeploymentModel) jsonCodec.load(new FileInputStream("../src/main/resources/sensappAdmin.json"));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ForwardTransformTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (NodeInstance ni : root.getNodeInstances()) {
            ni.getProperties().add(new Property("state", "onn"));
        }
        for (ArtefactInstance ai : root.getArtefactInstances()) {
            ai.getProperties().add(new Property("state", "onn"));
        }
        return root;
    }

    public DeploymentModel initWithMDMS() {
        MdmsModelCreator creator = new MdmsModelCreator();
        return creator.create();
    }

    public <T extends Object> T parseMe(final String xpath, final Object context) {
        JXPathContext _newContext = JXPathContext.newContext(context);
        Object _value = _newContext.getValue(xpath);
        return ((T) _value);
    }
}
