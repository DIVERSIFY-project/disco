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
        ArtefactInstance ai = transformation.provision(
                model.getArtefactTypes().get("SensAppGUIWar"), "noname");
        Collection<Binding> bd = transformation.fixBinding(model, ai);

        assertTrue(bd.isEmpty());
        assertEquals(6, model.getBindingInstances().size());
    }

    public DeploymentModel initWithSenseApp() {
        JsonCodec jsonCodec = new JsonCodec();
        DeploymentModel root = null;
        try {
            root = (DeploymentModel) jsonCodec.load(new FileInputStream(
                    "../src/main/resources/sensappAdmin.json"));
            for (NodeInstance ni : root.getNodeInstances()) {
                ni.getProperties().add(new Property("state", "onn"));
            }
            for (ArtefactInstance ai : root.getArtefactInstances()) {
                ai.getProperties().add(new Property("state", "onn"));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null,
                                                            ex);
        }

        return root;
    }
}