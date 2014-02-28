/*
 */
package eu.diversify.disco.cloudml.transformations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the conversion from a Population Model to a CloudML
 * model
 *
 * @author Hui Song
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ToCloudMLTest extends TestCase {

    @Test
    public void testProvision() {
        BidirectionalTransformation transformation = new BidirectionalTransformation();

        DeploymentModel model = initWithSenseApp();
        ArtefactInstance ai = transformation.provision(model.getArtefactTypes().get("SensAppGUIWar"), "noname");
        Collection<Binding> bd = transformation.fixBinding(model, ai);

        assertTrue(bd.isEmpty());
        assertEquals(6, model.getBindingInstances().size());
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
}