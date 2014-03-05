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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

/**
 * Help to build deployment models
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class DeploymentModelBuilder {
    
    private final DeploymentModel result;
    private final NamingPolicy naming;
    
    public DeploymentModelBuilder() {
        this.result = new DeploymentModel();
        this.naming = new NamingPolicy();
                
    }
    
    public DeploymentModelBuilder(NamingPolicy naming) {
        this.result = new DeploymentModel();
        this.naming = naming;
    }
    
    public DeploymentModelBuilder(DeploymentModel toExtend) {
        this.result = toExtend;
        this.naming = new NamingPolicy();
    }
    
    public DeploymentModelBuilder(DeploymentModel toExtend, NamingPolicy naming) {
        this.result = toExtend;
        this.naming = naming;
    }

    public DeploymentModel getResult() {
        return result;
    }

    public Node addNodeType(String name) {
        Node nodeType = new Node(name);
        result.getNodeTypes().put(name, nodeType);
        return nodeType;
    }

    public Node addNodeTypeWithDefaultName() {
        return addNodeType(naming.defaultNodeTypeName(result.getNodeTypes().keySet()));
    }

    public Artefact addArtefactType(String artefactTypeName) {
        Artefact artefactType = new Artefact(artefactTypeName);
        result.getArtefactTypes().put(artefactTypeName, artefactType);
        return artefactType;
    }

    public Artefact addArtefactTypeWithDefaultName() {
        return addArtefactType(naming.defaultArtefactTypeName(result.getArtefactTypes().keySet()));
    }

    public NodeInstance instantiate(Node type, String instanceName) {
        abortIfInvalidType(type);
        abortIfInvalidName(instanceName);
        final NodeInstance instance = type.instanciates(instanceName);
        result.getNodeInstances().add(instance);
        return instance;
    }

    public NodeInstance instantiate(Node type) {
        return instantiate(type, naming.defaultNodeInstanceName(nodeInstancesName()));
    }

    private void abortIfInvalidType(Node type) {
        if (type == null) {
            throw new IllegalArgumentException("Node type shall not be null");
        }
        if (isExternal(type)) {
            final String message = String.format("Node type '%s' is not part of the model under construction", type.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void abortIfInvalidName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot use null as a name!");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Cannot use empty name!");
        }
    }

    public ArtefactInstance instantiate(Artefact type, String instanceName) {
        abortIfInvalidType(type);
        abortIfInvalidName(instanceName);
        final ArtefactInstance instance = type.instanciates(instanceName);
        result.getArtefactInstances().add(instance);
        return instance;
    }

    public ArtefactInstance instantiate(Artefact type) {
        return instantiate(type, naming.defaultArtefactInstanceName(artefactInstancesName()));
    }

    public void deploy(NodeInstance host, ArtefactInstance application) {
        abortIfInvalidNodeInstance(host);
        abortIfInvalidArtefactInstance(application);
        application.setDestination(host);
    }
    
    private void abortIfInvalidNodeInstance(NodeInstance host) {
        if (host == null) {
            throw new IllegalArgumentException("null given as node instance!");
        }
        if (isExternal(host)) {
            final String message = String.format("Node instance '%s' is not part of the model under construction", host.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void abortIfInvalidArtefactInstance(ArtefactInstance application) {
        if (application == null) {
            final String message = "null given as artefact instance";
            throw new IllegalArgumentException(message);
        }
        if (isExternal(application)) {
            final String message = String.format("The artefact instance '%s' is not part of the model under construction", application.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private boolean isExternal(NodeInstance nodeInstance) {
        // FIXME: return !result.getNodeInstances().contains(nodeInstance);
        boolean found = false;
        Iterator<NodeInstance> iterator = result.getNodeInstances().iterator();
        while (!found && iterator.hasNext()) {
            NodeInstance instance = iterator.next();
            if (instance.getName().equals(nodeInstance.getName())) {
                found = true;
            }
        }
        return !found;
    }

    private boolean isExternal(ArtefactInstance artefactInstance) {
        // FIXME: return !result.getArtefactInstances().contains(artefactInstance);
        boolean found = false;
        Iterator<ArtefactInstance> iterator = result.getArtefactInstances().iterator();
        while (!found && iterator.hasNext()) {
            ArtefactInstance instance = iterator.next();
            if (instance.getName().equals(artefactInstance.getName())) {
                found = true;
            }
        }
        return !found;
    }

    private boolean isExternal(Node type) {
        return !result.getNodeTypes().containsKey(type.getName());
    }

    private boolean isExternal(Artefact type) {
        return !result.getArtefactTypes().containsKey(type.getName());
    }

    private Collection<String> artefactInstancesName() {
        ArrayList<String> names = new ArrayList<String>();
        for (ArtefactInstance instance : result.getArtefactInstances()) {
            names.add(instance.getName());
        }
        return names;
    }

    private Collection<String> nodeInstancesName() {
        ArrayList<String> names = new ArrayList<String>();
        for (NodeInstance instance : result.getNodeInstances()) {
            names.add(instance.getName());
        }
        return names;
    }

    private void abortIfInvalidType(Artefact type) {
        if (type == null) {
            throw new IllegalArgumentException("Artefact type shall not be null");
        }
        if (isExternal(type)) {
            final String message = String.format("Artefact type '%s' is not part of the model under construction", type.getName());
            throw new IllegalArgumentException(message);
        }
    }

    public Node findNodeType(int index) {
        Node found = null;
        Iterator<Node> iterator = result.getNodeTypes().values().iterator();
        while(iterator.hasNext() && found == null) {
            Node node = iterator.next();
            if (node.getName().equals(naming.nameOfNodeType(index))) {
                found = node;
            }
        }
        return found;
    }

    public Artefact findArtefactType(int index) {
        Artefact found = null;
        Iterator<Artefact> iterator = result.getArtefactTypes().values().iterator();
        while (iterator.hasNext() && found == null) {
            Artefact type = iterator.next();
            if (type.getName().equals(naming.nameOfArtefactType(index))) {
                found = type;
            }
        }
        return found;
    }
}
