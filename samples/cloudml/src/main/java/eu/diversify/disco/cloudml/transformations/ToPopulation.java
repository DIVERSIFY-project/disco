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
package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

/**
 * Convert a CloudML deployment model into a population
 *
 * @author Franck Chauvel
 * @author Hui Song
 *
 * @since 0.1
 */
public class ToPopulation {

    public Population applyTo(DeploymentModel model) {
        abortIfInvalid(model);
        Population population = new PopulationBuilder().make();
        convertNodeTypes(model, population);
        convertArtefactTypes(model, population);
        convertNodeInstances(model, population);
        convertArtefactInstances(model, population);
        return population;
    }

    private void convertNodeTypes(DeploymentModel model, Population population) {
        for (Node nodeType : model.getNodeTypes().values()) {
            abortIfInvalid(nodeType);
            population.addSpecie(nodeType.getName());
        }
    }

    private void convertArtefactTypes(DeploymentModel model, Population population) {
        for (Artefact artefactType : model.getArtefactTypes().values()) {
            abortIfInvalid(artefactType);
            population.addSpecie(artefactType.getName());
        }
    }

    private void convertNodeInstances(DeploymentModel model, Population population) {
        for (NodeInstance node : model.getNodeInstances()) {
            abortIfInvalid(node);
            population.shiftNumberOfIndividualsIn(node.getType().getName(), +1);
        }
    }

    private void convertArtefactInstances(DeploymentModel model, Population population) {
        for (ArtefactInstance artefact : model.getArtefactInstances()) {
            abortIfInvalid(artefact);
            population.shiftNumberOfIndividualsIn(artefact.getType().getName(), +1);
        }
    }

    private void abortIfInvalid(DeploymentModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Cannot convert null!");
        }
    }

    private void abortIfInvalid(Node nodeType) {
        if (nodeType == null) {
            String message = String.format("Illformed CloudML model: null node type!");
            throw new IllegalArgumentException(message);
        }
        if (nodeType.getName().equals("")) {
            String message = String.format("Illformed CloudML model: node type has an empty name ''");
            throw new IllegalArgumentException(message);
        }
    }

    private void abortIfInvalid(Artefact artefactType) {
        if (artefactType == null) {
            throw new IllegalArgumentException("Illformed CloudML model: null artefact type!");
        }
        if (artefactType.getName().equals("")) {
            String message = String.format("Illformed CloudML model: artefact type has an empty name ''");
            throw new IllegalArgumentException(message);
        }
    }

    private void abortIfInvalid(ArtefactInstance artefact) {
        if (artefact.getType() == null) {
            String message = String.format("Illformed CloudML model: the artefact '%s' has no type!", artefact.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void abortIfInvalid(NodeInstance node) {
        if (node.getType() == null) {
            String message = String.format("Illformed CloudML model: the node '%s' has no type!", node.getName());
            throw new IllegalArgumentException(message);
        }
    }
}
