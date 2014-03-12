
package eu.diversify.disco.cloudml.util.actions;

import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;


public class Terminate extends AbstractAction<Void> {

    private final NodeInstance instance;

    
    public Terminate(StandardLibrary library, NodeInstance instance) {
        super(library);
        this.instance = instance;
    }
      
    
    @Override
    public Void applyTo(DeploymentModel target) {
        final List<ArtefactInstance> hosted = findArtefactInstancesByDestination(target, instance);
        for(ArtefactInstance artefact: hosted) {
            getLibrary().migrate(target, artefact);
        }
        getLibrary().stop(target, instance);
        target.getNodeInstances().remove(instance);
        return NOTHING;
    }

    // FIXME: Should be part of the deployment model
    private List<ArtefactInstance> findArtefactInstancesByDestination(DeploymentModel deployment, NodeInstance instance) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance artefact: deployment.getArtefactInstances()) {
            if (artefact.getDestination().equals(instance)) {
                selection.add(artefact);
            }
        }
        return selection;
    }

    
}
