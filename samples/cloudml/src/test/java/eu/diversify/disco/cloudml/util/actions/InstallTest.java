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
package eu.diversify.disco.cloudml.util.actions;

import junit.framework.TestCase;
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static eu.diversify.disco.cloudml.matchers.Commons.*;
import static eu.diversify.disco.cloudml.util.ToolBox.countArtefactsOfType;
import static eu.diversify.disco.cloudml.util.ToolBox.countBindingsOfType;
import org.cloudml.core.samples.SshClientServer;
import static org.cloudml.core.samples.SshClientServer.BINDING_TYPE;
import static org.cloudml.core.samples.SshClientServer.CLIENT_TYPE;
import static org.cloudml.core.samples.SshClientServer.SERVER_TYPE;

public class InstallTest extends TestCase {

    @Test
    public void testInstall() {
        final DeploymentModel model = new SshClientServer()
                .getTypes()
                .build();

        final Artefact artefact = model.findArtefactByName(SshClientServer.CLIENT_TYPE);


        new Install(new StandardLibrary(), artefact).applyTo(model);

        assertThat("Node instance count", model.getNodeInstances().size(), is(equalTo(1)));
        assertThat("Client instance count", countArtefactsOfType(model, CLIENT_TYPE), is(equalTo(1)));
        assertThat("Server instance count", countArtefactsOfType(model, SERVER_TYPE), is(equalTo(1)));
        assertThat("SSH connection count", countBindingsOfType(model, BINDING_TYPE), is(equalTo(1)));

        assertThat("valid output", model, is(valid()));
    }
}