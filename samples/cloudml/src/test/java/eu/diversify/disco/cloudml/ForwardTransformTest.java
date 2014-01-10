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

import com.google.common.base.Predicate;
import static com.google.common.collect.Collections2.filter;
import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.population.Population;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.jxpath.JXPathContext;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;

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
    
    public void testLoadSenseApp(){
        CloudML model = new CloudML();
        
        initWithSenseApp(model);
        Transformation transformation = new Transformation();  
        assertEquals(2, model.getRoot().getNodeTypes().size());
        
        Population population = transformation.forward(model);
        
        assertEquals(6, population.getSpecies().size());
    }
    
    public void testProvision(){
        CloudML model = new CloudML();
        
        initWithSenseApp(model);
        
        Transformation transformation = new Transformation();
        
       
        
        ArtefactInstance ai = transformation.provision(model.root.getArtefactTypes().get("SensAppGUIWar"), "noname");
        //ai = transformation.provision(model.root.getArtefactTypes().get("SensAppGUIWar"), "noname2");
        Collection<Binding> bd = transformation.fixBinding(model.getRoot(), ai);
        
        assertTrue(bd.isEmpty());
        
        //should be a new binding instance created
        assertEquals(6, model.getRoot().getBindingInstances().size());
        
    }
    
    public void testWithMDMS(){
        
        CloudML model = new CloudML();
        initWithMDMS(model);
        
        
        
        Transformation transformation = new Transformation();
        
        Population population = transformation.forward(model);
        population.getSpecie("RingoJS").setIndividualCount(3);
        
        transformation.backward(model, population);
        
        ArtefactInstance ai = filter(model.getRoot().getArtefactInstances(), new Predicate<ArtefactInstance>(){

            public boolean apply(ArtefactInstance t) {
                return t.getName().startsWith("RingoJS");
            }
        }).iterator().next();
        
        assertEquals("ec2_mdms", ai.getDestination().getName());
        
        assertEquals(10, model.getRoot().getBindingInstances().size());
        
        
        
    }
    
    public void initWithSenseApp(CloudML model){
        
        JsonCodec jsonCodec = new JsonCodec();
        DeploymentModel root = null;
        try {
            root = (DeploymentModel) jsonCodec.load(new FileInputStream("sensappAdmin.json"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ForwardTransformTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(NodeInstance ni : root.getNodeInstances()){
            ni.getProperties().add(new Property("state","onn"));
        }
        for(ArtefactInstance ai : root.getArtefactInstances()){
            ai.getProperties().add(new Property("state","onn"));
        }
        
        model.setRoot(root);
    }
    
    public void initWithMDMS(CloudML model){
        
        
        MdmsModelCreator creator = new MdmsModelCreator();
        
        DeploymentModel dm = creator.create();
        
        model.setRoot(dm);
        
        
        
        
    }

    public <T extends Object> T parseMe(final String xpath, final Object context) {
        JXPathContext _newContext = JXPathContext.newContext(context);
        Object _value = _newContext.getValue(xpath);
        return ((T) _value);
    }
}
