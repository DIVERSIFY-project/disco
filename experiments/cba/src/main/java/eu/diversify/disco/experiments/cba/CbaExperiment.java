package eu.diversify.disco.experiments.cba;

import eu.diversify.disco.cloudml.CloudMLModel;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.cost.CostAsSize;
import eu.diversify.disco.cloudml.indicators.diversity.DiversityCalculator;
import eu.diversify.disco.cloudml.indicators.robustness.RobustnessCalculator;
import eu.diversify.disco.cloudml.transformations.ToCloudML;
import eu.diversify.disco.cloudml.transformations.ToPopulation;
import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import eu.diversify.disco.population.diversity.MetricFactory;
import eu.diversify.disco.population.diversity.TrueDiversity;
import eu.diversify.disco.samples.commons.ConstantReference;
import eu.diversify.disco.samples.commons.DiversityController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;

/**
 * The Cost-Benefits Analysis (CBA) on CloudML models
 */
public class CbaExperiment implements Experiment {

    public static final Field RUN = new Field("run", Integer.class);
    public static final Field DEPLOYMENT = new Field("deployment", String.class);
    public static final Field INITIAL_DIVERSITY = new Field("initial diversity", Double.class);
    public static final Field EXPECTED_DIVERSITY = new Field("expected diversity", Double.class);
    public static final Field ACTUAL_DIVERSITY = new Field("actual diversity", Double.class);
    public static final Field INITIAL_COST = new Field("initial cost", Double.class);
    public static final Field ACTUAL_COST = new Field("cost", Double.class);
    public static final Field INITIAL_ROBUSTNESS = new Field("initial robustness", Double.class);
    public static final Field ACTUAL_ROBUSTNESS = new Field("robustness", Double.class);
    private static final Schema SCHEMA = new Schema(
            Arrays.asList(new Field[]{
                RUN,
                DEPLOYMENT,
                INITIAL_DIVERSITY,
                INITIAL_COST,
                INITIAL_ROBUSTNESS,
                EXPECTED_DIVERSITY,
                ACTUAL_DIVERSITY,
                ACTUAL_COST,
                ACTUAL_ROBUSTNESS
            }),
            "");
    private final CbaSetup setup;
    private final DeploymentIndicator diversity;
    private final DeploymentIndicator cost;
    private final DeploymentIndicator robustness;

    public CbaExperiment(CbaSetup setup) {
        this.setup = setup;
        diversity = new DiversityCalculator(MetricFactory.create(setup.getDiversityMetric()).normalise());
        cost = new CostAsSize();
        robustness = new RobustnessCalculator();
    }

    @Override
    public List<DataSet> run() {
        final ArrayList<DataSet> results = new ArrayList<DataSet>();
        final DataSet dataset = new DataSet(SCHEMA);
        for (String eachDeployment: setup.getDeploymentModels()) {
            for (int run = 0; run < setup.getSampleCount(); run++) {
                final Deployment model = loadDeployment(eachDeployment);
                final double initialRobustness = robustness.evaluateOn(model);
                final double initialDiversity = diversity.evaluateOn(model);
                final double initialCost = cost.evaluateOn(model);
                for (double reference: setup.getDiversityLevels()) {
                    Deployment diversifiedModel = diversifyDeployment(model, reference);
                    Data data = SCHEMA.newData();
                    data.set(RUN, run);
                    data.set(DEPLOYMENT, model.getName());
                    data.set(INITIAL_DIVERSITY, initialDiversity);
                    data.set(INITIAL_COST, initialCost);
                    data.set(INITIAL_ROBUSTNESS, initialRobustness);
                    data.set(EXPECTED_DIVERSITY, reference);
                    data.set(ACTUAL_DIVERSITY, diversity.evaluateOn(diversifiedModel));
                    data.set(ACTUAL_COST, cost.evaluateOn(diversifiedModel));
                    data.set(ACTUAL_ROBUSTNESS, robustness.evaluateOn(diversifiedModel));
                    dataset.add(data);
                }
            }
        }
        results.add(dataset);
        return results;
    }

    private Deployment loadDeployment(String deployment) {
        assert deployment != null: "Unable to load 'null'";
        assert !deployment.isEmpty(): "Unable to load ''";

        final JsonCodec codec = new JsonCodec();
        InputStream stream = null;
        try {
            stream = new FileInputStream(deployment);
        
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("Unable to load the given deployment model '" + setup.getDeploymentModels() + "'");
        }
            
        final Deployment model = (Deployment) codec.load(stream);
        try {
            stream.close();

        } catch (IOException ex) {
            throw new IllegalArgumentException("I/O error while closing '" + setup.getDeploymentModels() + "'");
        }
        
        return model;
    }

    private Deployment diversifyDeployment(Deployment model, double reference) {
        final CloudMLModel source = new CloudMLModel();
        source.setLocation("source.json");
        source.write(model);

        final CloudMLModel target = new CloudMLModel();
        target.setLocation(makeFileName(model, reference));

        final ConstantReference setPoint = new ConstantReference();
        setPoint.setReference(reference);

        final DiversityController<Deployment> controller = new DiversityController<Deployment>(
                new TrueDiversity().normalise(),
                source,
                new ToPopulation(),
                setPoint,
                new ToCloudML(),
                target);

        controller.control();

        return target.read();
    }

    private String makeFileName(Deployment model, double reference) {
        assert model != null: "Unable to compute the name of the model";

        final int asPercentage = (int) Math.round(100 * reference);

        return String.format("%s_%03d.json", model.getName(), asPercentage);
    }
}
