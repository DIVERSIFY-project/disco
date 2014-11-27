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
package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.samples.commons.DiversityExtraction;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import java.util.HashMap;
import java.util.Map;
import org.cloudml.core.*;

import static eu.diversify.disco.population.PopulationBuilder.*;

import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.AbstractVisitListener;
import org.cloudml.core.visitors.Visitor;

/**
 * Traverse a given deployment model a generate the associated Population model.
 */
public class ToPopulation implements DiversityExtraction<Deployment> {

    @Override
    public Population applyTo(Deployment deployment) {
        abortIfInvalidModel(deployment);
        return extract(deployment).getExtractedPopulation();
    }

    private void abortIfInvalidModel(Deployment deployment) {
        final Report validation = new DeploymentValidator().validate(deployment);
        if (!validation.pass(Report.WITHOUT_WARNING)) {
            throw new IllegalArgumentException("Invalid model: " + validation.toString());
        }
    }

    private PopulationExtractor extract(Deployment deployment) {
        final PopulationExtractor extractor = new PopulationExtractor();
        deployment.accept(new Visitor(extractor));
        return extractor;
    }

    /**
     * Actual deployment visitor object. It traverses a deployment model and
     * incrementally build the associated deployment model.
     */
    private static class PopulationExtractor extends AbstractVisitListener {

        private final PopulationBuilder population;
        private final Map<String, Integer> distribution;

        public PopulationExtractor() {
            this.distribution = new HashMap<String, Integer>();
            this.population = aPopulation()
                    .withFixedNumberOfIndividuals()
                    .withFixedNumberOfSpecies();
        }

        @Override
        public void onVMExit(VM subject) {
            checkForConstraints(subject);
            createSpecie(subject);
        }

        /**
         * Create a specie for the given subject
         * @param subject the subject that needs an associated specie
         */
        private void createSpecie(Component subject) {
            assert subject != null: 
                    "Unable to create a specie for 'null'";
            assert !distribution.containsKey(subject.getName()): 
                    "There is already a specie associated with component type '" + subject.getName() + "'";

            distribution.put(subject.getName(), 0);
        }

        /**
         * Check whether some additional constraints are specified in the
         * properties of the given CloudML component. Constraints can be either
         * 'at_least' or 'at_most' multiplicity.
         *
         * @param subject the component which could be subject to some
         * constraints
         * @throws NumberFormatException if the bound associated with the
         * constraints cannot be parsed
         */
        private void checkForConstraints(Component subject) throws NumberFormatException {
            assert subject != null: 
                    "Unable to check constraints of 'null'";
            
            final String specieName = subject.getName();

            if (subject.hasProperty(AT_LEAST)) {
                int limit = Integer.parseInt(subject.getProperties().valueOf(AT_LEAST));
                population.withAtLeast(limit, specieName);
            }
            
            if (subject.hasProperty(AT_MOST)) {
                int limit = Integer.parseInt(subject.getProperties().valueOf(AT_MOST));
                population.withAtMost(limit, specieName);
            }
            
        }
        private static final String AT_MOST = "at_most";

        private static final String AT_LEAST = "at_least";

        @Override
        public void onInternalComponentExit(InternalComponent subject) {
            checkForConstraints(subject);
            createSpecie(subject);
        }

        @Override
        public void onExternalComponentExit(ExternalComponent subject) {
            checkForConstraints(subject);
            createSpecie(subject);
        }

        @Override
        public void onInternalComponentInstanceExit(InternalComponentInstance subject) {
            incrementHeadCountFor(subject.getType().getName());
        }

        private void incrementHeadCountFor(final String specieName) {
            assert distribution.containsKey(specieName):
                    "Must have already process '" + specieName + "'";
            distribution.put(specieName, distribution.get(specieName) + 1);
        }

        @Override
        public void onExternalComponentInstanceExit(ExternalComponentInstance subject) {
            incrementHeadCountFor(subject.getType().getName());
        }

        @Override
        public void onVMInstanceExit(VMInstance subject) {
            incrementHeadCountFor(subject.getType().getName());
        }

        /**
         * @return the population associated with the last visited model
         */
        public Population getExtractedPopulation() {
            return this.population.fromMap(distribution).build();
        }
    }
}
