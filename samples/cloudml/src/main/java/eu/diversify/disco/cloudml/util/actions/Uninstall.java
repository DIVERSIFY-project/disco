
package eu.diversify.disco.cloudml.util.actions;

import java.util.ArrayList;
import java.util.Iterator;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;

public class Uninstall extends AbstractAction<Void> {

    private final ArtefactInstance artefactInstance;

    public Uninstall(StandardLibrary library, ArtefactInstance artefactInstance) {
        super(library);
        this.artefactInstance = artefactInstance;
    }

    @Override
    public Void applyTo(DeploymentModel target) {
        disconnectRequiredPorts(target);
        ArrayList<ClientPortInstance> pendingClients = disconnectProvidedPorts(target);
        shutdownArtefactInstance(target);
        reconnectClientsWithAlternativeServer(pendingClients, target);
        return NOTHING;
    }

    private void disconnectRequiredPorts(DeploymentModel target) {
        for (ClientPortInstance clientPort : artefactInstance.getRequired()) {
            if (isBound(target, clientPort)) {
                final ArtefactInstance server = findServerPort(target, clientPort).getOwner();
                getLibrary().unbind(target, clientPort);
                if (!isUsed(target, server)) {
                    getLibrary().uninstall(target, server);
                }
            }
        }
    }

    // FIXME: Should be part of deployment model
    private boolean isUsed(DeploymentModel deployment, ArtefactInstance server) {
        boolean found = false;
        final Iterator<ServerPortInstance> iterator = server.getProvided().iterator();
        while (iterator.hasNext() && !found) {
            final ServerPortInstance port = iterator.next();
            if (isBound(deployment, port)) {
                found = true;
            }
        }
        return found;
    }

    // FIXME: Should be part of deployment model
    private ServerPortInstance findServerPort(DeploymentModel target, ClientPortInstance clientPort) {
        BindingInstance binding = null;
        final Iterator<BindingInstance> iterator = target.getBindingInstances().iterator();
        while (iterator.hasNext() && binding == null) {
            final BindingInstance instance = iterator.next();
            if (instance.getClient().equals(clientPort)) {
                binding = instance;
            }
        }
        if (binding == null) {
            abortAsUnboundPortShouldHaveAlreadyBeenFiltered(clientPort);
        }
        return binding.getServer();
    }

    private void abortAsUnboundPortShouldHaveAlreadyBeenFiltered(ArtefactPortInstance<? extends ArtefactPort> clientPort) throws IllegalStateException {
        final String message = String.format(
                "the client port '%s::%s' is not bound!",
                clientPort.getOwner().getName(),
                clientPort.getName());
        throw new IllegalStateException(message);
    }

    
    // Should be part of the deployment model
    private ClientPortInstance findClientPort(DeploymentModel target, ServerPortInstance serverPort) {
        BindingInstance binding = null;
        final Iterator<BindingInstance> iterator = target.getBindingInstances().iterator();
        while (iterator.hasNext() && binding == null) {
            final BindingInstance instance = iterator.next();
            if (instance.getServer().equals(serverPort)) {
                binding = instance;
            }
        }
        if (binding == null) {
            abortAsUnboundPortShouldHaveAlreadyBeenFiltered(serverPort);
        }
        return binding.getClient();
    }

    private ArrayList<ClientPortInstance> disconnectProvidedPorts(DeploymentModel target) {
        ArrayList<ClientPortInstance> customerToBeRebound = new ArrayList<ClientPortInstance>();
        for (ServerPortInstance serverPort : artefactInstance.getProvided()) {
            customerToBeRebound.add(findClientPort(target, serverPort));
            getLibrary().unbind(target, serverPort);
        }
        return customerToBeRebound;
    }

    private void reconnectClientsWithAlternativeServer(ArrayList<ClientPortInstance> customerToBeRebound, DeploymentModel target) {
        for (ClientPortInstance clientPort : customerToBeRebound) {
            getLibrary().bind(target, clientPort);
        }
    }

    private void shutdownArtefactInstance(DeploymentModel target) {
        getLibrary().stop(target, artefactInstance);
        target.getArtefactInstances().remove(artefactInstance);
    }

    // FIXME: Should be part of the deployment model
    private boolean isBound(DeploymentModel deployment, ArtefactPortInstance<? extends ArtefactPort> port) {
        boolean bound = false;
        final Iterator<BindingInstance> iterator = deployment.getBindingInstances().iterator();
        while (iterator.hasNext() && !bound) {
            final BindingInstance instance = iterator.next();
            if (instance.getClient().equals(port) || instance.getServer().equals(port)) {
                bound = true;
            }
        }
        return bound;
    }
}
