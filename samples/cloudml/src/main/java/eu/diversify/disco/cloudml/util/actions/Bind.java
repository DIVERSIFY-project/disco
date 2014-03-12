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

import java.util.ArrayList;
import java.util.Collection;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;

import static org.cloudml.core.builders.Commons.*;


public class Bind extends AbstractAction<Void> { 

    private final ClientPortInstance clientPort;
    
    public Bind(StandardLibrary library, ClientPortInstance clientPort) {
      super(library);
      this.clientPort = clientPort;
    }

    @Override
    public Void applyTo(DeploymentModel deployment) {
        String name = getLibrary().createUniqueBindingInstanceName(deployment);
        if (!isAlreadyBound(deployment, clientPort)) {
            Binding bindingType = getLibrary().findBindingFor(deployment, clientPort);
            ServerPortInstance serverPort = getLibrary().findServerPortFor(deployment, bindingType); 
            aBindingInstance()
                    .named(name)
                    .from(clientPort.getOwner().getName(), clientPort.getName())
                    .to(serverPort.getOwner().getName(), serverPort.getName())
                    .ofType(bindingType.getName())
                    .integrateIn(deployment);        
        }
        return NOTHING;
    }

    private boolean isAlreadyBound(DeploymentModel deployment, ClientPortInstance clientPort) {
        return !findBindingInstancesByClient(deployment, clientPort).isEmpty();
    }

    // FIXME: Should be a feature of deployment models
    private Collection<BindingInstance> findBindingInstancesByClient(DeploymentModel deployment, ClientPortInstance cpi) {
        final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (final BindingInstance bi : deployment.getBindingInstances()) {
            if (bi.getClient().equals(cpi)) {
                selection.add(bi);
            }
        }
        return selection;
    }
    
}
