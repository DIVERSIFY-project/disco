package eu.diversify.disco.experiments;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A dummy experiment which generates dummy data
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ExperimentMock implements Experiment {

    private final SetupMock setup;

    public ExperimentMock(SetupMock setup) {
        this.setup = setup;
    }

    @Override
    public List<DataSet> run() {
        // Create a schema for the data set
        Field time = new Field("time", Integer.class);
        Field rnorm = new Field("rnorm", Double.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{time, rnorm}), "n/a");

        // Create and populate a data set
        DataSet ds = new DataSet(schema, "test");
        Random generator = new Random();
        for (int i = 0; i < 1000; i++) {
            Data d = schema.newData();
            d.set(time, i);
            d.set(rnorm, generator.nextGaussian());
            ds.add(d);
        }

        // Wrap the resulting ds in a list
        ArrayList<DataSet> results = new ArrayList<DataSet>();
        results.add(ds);
        return results;
    }
}
