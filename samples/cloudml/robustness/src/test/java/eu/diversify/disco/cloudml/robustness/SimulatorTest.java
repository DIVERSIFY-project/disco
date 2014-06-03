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
    public void isNotKilledShouldBeTrueNewComponent() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("My VM")
                        .providedBy("Ec2"))
                .build();

        final Simulator simulator = new Simulator(deployment);

        assertThat("not killed", simulator.isNotKilled(deployment.getComponents().firstNamed("My VM")));

    }

    @Test
    public void isNotKilledShouldBeFalseForAComponentThatWasJustKilled() {
        final Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("My VM")
                        .providedBy("Ec2"))
                .build();
        final Component theVm = deployment.getComponents().firstNamed("My VM");

        final Simulator simulator = new Simulator(deployment);
        simulator.kill(theVm); 
        
        
        assertThat("not killed", simulator.isNotKilled(theVm), is(false));
    }
}
