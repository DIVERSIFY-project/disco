package eu.diversify.disco.cloudml.util.actions;

import junit.framework.TestCase;
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;

import static org.cloudml.core.builders.Commons.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static eu.diversify.disco.cloudml.matchers.Commons.*;

public class InstallTest extends TestCase {

    @Test
    public void testInstall() {
        final DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("EC2"))
                .withNodeType(aNode()
                    .named("Linux")
                    .providedBy("EC2"))
                .withNodeInstance(aNodeInstance()
                    .named("host 1")
                    .ofType("Linux"))
                .withArtefact(anArtefact()
                    .named("My Client")
                    .withClientPort(aClientPort()
                        .named("rp1")
                        .remote()))
                .withArtefact(anArtefact()
                    .named("My Server")
                    .withServerPort(aServerPort()
                        .named("pp1")
                        .remote()))
                .withArtefactInstance(anArtefactInstance()
                    .named("server")
                    .ofType("My Server")
                    .hostedBy("host 1"))
                .withBinding(aBinding()
                    .named("Connection")
                    .from("My Client", "rp1")
                    .to("My Server", "pp1"))
                .build();
        
        final Artefact artefact = model.findArtefactByName("My Client");

        final StandardLibrary deployer = new StandardLibrary();

        new Install(deployer, artefact).applyTo(model);

        assertThat("valid output", model, is(valid()));
    }
}