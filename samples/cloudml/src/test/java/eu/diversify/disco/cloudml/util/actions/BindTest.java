/*
 */
package eu.diversify.disco.cloudml.util.actions;

import junit.framework.TestCase;
import org.cloudml.core.Binding;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;
import static org.cloudml.core.builders.Commons.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static eu.diversify.disco.cloudml.matchers.Commons.*;


public class BindTest extends TestCase {

    @Test
    public void testBinder() {
        final DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("EC2"))
                .withNodeType(aNode()
                    .named("Linux")
                    .providedBy("EC2"))
                .withNodeInstance(aNodeInstance()
                    .named("host 1")
                    .ofType("Linux"))
                .withNodeInstance(aNodeInstance()
                    .named("host 2")
                    .ofType("Linux"))
                .withArtefact(anArtefact()
                    .named("Client")
                    .withClientPort(aClientPort()
                        .named("client")
                        .remote()))
                .withArtefact(anArtefact()
                    .named("Server")
                    .withServerPort(aServerPort()
                        .named("server")
                        .remote()))
                .withBinding(aBinding()
                    .named("Connection")
                    .from("Client", "client")
                    .to("Server", "server"))
                .withArtefactInstance(anArtefactInstance()
                    .ofType("Client")
                    .named("client")
                    .hostedBy("host 1"))
                .withArtefactInstance(anArtefactInstance()
                    .ofType("Server")
                    .named("server")
                    .hostedBy("host 2"))
                .build();
        
       
        final Binding bindingType = model
                .findBindingByName("Connection");
                
        final ClientPortInstance clientPort = model
                .findArtefactInstanceByName("client")
                .findRequiredPortByName("client");
        
        final ServerPortInstance serverPort = model
                .findArtefactInstanceByName("server")
                .findProvidedPortByName("server");
        
        final StandardLibrary deployer = new StandardLibrary();

        new Bind(deployer, clientPort).applyTo(model);
        
        assertThat("valid output", model, is(valid()));        
    }
}