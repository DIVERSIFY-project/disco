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
package eu.diversify.disco.cloudml.util.actions;

import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.cloudml.core.builders.Commons.*;

@RunWith(JUnit4.class)
public class NamingStrategyTest extends TestCase {

    private final NamingStrategy naming;

    public NamingStrategyTest() {
        this.naming = new NamingStrategy();
    }

    @Test
    public void testMaxId() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named("EC2"))
                .withNodeType(aNode()
                .named("Node Type 1")
                .providedBy("EC2"))
                .withNodeInstance(aNodeInstance()
                .named("node instance #1")
                .ofType("Node Type 1"))
                .withNodeInstance(aNodeInstance()
                .named("node instance #2")
                .ofType("Node Type 1"))
                .build();

        String name = naming.createUniqueNodeInstanceName(model);
        assertThat("wrong id", name, containsString("#3"));
    }

    @Test
    public void testMinId() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named("EC2"))
                .withNodeType(aNode()
                .named("Node Type 1")
                .providedBy("EC2"))
                .build();

        String name = naming.createUniqueNodeInstanceName(model);
        assertThat("wrong id", name, containsString("#1"));
    }
    
    
     @Test
    public void testReuseUnusedId() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named("EC2"))
                .withNodeType(aNode()
                .named("Node Type 1")
                .providedBy("EC2"))
                .withNodeInstance(aNodeInstance()
                .named("node instance #1")
                .ofType("Node Type 1"))
                .withNodeInstance(aNodeInstance()
                .named("node instance #3")
                .ofType("Node Type 1"))
                .build();

        String name = naming.createUniqueNodeInstanceName(model);
        assertThat("wrong id", name, containsString("#2"));
    }
}