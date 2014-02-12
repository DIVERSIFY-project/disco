package eu.diversify.disco.experiments.commons;

import eu.diversify.disco.experiments.commons.data.DataSet;
import java.util.List;

/**
 * General behaviour of experiments
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Experiment {

    /**
     * Run the experiment against the predefined parameters and return the
     * associated result set
     *
     * @return the associated result set.
     */
    public List<DataSet> run();
}
