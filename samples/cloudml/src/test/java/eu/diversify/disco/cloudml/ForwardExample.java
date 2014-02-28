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
        "Empty DeploymentModel model",
        createEmptyDeploymentModel(),
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
        "2 VM types, 2 artefact types and the four related instances",
        cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances(),
        populationWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances())
    ;
    
    /*
     *
     */
    private final String name;
    private final DeploymentModel input;
    private final Population expectedOutput;

    private ForwardExample(String name, DeploymentModel input, Population expectedOutput) {
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
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2, VM_NAME_PREFIX + 3)
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
                .withSpeciesNamed(VM_NAME_PREFIX + 1, VM_NAME_PREFIX + 2, VM_NAME_PREFIX + 3)
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

    private static DeploymentModel cloudmlWithTwoVmTypesTwoArtefactTypesAndTheFourRelatedInstances() {
        DeploymentModel model = cloudmlWithOneVmTypeOneArtefactTypeAndTheirTwoRelatedInstances();
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
     * Helper functions to manipulate DeploymentModel models
     */
    private static Node getVmType(DeploymentModel model, int nodeTypeIndex) {
        final Node type = model.getNodeTypes().get(VM_NAME_PREFIX + nodeTypeIndex);
        if (type == null) {
            throw new IllegalArgumentException("No VM type with index '" + nodeTypeIndex + "'");
        }
        return type;
    }

    private static Artefact getArtefactType(DeploymentModel model, int artefactTypeIndex) {
        final Artefact type = model.getArtefactTypes().get(ARTEFACT_NAME_PREFIX + artefactTypeIndex);
        if (type == null) {
            throw new IllegalArgumentException("No artefact type with idnex '" + artefactTypeIndex + "'");
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
