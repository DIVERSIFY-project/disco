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

import java.util.List;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;


public class Unbind extends AbstractAction<Void> {

    private final ArtefactPortInstance<? extends ArtefactPort> port;

    public Unbind(StandardLibrary library, ArtefactPortInstance<? extends ArtefactPort> port) {
        super(library);
        this.port = port;
    }

    @Override
    public Void applyTo(DeploymentModel target) {
        List<BindingInstance> bindings = findBindingInstancesByPort(port);
        for(BindingInstance binding: bindings) {
            target.getBindingInstances().remove(binding);
        }
        return NOTHING;
    }  

    private List<BindingInstance> findBindingInstancesByPort(ArtefactPortInstance<? extends ArtefactPort> port) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
