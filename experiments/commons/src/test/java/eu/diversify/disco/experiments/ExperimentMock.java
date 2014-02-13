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
