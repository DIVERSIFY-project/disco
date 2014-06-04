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
package eu.diversify.disco.cloudml.robustness;

import junit.framework.TestCase;
import org.cloudml.core.Component;
import org.cloudml.core.Deployment;

import static org.cloudml.core.builders.Commons.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the extinction sequence simulator
 */
@RunWith(JUnit4.class)
public class SimulatorTest extends TestCase {

    @Test
    public void countAliveComponentsShouldReturnTheNumberOfComponentsBeforeAnyKilling() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("My VM")
                        .providedBy("Ec2"))
                .build();

        final Simulator simulator = new Simulator(deployment);

        assertThat(simulator.countAliveComponents(), is(equalTo(deployment.getComponents().size())));
    }

    @Test
    public void brandNewComponentsShouldNotBeKilled() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("My VM")
                        .providedBy("Ec2"))
                .build();

        final Simulator simulator = new Simulator(deployment);

        assertThat("not killed", simulator.isAlive(deployment.getComponents().firstNamed("My VM")));

    }

    @Test
    public void killedComponentsShouldBeMarkedAsKilled() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("My VM")
                        .providedBy("Ec2"))
                .build();
        final Component theVm = deployment.getComponents().firstNamed("My VM");

        final Simulator simulator = new Simulator(deployment);
        simulator.markAsDead(theVm); 
                
        assertThat("not marked as killed", simulator.isDead(theVm));
    }
    
    
    @Test
    public void killedComponentsShouldBeNotBeCountedAsAlive() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("VM #1")
                        .providedBy("Ec2"))
                .with(aVM()
                        .named("VM #2")
                        .providedBy("Ec2"))
                .build();
        final Component theVm = deployment.getComponents().firstNamed("VM #1");

        final Simulator simulator = new Simulator(deployment);
        simulator.markAsDead(theVm); 
                
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(1)));
    }
    
    
    @Test
    public void killingOneComponentShouldDecreaseTheNumberOfAliveComponents() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("VM #1")
                        .providedBy("Ec2"))
                .with(aVM()
                        .named("VM #2")
                        .providedBy("Ec2"))
                .build();
        
        final Simulator simulator = new Simulator(deployment);
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(2)));
        
        simulator.killOneComponent(); 
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(1)));
                
        simulator.killOneComponent(); 
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(0)));
    }
}
