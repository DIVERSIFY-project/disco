package eu.diversify.disco.cloudml.util.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;

public class FindServerPortInstance extends AbstractAction<ServerPortInstance> {

    private final Binding bindingType;

    public FindServerPortInstance(StandardLibrary library, Binding bindingType) {
        super(library);
        this.bindingType = bindingType;
    }

    @Override
    public final ServerPortInstance applyTo(DeploymentModel deployment) {
        final List<ServerPortInstance> candidates = collectCandidates(deployment);
        if (candidates.isEmpty()) {
            final Artefact artefact = getLibrary().findArtefactTypeProviding(deployment, bindingType);
            final ArtefactInstance instance = getLibrary().install(deployment, artefact);
            final ServerPortInstance serverPort = instance.findProvidedPortByName(bindingType.getServer().getName());
            candidates.add(serverPort);
        }
        return chooseOne(candidates);
    }

    private ArrayList<ServerPortInstance> collectCandidates(DeploymentModel deployment) {
        ArrayList<ServerPortInstance> candidates = new ArrayList<ServerPortInstance>();
        for (ArtefactInstance artefactInstance : deployment.getArtefactInstances()) {
            for (ServerPortInstance serverPort : artefactInstance.getProvided()) {
                if (isCandidate(bindingType, serverPort)) {
                    candidates.add(serverPort);
                }
            }
        }
        return candidates;
    }

    private boolean isCandidate(Binding bindingType, ServerPortInstance serverPort) {
        return bindingType.getServer().equals(serverPort.getType());
    }

    protected ServerPortInstance chooseOne(List<ServerPortInstance> candidates) {
        final int index = new Random().nextInt(candidates.size());
        return candidates.get(index);
    }
}
