package eu.diversify.disco.cloudml;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;

/**
 * Hold one single example of invocation of the forward transformation
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public enum ForwardExample {

    // TODO: test many vms types, and no instance
    // TODO: test many vm types and and many instances
    // TODO: test one artefact type and no instance
    // TODO: test many artefact types and many instances
    
    EMPTY_MODEL(
        "Empty CloudML model",
        createEmptyCloudML(),
        createEmptyPopulation()),
    ONE_VM_TYPE_BUT_NO_INSTANCE(
        "1 VM type but no instance",
        cloudMlWithOneVmTypeButNoInstance(),
        populationWithOneVmTypeButNoInstance()),
    ONE_VM_TYPE_AND_ITS_INSTANCE(
        "1 VM type and one instance",
        cloudmlWithOneVmTypeAndItsInstance(),
        populationWithOneVmTypeAndItsInstance());
    
    
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

    // helpers that create the various population and cloudMl models;
    
    private static CloudML createEmptyCloudML() {
        return new CloudML();
    }

    private static Population createEmptyPopulation() {
        return new PopulationBuilder().make();
    }

    private static CloudML cloudMlWithOneVmTypeButNoInstance() {
        DeploymentModel model = new DeploymentModel();

        Node vm = new Node(SINGLE_VM_NAME);
        model.getNodeTypes().put(SINGLE_VM_NAME, vm);

        CloudML cloudml = new CloudML();
        cloudml.setRoot(model);

        return cloudml;
    }

    private static Population populationWithOneVmTypeButNoInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(SINGLE_VM_NAME)
                .make();
    }

    private static CloudML cloudmlWithOneVmTypeAndItsInstance() {
        CloudML model = cloudMlWithOneVmTypeButNoInstance();

        Node type = model.getRoot().getNodeTypes().get(SINGLE_VM_NAME);
        NodeInstance instance = new NodeInstance(SINGLE_VM_INSTANCE_NAME, type);
        model.getRoot().getNodeInstances().add(instance);

        return model;
    }

    private static Population populationWithOneVmTypeAndItsInstance() {
        return new PopulationBuilder()
                .withSpeciesNamed(SINGLE_VM_NAME)
                .withDistribution(1)
                .make();
    }
    
    public static final String SINGLE_VM_NAME = "VM";
    public static final String SINGLE_VM_INSTANCE_NAME = "instance#1";
}
