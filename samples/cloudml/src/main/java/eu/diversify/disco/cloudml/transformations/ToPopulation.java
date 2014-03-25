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

package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.cloudml.controller.DiversityExtraction;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.AbstractVisitListener;
import org.cloudml.core.visitors.ContainmentDispatcher;
import org.cloudml.core.visitors.Visitor;



public class ToPopulation implements DiversityExtraction<DeploymentModel> {

    @Override
    public Population applyTo(DeploymentModel deployment) {
        //FIXME: abortIfInvalidModel(deployment);
        return extract(deployment).getExtractedPopulation();
    }

    private void abortIfInvalidModel(DeploymentModel deployment) {
        DeploymentValidator validator = new DeploymentValidator();
        final Report validation = validator.validate(deployment);
        if (!validation.pass(Report.WITHOUT_WARNING)) {
            throw new IllegalArgumentException("Invalid model: " + validation.toString());
        }
    }

    private PopulationExtractor extract(DeploymentModel deployment) {
        PopulationExtractor extractor = new PopulationExtractor();
        Visitor visitor = new Visitor(new ContainmentDispatcher());
        visitor.addListeners(extractor);
        deployment.accept(visitor);
        return extractor;
    }
    
    private static class PopulationExtractor extends AbstractVisitListener {

        private final Population population;

        public PopulationExtractor() {
            this.population = new PopulationBuilder().make();
        }

        @Override
        public void onNode(Node subject) {
                population.addSpecie(subject.getName());
        }

        @Override
        public void onArtefact(Artefact subject) {
                population.addSpecie(subject.getName());
        }

        @Override
        public void onNodeInstance(NodeInstance subject) {
            final String specieName = subject.getType().getName();
            population.shiftNumberOfIndividualsIn(specieName, +1);
        }

        @Override
        public void onArtefactInstance(ArtefactInstance subject) {
            final String specieName = subject.getType().getName();
            population.shiftNumberOfIndividualsIn(specieName, +1);
        }
        
        
        public Population getExtractedPopulation() {
            return this.population;
        }
    }
}
