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

import eu.diversify.disco.cloudml.util.DeploymentModelBuilder;
import eu.diversify.disco.cloudml.util.NamingPolicy;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.cloudml.core.Provider;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ToPopulationExampleCatalog {

    private final NamingPolicy naming;
    private final HashMap<String, ToPopulationExample> examples;

    public ToPopulationExampleCatalog() {
        this.naming = new NamingPolicy();
        this.examples = new HashMap<String, ToPopulationExample>();
        prepareCatalog();
    }

    private void prepareCatalog() {
        add("Empty DeploymentModel model",
            createEmptyDeploymentModel(),
            createEmptyPopulation());

        add("1 VM type but no instance",
            cloudMlWithOneVmTypeButNoInstance(),
            populationWithOneVmTypeButNoInstance());

        add("1 VM type and 1 instance",
            cloudmlWithOneVmTypeAndItsInstance(),
            populationWithOneVmTypeAndItsInstance());

        add("Many VM types but no instance",
            cloudmlManyVmTypesButNoInstance(),
            populationManyVmTypesButNoInstance());

        add("Many VM types with 2 instances",
            cloudmlWithManyVmTypesWithTwoInstances(),
            populationWithManyVmTypesAndTwoInstances());

        add("1 artefact type, but no instance (and no VM type)",
            cloudmlWithOneArtefactTypeButNoInstance(),
            populationWithOneArtefactTypeButNoInstance());

        add("1 VM type, 1 artefact type, no instance",
            cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance(),
            populationWithOneVmTypeOneArtefactTypeAndNoInstance());

        add("1 VM type, 1 artefact type and their two instances",
            cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances(),
            populationWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances());

        add("2 VM types, 2 artefact types and the four related instances",
            cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances(),
            populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances());

        add("SensApp",
            cloudmlWithSensApp(),
            populationWithSensApp());

        add("MDMS",
            cloudmlWithMdms(),
            populationWithMdms());

        add("Hui's fake model",
            cloudmlWithHuisFakeModel(),
            populationWithHuisFakeModel());
    }

    private void add(String name, DeploymentModel input, Population output) {
        this.examples.put(name, new ToPopulationExample(name, input, output));
    }

    public Collection<String> getExampleNames() {
        return this.examples.keySet();
    }

    public ToPopulationExample get(String key) {
        if (!examples.containsKey(key)) {
            throw new IllegalArgumentException("Unknown example '" + key + "'");
        }
        return this.examples.get(key);
    }

    private DeploymentModel createEmptyDeploymentModel() {
        return new DeploymentModel();
    }

    private Population createEmptyPopulation() {
        return new PopulationBuilder().make();
    }

    private DeploymentModel cloudMlWithOneVmTypeButNoInstance() {
        DeploymentModelBuilder builder = new DeploymentModelBuilder();
        builder.addNodeTypeWithDefaultName();
        return builder.getResult();
    }

    private Population populationWithOneVmTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1))
                .make();
    }

    private DeploymentModel cloudmlWithOneVmTypeAndItsInstance() {
        DeploymentModelBuilder model = new DeploymentModelBuilder(cloudMlWithOneVmTypeButNoInstance());
        final Node vmType = model.findNodeType(1);
        model.instantiate(vmType);
        return model.getResult();
    }

    private Population populationWithOneVmTypeAndItsInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1))
                .withDistribution(1)
                .make();
    }

    private DeploymentModel cloudmlManyVmTypesButNoInstance() {
        DeploymentModelBuilder model = new DeploymentModelBuilder(cloudMlWithOneVmTypeButNoInstance());
        model.addNodeTypeWithDefaultName();
        model.addNodeTypeWithDefaultName();
        return model.getResult();
    }

    private Population populationManyVmTypesButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1),
                                  naming.nameOfNodeType(2),
                                  naming.nameOfNodeType(3))
                .withDistribution(0, 0, 0)
                .make();
    }

    private DeploymentModel cloudmlWithManyVmTypesWithTwoInstances() {
        DeploymentModelBuilder model = new DeploymentModelBuilder(cloudmlManyVmTypesButNoInstance());
        for (Node vmType : model.getResult().getNodeTypes().values()) {
            model.instantiate(vmType);
            model.instantiate(vmType);
        }
        return model.getResult();
    }

    private Population populationWithManyVmTypesAndTwoInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1),
                                  naming.nameOfNodeType(2),
                                  naming.nameOfNodeType(3))
                .withDistribution(2, 2, 2)
                .make();
    }

    private DeploymentModel cloudmlWithOneArtefactTypeButNoInstance() {
        DeploymentModelBuilder model = new DeploymentModelBuilder();
        model.addArtefactTypeWithDefaultName();
        return model.getResult();
    }

    private Population populationWithOneArtefactTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfArtefactType(1))
                .withDistribution(0)
                .make();
    }

    private DeploymentModel cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance() {
        DeploymentModelBuilder model = new DeploymentModelBuilder(cloudMlWithOneVmTypeButNoInstance());
        model.addArtefactTypeWithDefaultName();
        return model.getResult();
    }

    private Population populationWithOneVmTypeOneArtefactTypeAndNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1), naming.nameOfArtefactType(1))
                .withDistribution(0, 0)
                .make();
    }

    private DeploymentModel cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances() {
        DeploymentModelBuilder builder = new DeploymentModelBuilder(cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance());
        NodeInstance host = builder.instantiate(builder.findNodeType(1));
        ArtefactInstance app = builder.instantiate(builder.findArtefactType(1));
        builder.deploy(host, app);
        return builder.getResult();
    }

    private Population populationWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1), naming.nameOfArtefactType(1))
                .withDistribution(1, 1)
                .make();
    }

    private DeploymentModel cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        DeploymentModelBuilder model = new DeploymentModelBuilder(cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances());
        Node nodeType = model.addNodeTypeWithDefaultName();
        Artefact artefactType = model.addArtefactTypeWithDefaultName();
        NodeInstance host = model.instantiate(nodeType);
        ArtefactInstance app = model.instantiate(artefactType);
        model.deploy(host, app);
        return model.getResult();
    }

    private Population populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(naming.nameOfNodeType(1),
                                  naming.nameOfNodeType(2),
                                  naming.nameOfArtefactType(1),
                                  naming.nameOfArtefactType(2))
                .withDistribution(1, 1, 1, 1)
                .make();
    }

    private static DeploymentModel cloudmlWithSensApp() {
        JsonCodec jsonCodec = new JsonCodec();
        DeploymentModel root = null;
        try {
            root = (DeploymentModel) jsonCodec.load(new FileInputStream(
                    "../src/main/resources/sensappAdmin.json"));
            for (NodeInstance ni : root.getNodeInstances()) {
                ni.getProperties().add(new Property("state", "onn"));
            }
            for (ArtefactInstance ai : root.getArtefactInstances()) {
                ai.getProperties().add(new Property("state", "onn"));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ToPopulationExample.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }
        return root;
    }

    private static Population populationWithSensApp() {
        return new PopulationBuilder()
                .withSpeciesNamed("cloudmlsensappgui", "cloudmlsensapp",
                                  "SensAppGUIWar", "MongoDB",
                                  "jettyWarContainer", "SensAppWar")
                .withDistribution(1, 1, 1, 1, 2, 1)
                .make();
    }

    private DeploymentModel cloudmlWithMdms() {
        return new MdmsModelCreator().create();
    }

    private Population populationWithMdms() {
        return new PopulationBuilder()
                .withSpeciesNamed("EC2", "MySQL", "Balancer", "RingoJS",
                                  "OpenJDK", "Rhino", "MDMS")
                .withDistribution(3, 1, 1, 1, 1, 1, 1)
                .make();
    }

    private DeploymentModel cloudmlWithHuisFakeModel() {
        DeploymentModel dm = new DeploymentModel();
        Provider hugeProvider = new Provider("huge",
                                             "../src/main/resources/credentials");
        Provider bigsmallProvider = new Provider("bigsmall",
                                                 "../src/main/resources/credentials");
        dm.getProviders().add(hugeProvider);
        dm.getProviders().add(bigsmallProvider);

        Node huge = new Node("huge");
        huge.setProvider(hugeProvider);
        Node big = new Node("big");
        big.setProvider(bigsmallProvider);
        Node small = new Node("small");
        small.setProvider(bigsmallProvider);

        dm.getNodeTypes().put("huge", huge);
        dm.getNodeTypes().put("big", big);
        dm.getNodeTypes().put("small", small);

        for (int i = 0; i < 5; i++) {
            dm.getNodeInstances().add(huge.instanciates("huge" + i));
        }

        for (int i = 0; i < 10; i++) {
            dm.getNodeInstances().add(big.instanciates("big" + i));
        }

        for (int i = 0; i < 10; i++) {
            dm.getNodeInstances().add(small.instanciates("small" + i));
        }

        for (NodeInstance ni : dm.getNodeInstances()) {
            ni.getProperties().add(new Property("state", "on"));
        }

        return dm;
    }

    private Population populationWithHuisFakeModel() {
        return new PopulationBuilder()
                .withSpeciesNamed("huge", "big", "small")
                .withDistribution(5, 10, 10)
                .make();
    }
}
