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
import org.cloudml.core.DeploymentModel;

/**
 * Hold the various examples used to test the ToCloudML transformation
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public enum ToCloudMLExample {
    
    // TODO: inconsistency between the population and the deployment model (exists in terms of species)
    
    WITH_NULL_DEPLOYMENT(
            "with 'null' as input deployment model",
            null,
            new PopulationBuilder().make(),
            null,
            IllegalArgumentException.class),
    
    WITH_NULL_POPULATION(
            "with 'null' as input population",
            new DeploymentModel(),
            null,
            null,
            IllegalArgumentException.class
            );
    
    private final String name;
    private final DeploymentModel inputDeployment;
    private final Population inputPopulation;
    private final DeploymentModel expected;
    private final Class expectedException;
    
    private ToCloudMLExample(String name, DeploymentModel inputModel, Population inputPopulation, DeploymentModel expected) {
        this.name = name;
        this.inputDeployment = inputModel;
        this.inputPopulation = inputPopulation;
        this.expected = expected;
        this.expectedException = null;
    }
    
    private ToCloudMLExample(String name, DeploymentModel inputModel, Population inputPopulation, DeploymentModel expected, Class expectedException) {
        this.name = name;
        this.inputDeployment = inputModel;
        this.inputPopulation = inputPopulation;
        this.expected = expected;
        this.expectedException = expectedException;
    }
        
    public boolean shallRaiseAnException() {
        return this.expectedException != null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public DeploymentModel getInputDeployment() {
        return this.inputDeployment;
    }
    
    public Population getInputPopulation() {
        return this.inputPopulation;
    }
    
    public DeploymentModel getExpected() {
        return this.expected;
    }
    
    public Class getExpectedException() {
        if (!shallRaiseAnException()) {
            String message = String.format("Example '%s' shall not raise any exception!", this.name);
            throw new IllegalStateException(message);
        }
        return this.expectedException;
    }
    
    boolean shallRaise(Class<? extends Exception> aClass) {
        return shallRaiseAnException() && this.expectedException.equals(aClass);
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    public Object[] toArray() {
        return new Object[]{ this };
    }


    
    
}
