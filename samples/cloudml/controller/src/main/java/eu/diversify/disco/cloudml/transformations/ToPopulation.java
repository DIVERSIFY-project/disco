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

        private final Population population;

        public PopulationExtractor() {
            this.population = aPopulation()
                    .withFixedNumberOfIndividuals()
                    .withFixedNumberOfSpecies()
                    .build();
        }

        @Override
        public void onVMExit(VM subject) {
            population.addSpecie(subject.getName());
        }

        @Override
        public void onInternalComponentExit(InternalComponent subject) {
            population.addSpecie(subject.getName());
        }

        @Override
        public void onExternalComponentExit(ExternalComponent subject) {
            population.addSpecie(subject.getName());
        }

        @Override
        public void onInternalComponentInstanceExit(InternalComponentInstance subject) {
            final String specieName = subject.getType().getName();
            population.getSpecie(specieName).shiftHeadcountBy(+1);
        }

        @Override
        public void onExternalComponentInstanceExit(ExternalComponentInstance subject) {
            final String specieName = subject.getType().getName();
            population.getSpecie(specieName).shiftHeadcountBy(+1);
        }

        @Override
        public void onVMInstanceExit(VMInstance subject) {
            final String specieName = subject.getType().getName();
            population.getSpecie(specieName).shiftHeadcountBy(+1);
        }

        /**
         * @return the population associated with the last visited model
         */
        public Population getExtractedPopulation() {
            return this.population;
        }
    }
}
