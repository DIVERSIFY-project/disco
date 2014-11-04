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

import eu.diversify.disco.samples.commons.DiversityInjection;
import eu.diversify.disco.population.Population;
import java.util.List;
import org.cloudml.core.*;
import org.cloudml.core.actions.StandardLibrary;

/**
 * Update a CloudML model so as it reflects a given population, as much as
 * possible.
 */
public class ToCloudML implements DiversityInjection<Deployment> {

    private final StandardLibrary deployer;

    public ToCloudML() {
        deployer = new StandardLibrary();
    }

    @Override
    public Deployment applyTo(Population prescription, Deployment currentModel) {
        return applyTo(currentModel, prescription);
    }

    public Deployment applyTo(Deployment deployment, Population prescription) {
        abortIfInvalid(deployment);
        abortIfInvalid(prescription);
        // TODO: Check if the model and the population are consistent?

        for (InternalComponent each: deployment.getComponents().onlyInternals()) {
            adjustInternalComponentInstanceCount(
                    deployment,
                    each,
                    prescription.getSpecie(each.getName()).getHeadcount()
            );
        }
        
        
        for (VM each: deployment.getComponents().onlyVMs()) {
            adjustVmInstanceCount(
                    deployment,
                    each,
                    prescription.getSpecie(each.getName()).getHeadcount()
            );
        }

        return deployment;
    }

    private void abortIfInvalid(Deployment deployment) {
        if (deployment == null) {
            throw new IllegalArgumentException("Cannot convert 'null' as input deployment model");
        }
    }

    private void abortIfInvalid(Population toBe) {
        if (toBe == null) {
            throw new IllegalArgumentException("Cannot exploit 'null' as a target population configuration");
        }
    }

    /**
     * Adjust the number of instance of the given type of VM to the given count
     *
     * @param deployment the deployment model where VM instance must be created
     * or deleted
     * @param vmType the type of VM instances, whose instance count must be
     * adjusted
     * @param desiredCount the desired number of instance of that type
     */
    private void adjustVmInstanceCount(Deployment deployment, final VM vmType, int desiredCount) {
        final List<VMInstance> existings = deployment
                .getComponentInstances()
                .onlyVMs()
                .ofType(vmType.getName())
                .toList();

        final int error = desiredCount - existings.size();
        if (error < 0) {
            removeExcessiveVmInstances(deployment, existings, Math.abs(error));

        } else if (error > 0) {
            addMissingVmInstances(deployment, error, vmType);

        }
    }

    /**
     * Add the given number of new instances of a given VMType.
     *
     * @param deployment the deployment model where the new instance shall be
     * created
     * @param missingCount the number of missing instances
     * @param vmType the type of VM that shall be instantiated
     */
    private void addMissingVmInstances(Deployment deployment, int missingCount, VM vmType) {
        for (int i = 0; i < missingCount; i++) {
            deployer.provision(deployment, vmType);
        }
    }

    /**
     * Remove a given number of instance, from a given set of candidates
     *
     * @param deployment the CloudML deployment model, where the instances have
     * to be taken.
     * @param candidates the list of existing VM, candidates for removal
     * @param excessCount the number of VM to remove
     */
    private void removeExcessiveVmInstances(Deployment deployment, List<VMInstance> candidates, int excessCount) {
        final int count = Math.min(excessCount, candidates.size());
        for (int i = 0; i < count; i++) {
            deployer.terminate(deployment, candidates.get(i));
        }
    }

    /**
     * Adjust the number of instance of a given type of internal component, so
     * that is match the desired count.
     *
     * @param deployment the deployment model to be adjusted
     * @param componentType the type of internal component, whose number of
     * instance must be balanced
     * @param desiredCount the desired number of instance
     */
    private void adjustInternalComponentInstanceCount(Deployment deployment, InternalComponent componentType, int desiredCount) {
        final List<InternalComponentInstance> existings = deployment
                .getComponentInstances()
                .onlyInternals()
                .ofType(componentType.getName())
                .toList();

        final int error = desiredCount - existings.size();
        if (error < 0) {
            removeExcessiveInternalComponentInstances(deployment, existings, Math.abs(error));

        } else if (error > 0) {
            addMissingInternalComponentInstances(deployment, error, componentType);

        }
    }

    /**
     * Add new instances of the given internal component type.
     *
     * @param deployment the deployment model where the new instances must be
     * added
     * @param missingCount the number of missing instances
     * @param componentType the type of internal component to instantiate
     */
    private void addMissingInternalComponentInstances(Deployment deployment, int missingCount, InternalComponent componentType) {
        for (int i = 0; i < missingCount; i++) {
            deployer.provision(deployment, componentType);
        }
    }

    /**
     * Remove existing instances, from the given set of candidates.
     *
     * @param deployment the deployment model where the candidates belong
     * @param candidates the given set instances, candidates for removal
     * @param excessCount the number of instances to remove
     */
    private void removeExcessiveInternalComponentInstances(Deployment deployment, List<InternalComponentInstance> candidates, int excessCount) {
        final int bound = Math.min(excessCount, candidates.size());
        for (int i = 0; i < bound; i++) {
            deployer.uninstall(deployment, candidates.get(i));
        }
    }

}
