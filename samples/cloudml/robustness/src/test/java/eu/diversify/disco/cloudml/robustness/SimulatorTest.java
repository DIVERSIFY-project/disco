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
package eu.diversify.disco.cloudml.robustness;

import junit.framework.TestCase;
import org.cloudml.core.Component;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.VM;

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
    public void aliveComponentsShouldIncludeInternalOnes() {
        final Deployment deployment = Samples.anAppOnAVm().build();

        final Simulator simulator = new Simulator(deployment);

        assertThat(simulator.countAliveComponents(), is(equalTo(2)));
    }

    @Test
    public void internalComponentShouldBeAlive() {
        final String appName = "app";
        final Deployment deployment = Samples.anAppOnAVm(appName, "vm").build();

        final InternalComponent theApp = deployment.getComponents().onlyInternals().firstNamed(appName);
        final Simulator simulator = new Simulator(deployment);

        assertThat("dead!", simulator.isAlive(theApp));
    }
    
    @Test
    public void freshInternalComponentsShouldBeReadyToStart() {
        final String appName = "app";
        final String vmName = "vm";
        final Deployment deployment = Samples.anAppOnAVm(appName, vmName).build();
        
        final InternalComponent theApp = deployment.getComponents().onlyInternals().firstNamed(appName);
        final VM vm = deployment.getComponents().onlyVMs().firstNamed(vmName);
        final Simulator simulator = new Simulator(deployment);

        assertThat("dead!", simulator.isAlive(theApp));
        assertThat("cannot be hosted by the VM", vm.canHost(theApp));
        assertThat("cannot be provisioned!", simulator.canBeProvisioned(theApp));
        assertThat("cannot start!", simulator.canStillBeStarted(theApp));
    }

    @Test
    public void allComponentsShouldBeAliveAtFirst() {
        final Deployment deployment = Samples.aSingleVM().build();

        final Simulator simulator = new Simulator(deployment);

        assertThat(simulator.countAliveComponents(), is(equalTo(deployment.getComponents().size())));
    }

    @Test
    public void brandNewComponentsShouldBeAlive() {
        final String vmName = "My VM";

        final Deployment deployment = Samples.aSingleVM(vmName).build();
        final Simulator simulator = new Simulator(deployment);
        final Component vm = deployment.getComponents().firstNamed(vmName);

        assertThat("alive", simulator.isAlive(vm));
    }

    @Test
    public void killedComponentsShouldBeDead() {
        final String vmName = "My VM";
        final Deployment deployment = Samples.aSingleVM(vmName).build();

        final Component vm = deployment.getComponents().firstNamed(vmName);

        final Simulator simulator = new Simulator(deployment);
        simulator.markAsDead(vm);

        assertThat("not marked as killed", simulator.isDead(vm));
    }

    @Test
    public void deadComponentsShouldNotBeCountedAsAlive() {
        final String vmName = "VM #1";
        final Deployment deployment = Samples.twoIndependentVMs(vmName, "any name").build();

        final Component theVm = deployment.getComponents().firstNamed(vmName);

        final Simulator simulator = new Simulator(deployment);
        simulator.markAsDead(theVm);

        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(1)));
    }

    @Test
    public void killingOneComponentShouldDecreaseTheNumberOfAliveComponents() {
        final Deployment deployment = Samples.twoIndependentVMs().build();

        final Simulator simulator = new Simulator(deployment);
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(2)));

        simulator.killOneComponent();
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(1)));

        simulator.killOneComponent();
        assertThat("alive components", simulator.countAliveComponents(), is(equalTo(0)));
    }
}
