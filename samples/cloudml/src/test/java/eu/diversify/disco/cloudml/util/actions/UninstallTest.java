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
package eu.diversify.disco.cloudml.util.actions;

import static eu.diversify.disco.cloudml.util.ToolBox.countArtefactsOfType;
import static eu.diversify.disco.cloudml.util.ToolBox.countBindingsOfType;
import static eu.diversify.disco.cloudml.util.ToolBox.countNodesOfType;
import junit.framework.TestCase;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.samples.SshClientServer;
import static org.cloudml.core.samples.SshClientServer.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UninstallTest extends TestCase {

    @Test
    public void uninstallReinstallANewMissingServerForRemainingClientsIfNeeded() {
        DeploymentModel model = new SshClientServer()
                .getOneClientConnectedToOneServer()
                .build();

        ArtefactInstance artefact = model.findArtefactInstanceByName(SshClientServer.SERVER_NO_1);
        Uninstall command = new Uninstall(new StandardLibrary(), artefact);

        command.applyTo(model);

        assertThat("Linux instance count", countNodesOfType(model, LINUX_TYPE), is(equalTo(1)));
        assertThat("Windows instance count", countNodesOfType(model, WINDOWS_TYPE), is(equalTo(1)));
        assertThat("Client instance count", countArtefactsOfType(model, CLIENT_TYPE), is(equalTo(1)));
        assertThat("Server instance count", countArtefactsOfType(model, SERVER_TYPE), is(equalTo(1)));
        assertThat("SSH connection count", countBindingsOfType(model, BINDING_TYPE), is(equalTo(1)));
    }

    @Test
    public void uninstallDoesNotReinstallMissingServerForRemainingClientsWhenAlternativeExist() {
        DeploymentModel model = new SshClientServer()
                .getTwoClientsConnectedTwoServers() 
                .build();

        ArtefactInstance artefact = model.findArtefactInstanceByName(SshClientServer.SERVER_NO_1);
        Uninstall command = new Uninstall(new StandardLibrary(), artefact);

        command.applyTo(model);

        assertThat("Linux instance count", countNodesOfType(model, LINUX_TYPE), is(equalTo(3)));
        assertThat("Windows instance count", countNodesOfType(model, WINDOWS_TYPE), is(equalTo(1)));
        assertThat("Client instance count", countArtefactsOfType(model, CLIENT_TYPE), is(equalTo(2)));
        assertThat("Server instance count", countArtefactsOfType(model, SERVER_TYPE), is(equalTo(1)));
        assertThat("SSH connection count", countBindingsOfType(model, BINDING_TYPE), is(equalTo(2)));
    }
    
    
}
