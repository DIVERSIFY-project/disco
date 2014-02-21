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
package eu.diversify.disco.experiments.cba;

import eu.diversify.disco.cloudml.CloudML;
import eu.diversify.disco.cloudml.DiversityController;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.cost.CostAsSize;
import eu.diversify.disco.cloudml.indicators.diversity.DiversityCalculator;
import eu.diversify.disco.cloudml.indicators.robustness.DummyRobustnessCalculator;
import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import eu.diversify.disco.population.diversity.MetricFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;

/**
 * The Cost-Benefits Analysis (CBA) on CloudML models
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class CbaExperiment implements Experiment {

    public static final Field RUN = new Field("run", Integer.class);
    public static final Field EXPECTED_DIVERSITY = new Field("expected diversity", Double.class);
    public static final Field ACTUAL_DIVERSITY = new Field("actual diversity", Double.class);
    public static final Field COST = new Field("cost", Double.class);
    public static final Field ROBUSTNESS = new Field("robustness", Double.class);
    private static final Schema SCHEMA = new Schema(
            Arrays.asList(new Field[]{
        RUN,
        EXPECTED_DIVERSITY,
        ACTUAL_DIVERSITY,
        COST,
        ROBUSTNESS
    }),
            "");
    private final CbaSetup setup;
    private final DeploymentIndicator diversity;
    private final DeploymentIndicator cost;
    private final DeploymentIndicator robustness;

    public CbaExperiment(CbaSetup setup) {
        this.setup = setup;
        MetricFactory metrics = new MetricFactory();
        diversity = new DiversityCalculator(metrics.instantiate(setup.getDiversityMetric()));
        cost = new CostAsSize();
        robustness = new DummyRobustnessCalculator();
    }

    @Override
    public List<DataSet> run() {
        ArrayList<DataSet> results = new ArrayList<DataSet>();
        DataSet dataset = new DataSet(SCHEMA);
        for (int run = 0; run < setup.getSampleCount(); run++) {
            DeploymentModel model = loadDeploymentModel();
            for (double reference : setup.getDiversityLevels()) {
                DeploymentModel diversifiedModel = diversifyDeployment(model, reference);
                Data data = SCHEMA.newData();
                data.set(RUN, run);
                data.set(EXPECTED_DIVERSITY, reference);
                data.set(ACTUAL_DIVERSITY, diversity.evaluateOn(diversifiedModel));
                data.set(COST, cost.evaluateOn(diversifiedModel));
                data.set(ROBUSTNESS, robustness.evaluateOn(diversifiedModel));
                dataset.add(data);
            }
        }
        results.add(dataset);
        return results;
    }

    private DeploymentModel loadDeploymentModel() {
        DeploymentModel model;
        JsonCodec codec = new JsonCodec();
        InputStream stream = null;
        try {
            stream = new FileInputStream(setup.getDeploymentModel());
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("Unable to load the given deployment model '" + setup.getDeploymentModel() + "'");
        }
        model = (DeploymentModel) codec.load(stream);
        try {
            stream.close();
        } catch (IOException ex) {
            throw new IllegalArgumentException("I/O error while closing '" + setup.getDeploymentModel() + "'");
        }
        return model;
    }

    private DeploymentModel diversifyDeployment(DeploymentModel model, double reference) {
        DiversityController controller = new DiversityController(reference);
        CloudML cloudml = new CloudML();
        cloudml.setRoot(model);
        // return controller.applyTo(cloudml).getRoot(); FIXME this line fails
        return model;
    }
}
