package eu.diversify.disco.cloudml;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

/**
 * Hold the examples of invocation for the forward transformation
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public enum ForwardExample {

    // TODO: test many artefact types and many instances, and their instances
    EMPTY_MODEL(
        "Empty CloudML model",
        createEmptyCloudML(),
        createEmptyPopulation()),
    ONE_VM_TYPE_BUT_NO_INSTANCE(
        "1 VM type but no instance",
        cloudMlWithOneVmTypeButNoInstance(),
        populationWithOneVmTypeButNoInstance()),
    ONE_VM_TYPE_AND_ITS_INSTANCE(
        "1 VM type and 1 instance",
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
        "2 VM typea, 2 artefact types and the four related instances",
        cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances(),
        populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances())
    ;
    
    /*
     *
     */
    private final String name;
    private final CloudML input;
    private final Population expectedOutput;

    private ForwardExample(String name, CloudML input, Population expectedOutput) {
        this.name = name;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public String getName() {
        return this.name;
    }

    public CloudML getInput() {
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
    private static CloudML createEmptyCloudML() {
        return new CloudML();
    }

    private static Population createEmptyPopulation() {
        return new PopulationBuilder().make();
    }

    private static CloudML cloudMlWithOneVmTypeButNoInstance() {
        CloudML cloudml = prepareCloudMLModel();
        addNewVmType(cloudml);
        return cloudml;
    }

    private static Population populationWithOneVmTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1)
                .make();
    }

    private static CloudML cloudmlWithOneVmTypeAndItsInstance() {
        CloudML model = cloudMlWithOneVmTypeButNoInstance();
        final Node vmType = model.getRoot().getNodeTypes().get(VM_NAME_PREFIX + 1);
        addNewInstanceForVmType(model, vmType);
        return model;
    }

    private static Population populationWithOneVmTypeAndItsInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1)
                .withDistribution(1)
                .make();
    }

    private static CloudML cloudmlManyVmTypesButNoInstance() {
        CloudML model = cloudMlWithOneVmTypeButNoInstance();
        addNewVmType(model);
        addNewVmType(model);
        return model;
    }

    private static Population populationManyVmTypesButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2, VM_NAME_PREFIX + 3)
                .withDistribution(0, 0, 0)
                .make();
    }

    private static CloudML cloudmlWithManyVmTypesWithTwoInstances() {
        CloudML model = cloudmlManyVmTypesButNoInstance();
        for (Node vmType : model.getRoot().getNodeTypes().values()) {
            addNewInstanceForVmType(model, vmType);
            addNewInstanceForVmType(model, vmType);
        }
        return model;
    }

    private static Population populationWithManyVmTypesAndTwoInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2, VM_NAME_PREFIX + 3)
                .withDistribution(2, 2, 2)
                .make();
    }

    private static CloudML cloudmlWithOneArtefactTypeButNoInstance() {
        CloudML cloudml = prepareCloudMLModel();
        addNewArtefactType(cloudml);
        return cloudml;
    }

    private static Population populationWithOneArtefactTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(ARTEFACT_NAME_PREFIX + 1)
                .withDistribution(0)
                .make();
    }

    private static CloudML cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance() {
        CloudML model = cloudMlWithOneVmTypeButNoInstance();
        addNewArtefactType(model);
        return model;
    }

    private static Population populationWithOneVmTypeOneArtefactTypeAndNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, ARTEFACT_NAME_PREFIX + 1)
                .withDistribution(0, 0)
                .make();
    }

    private static CloudML cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances() {
        CloudML model = cloudmlWithOneVmTypeOneArtefactTypeAndNoInstance();
        NodeInstance host = addNewInstanceForVmType(model, getVmType(model, 1));
        ArtefactInstance app = addNewInstanceForArtefactType(model, getArtefactType(model, 1));
        deploy(model, host, app);
        return model;
    }

    private static Population populationWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, ARTEFACT_NAME_PREFIX + 1)
                .withDistribution(1, 1)
                .make();
    }

    private static CloudML cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        CloudML model = cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances();
        Node nodeType = addNewVmType(model);
        Artefact artefactType = addNewArtefactType(model);
        NodeInstance host = addNewInstanceForVmType(model, getVmType(model, 2));
        ArtefactInstance app = addNewInstanceForArtefactType(model, getArtefactType(model, 2));
        deploy(model, host, app);
        return model;
    }

    private static Population populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        return new PopulationBuilder()
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2, ARTEFACT_NAME_PREFIX + 1, ARTEFACT_NAME_PREFIX + 2)
                .withDistribution(1, 1, 1, 1)
                .make();
    }

    /*
     * Helper functions to manipulate CloudML models
     */
    private static Node getVmType(CloudML model, int nodeTypeIndex) {
        final Node type = model.getRoot().getNodeTypes().get(VM_NAME_PREFIX + nodeTypeIndex);
        if (type == null) {
            throw new IllegalArgumentException("No VM type with index '" + nodeTypeIndex + "'");
        }
        return type;
    }

    private static Artefact getArtefactType(CloudML model, int artefactTypeIndex) {
        final Artefact type = model.getRoot().getArtefactTypes().get(ARTEFACT_NAME_PREFIX + artefactTypeIndex);
        if (type == null) {
            throw new IllegalArgumentException("No artefact type with idnex '" + artefactTypeIndex + "'");
        }
        return type;
    }

    private static CloudML prepareCloudMLModel() {
        DeploymentModel model = new DeploymentModel();
        CloudML cloudml = new CloudML();
        cloudml.setRoot(model);
        return cloudml;
    }

    private static Node addNewVmType(CloudML model) {
        final int vmCount = model.getRoot().getNodeTypes().size();
        final String vmName = VM_NAME_PREFIX + (vmCount + 1);
        Node vm = new Node(vmName);
        model.getRoot().getNodeTypes().put(vmName, vm);
        return vm;
    }

    private static Artefact addNewArtefactType(CloudML model) {
        final int count = model.getRoot().getArtefactTypes().size();
        final String typeName = ARTEFACT_NAME_PREFIX + (count + 1);
        Artefact type = new Artefact(typeName);
        model.getRoot().getArtefactTypes().put(typeName, type);
        return type;
    }

    private static NodeInstance addNewInstanceForVmType(CloudML model, Node type) {
        final int count = model.getRoot().getNodeInstances().size();
        final String instanceName = VM_INSTANCE_NAME_PREFIX + (count + 1);
        NodeInstance instance = new NodeInstance(instanceName, type);
        model.getRoot().getNodeInstances().add(instance);
        return instance;
    }

    private static ArtefactInstance addNewInstanceForArtefactType(CloudML model, Artefact type) {
        final int count = model.getRoot().getArtefactInstances().size();
        final String instanceName = ARTEFACT_INSTANCE_NAME_PREFIX + (count + 1);
        ArtefactInstance instance = new ArtefactInstance(instanceName, type);
        model.getRoot().getArtefactInstances().add(instance);
        return instance;
    }

    private static void deploy(CloudML model, NodeInstance host, ArtefactInstance app) {
        app.setDestination(host);
    }
    // Constant values
    public static final String VM_NAME_PREFIX = "node type #";
    public static final String ARTEFACT_NAME_PREFIX = "artefact type #";
    public static final String VM_INSTANCE_NAME_PREFIX = "vm instance #";
    public static final String ARTEFACT_INSTANCE_NAME_PREFIX = "artefact instance #";
}
