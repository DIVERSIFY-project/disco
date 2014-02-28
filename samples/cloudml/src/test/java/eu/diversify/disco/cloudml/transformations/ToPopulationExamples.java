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

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * Hold the examples of invocation for the forward transformation
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public enum ToPopulationExamples {

    EMPTY_MODEL("Empty DeploymentModel model",
                createEmptyDeploymentModel(),
                createEmptyPopulation()),
    ONE_VM_TYPE_BUT_NO_INSTANCE("1 VM type but no instance",
                                cloudMlWithOneVmTypeButNoInstance(),
                                populationWithOneVmTypeButNoInstance()),
    ONE_VM_TYPE_AND_ITS_INSTANCE("1 VM type and 1 instance",
                                 cloudmlWithOneVmTypeAndItsInstance(),
                                 populationWithOneVmTypeAndItsInstance()),
    MANY_VM_TYPES_BUT_NO_INSTANCE(
    "Many VM types but no instance",
    cloudmlManyVmTypesButNoInstance(),
    populationManyVmTypesButNoInstance()),
    MANY_VM_TYPES_WITH_TWO_INSTANCES(
    "Many VM types with 2 instances",
    cloudmlWithManyVmTypesWithTwoInstances(),
    populationWithManyVmTypesAndTwoInstances()),
    ONE_ARTEFACT_TYPE_BUT_NO_INSTANCE(
    "1 artefact type, but no instance (and no VM type)",
    cloudmlWithOneArtefactTypeButNoInstance(),
    populationWithOneArtefactTypeButNoInstance()),
    ONE_VM_TYPE_ONE_ARTEFACT_TYPE_BUT_NO_INSTANCE(
    "1 VM type, 1 artefact type, no instance",
    cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance(),
    populationWithOneVmTypeOneArtefactTypeAndNoInstance()),
    ONE_VM_TYPE_ONE_ARTEFACT_TYPE_AND_THEIR_TWO_INSTANCES(
    "1 VM type, 1 artefact type and their two instances",
    cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances(),
    populationWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances()),
    TWO_VM_TYPES_TWO_ARTEFACT_TYPES_AND_THE_FOUR_RELATED_INSTANCES(
    "2 VM types, 2 artefact types and the four related instances",
    cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances(),
    populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances()),
    SENSAPP(
    "SensApp",
    cloudmlWithSensApp(),
    populationWithSensApp()),
    MDMS(
    "MDMS",
    cloudmlWithMdms(),
    populationWithMdms()),
    HUIS_FAKE_MODEL(
    "Hui's fake model",
    cloudmlWithHuisFakeModel(),
    populationWithHuisFakeModel());
    /*
     *
     */
    private final String name;
    private final DeploymentModel input;
    private final Population expectedOutput;

    private ToPopulationExamples(String name, DeploymentModel input, Population expectedOutput) {
        this.name = name;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public String getName() {
        return this.name;
    }

    public DeploymentModel getInput() {
        return this.input;
    }

    public Population getExpectedOutput() {
        return this.expectedOutput;
    }

    public Object[] toArray() {
        return new Object[]{this};
    }

    @Override
    public String toString() {
        return this.getName();
    }

    // helpers that create the various population and cloudMl models;
    private static DeploymentModel createEmptyDeploymentModel() {
        return new DeploymentModel();
    }

    private static Population createEmptyPopulation() {
        return new PopulationBuilder().make();
    }

    private static DeploymentModel cloudMlWithOneVmTypeButNoInstance() {
        DeploymentModel cloudml = new DeploymentModel();
        addNewVmType(cloudml);
        return cloudml;
    }

    private static Population populationWithOneVmTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1)
                .make();
    }

    private static DeploymentModel cloudmlWithOneVmTypeAndItsInstance() {
        DeploymentModel model = cloudMlWithOneVmTypeButNoInstance();
        final Node vmType = model.getNodeTypes().get(VM_NAME_PREFIX + 1);
        addNewInstanceForVmType(model, vmType);
        return model;
    }

    private static Population populationWithOneVmTypeAndItsInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1)
                .withDistribution(1)
                .make();
    }

    private static DeploymentModel cloudmlManyVmTypesButNoInstance() {
        DeploymentModel model = cloudMlWithOneVmTypeButNoInstance();
        addNewVmType(model);
        addNewVmType(model);
        return model;
    }

    private static Population populationManyVmTypesButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2,
                                  VM_NAME_PREFIX + 3)
                .withDistribution(0, 0, 0)
                .make();
    }

    private static DeploymentModel cloudmlWithManyVmTypesWithTwoInstances() {
        DeploymentModel model = cloudmlManyVmTypesButNoInstance();
        for (Node vmType : model.getNodeTypes().values()) {
            addNewInstanceForVmType(model, vmType);
            addNewInstanceForVmType(model, vmType);
        }
        return model;
    }

    private static Population populationWithManyVmTypesAndTwoInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2,
                                  VM_NAME_PREFIX + 3)
                .withDistribution(2, 2, 2)
                .make();
    }

    private static DeploymentModel cloudmlWithOneArtefactTypeButNoInstance() {
        DeploymentModel model = new DeploymentModel();
        addNewArtefactType(model);
        return model;
    }

    private static Population populationWithOneArtefactTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(ARTEFACT_NAME_PREFIX + 1)
                .withDistribution(0)
                .make();
    }

    private static DeploymentModel cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance() {
        DeploymentModel model = cloudMlWithOneVmTypeButNoInstance();
        addNewArtefactType(model);
        return model;
    }

    private static Population populationWithOneVmTypeOneArtefactTypeAndNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, ARTEFACT_NAME_PREFIX + 1)
                .withDistribution(0, 0)
                .make();
    }

    private static DeploymentModel cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances() {
        DeploymentModel model = cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance();
        NodeInstance host = addNewInstanceForVmType(model, getVmType(model, 1));
        ArtefactInstance app = addNewInstanceForArtefactType(model,
                                                             getArtefactType(
                model, 1));
        deploy(model, host, app);
        return model;
    }

    private static Population populationWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, ARTEFACT_NAME_PREFIX + 1)
                .withDistribution(1, 1)
                .make();
    }

    private static DeploymentModel cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        DeploymentModel model = cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances();
        Node nodeType = addNewVmType(model);
        Artefact artefactType = addNewArtefactType(model);
        NodeInstance host = addNewInstanceForVmType(model, getVmType(model, 2));
        ArtefactInstance app = addNewInstanceForArtefactType(model,
                                                             getArtefactType(
                model, 2));
        deploy(model, host, app);
        return model;
    }

    private static Population populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1,
                                  VM_NAME_PREFIX + 2,
                                  ARTEFACT_NAME_PREFIX + 1,
                                  ARTEFACT_NAME_PREFIX + 2)
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
            Logger.getLogger(ToPopulationExamples.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }
        return root;
    }

    private static Population populationWithSensApp() {
        // cloudmlsensappgui: 1, cloudmlsensapp: 1, SensAppGUIWar: 1, MongoDB: 1, jettyWarContainer: 2, SensAppWar: 1 
        return new PopulationBuilder()
                .withSpeciesNamed("cloudmlsensappgui", "cloudmlsensapp",
                                  "SensAppGUIWar", "MongoDB",
                                  "jettyWarContainer", "SensAppWar")
                .withDistribution(1, 1, 1, 1, 2, 1)
                .make();
    }

    private static DeploymentModel cloudmlWithMdms() {
        return new MdmsModelCreator().create();
    }

    private static Population populationWithMdms() {
        return new PopulationBuilder()
                .withSpeciesNamed("EC2", "MySQL", "Balancer", "RingoJS",
                                  "OpenJDK", "Rhino", "MDMS")
                .withDistribution(3, 1, 1, 1, 1, 1, 1)
                .make();
    }

    private static DeploymentModel cloudmlWithHuisFakeModel() {
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

    private static Population populationWithHuisFakeModel() {
        return new PopulationBuilder()
                .withSpeciesNamed("huge", "big", "small")
                .withDistribution(5, 10, 10)
                .make();
    }


    /*
     * Helper functions to manipulate DeploymentModel models
     */
    private static Node getVmType(DeploymentModel model, int nodeTypeIndex) {
        final Node type = model.getNodeTypes().get(
                VM_NAME_PREFIX + nodeTypeIndex);
        if (type == null) {
            throw new IllegalArgumentException(
                    "No VM type with index '" + nodeTypeIndex + "'");
        }
        return type;
    }

    private static Artefact getArtefactType(DeploymentModel model, int artefactTypeIndex) {
        final Artefact type = model.getArtefactTypes().get(
                ARTEFACT_NAME_PREFIX + artefactTypeIndex);
        if (type == null) {
            throw new IllegalArgumentException(
                    "No artefact type with idnex '" + artefactTypeIndex + "'");
        }
        return type;
    }

    private static Node addNewVmType(DeploymentModel model) {
        final int vmCount = model.getNodeTypes().size();
        final String vmName = VM_NAME_PREFIX + (vmCount + 1);
        Node vm = new Node(vmName);
        model.getNodeTypes().put(vmName, vm);
        return vm;
    }

    private static Artefact addNewArtefactType(DeploymentModel model) {
        final int count = model.getArtefactTypes().size();
        final String typeName = ARTEFACT_NAME_PREFIX + (count + 1);
        Artefact type = new Artefact(typeName);
        model.getArtefactTypes().put(typeName, type);
        return type;
    }

    private static NodeInstance addNewInstanceForVmType(DeploymentModel model, Node type) {
        final int count = model.getNodeInstances().size();
        final String instanceName = VM_INSTANCE_NAME_PREFIX + (count + 1);
        NodeInstance instance = new NodeInstance(instanceName, type);
        model.getNodeInstances().add(instance);
        return instance;
    }

    private static ArtefactInstance addNewInstanceForArtefactType(DeploymentModel model, Artefact type) {
        final int count = model.getArtefactInstances().size();
        final String instanceName = ARTEFACT_INSTANCE_NAME_PREFIX + (count + 1);
        ArtefactInstance instance = new ArtefactInstance(instanceName, type);
        model.getArtefactInstances().add(instance);
        return instance;
    }

    private static void deploy(DeploymentModel model, NodeInstance host, ArtefactInstance app) {
        app.setDestination(host);
    }
    // Constant values
    public static final String VM_NAME_PREFIX = "node type #";
    public static final String ARTEFACT_NAME_PREFIX = "artefact type #";
    public static final String VM_INSTANCE_NAME_PREFIX = "vm instance #";
    public static final String ARTEFACT_INSTANCE_NAME_PREFIX = "artefact instance #";
}
