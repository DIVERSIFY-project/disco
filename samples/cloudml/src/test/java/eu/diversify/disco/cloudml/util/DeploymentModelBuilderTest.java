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
package eu.diversify.disco.cloudml.util;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the Model builder
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class DeploymentModelBuilderTest extends TestCase {

    
    private final DeploymentModelBuilder builder;
    private final NamingPolicy naming;
    private DeploymentMatcher matcher;
 
    public DeploymentModelBuilderTest() {
        this.naming = new NamingPolicy();
        this.builder = new DeploymentModelBuilder(naming);
        this.matcher = new DeploymentMatcher();
        
    }

    @Ignore
    @Test
    public void testEmptyModel() {
        DeploymentModel model = builder.getResult();
        assertTrue(matcher.isEmpty(model));
    }

    @Test
    public void testNewNodeTypeWithDefaultName() {
        builder.addNodeTypeWithDefaultName();
        assertTrue(builder.getResult().getNodeTypes().containsKey(naming.nameOfNodeType(1)));
    }

    @Test
    public void testNewNodeTypeWithGivenName() {
        final String nodeTypeName = "Type Foo";
        builder.addNodeType(nodeTypeName);
        assertTrue(builder.getResult().getNodeTypes().containsKey(nodeTypeName));
    }

    @Test
    public void testNewNodeTypeAndReuseUnusedIds() {
        builder.addNodeType(naming.nameOfNodeType(1));
        builder.addNodeType(naming.nameOfNodeType(2));
        builder.addNodeTypeWithDefaultName();
        assertTrue(builder.getResult().getNodeTypes().containsKey(naming.nameOfNodeType(1)));
    }

    @Test
    public void testNewArtefactTypeWithGivenName() {
        final String artefactTypeName = "My Artefact Type";
        builder.addArtefactType(artefactTypeName);
        assertTrue(builder.getResult().getArtefactTypes().containsKey(artefactTypeName));
    }

    @Test
    public void testNewArtefactTypeWithDefaultName() {
        builder.addArtefactTypeWithDefaultName();
        assertTrue(builder.getResult().getArtefactTypes().containsKey(naming.nameOfArtefactType(1)));
    }

    @Ignore
    @Test
    public void testInstantiateNodeType() {
        Node type = builder.addNodeType("my type");
        NodeInstance instance = builder.instantiate(type, "my instance");
        assertSame(type, instance.getType());
        assertTrue(builder.getResult().getNodeInstances().contains(instance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstantiateExternalNodeType() {
        DeploymentModelBuilder otherBuilder = new DeploymentModelBuilder();
        Node externalType = otherBuilder.addNodeType("External Type");
        builder.instantiate(externalType, "my instance");
    }

    @Test
    public void testInstantiateArtefactType() {
        Artefact type = builder.addArtefactType("my type");
        ArtefactInstance instance = builder.instantiate(type, "my instance");
        assertSame(type, instance.getType());
        assertTrue(builder.getResult().getArtefactInstances().contains(instance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstantiateExternalArtefactType() {
        DeploymentModelBuilder otherBuilder = new DeploymentModelBuilder();
        Artefact externalType = otherBuilder.addArtefactType("External Type");
        builder.instantiate(externalType, "my instance");
    }

    @Test
    public void testDeploy() {
        Node nodeType = builder.addNodeTypeWithDefaultName();
        NodeInstance host = builder.instantiate(nodeType);
        Artefact artefactType = builder.addArtefactTypeWithDefaultName();
        ArtefactInstance application = builder.instantiate(artefactType);
        builder.deploy(host, application);
        assertSame(host, application.getDestination());
    }

    @Test
    public void testFindNodeType() {
        Node type = builder.addNodeTypeWithDefaultName();
        Node recovered = builder.findNodeType(1);
        assertSame(type, recovered);
    }

    @Test
    public void testFindArtefactType() {
        Artefact type = builder.addArtefactTypeWithDefaultName();
        Artefact recovered = builder.findArtefactType(1);
        assertSame(type, recovered);
    }
    // TODO: instantiate node type
    // TODO: check external types detection
    // TODO test instantiate with default names
}
