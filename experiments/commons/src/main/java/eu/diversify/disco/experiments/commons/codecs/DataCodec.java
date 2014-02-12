package eu.diversify.disco.experiments.commons.codecs;

import eu.diversify.disco.experiments.commons.data.DataSet;
import java.io.OutputStream;

/**
 * Interface of codec objects
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface DataCodec {

    /**
     * Write a the content of the dataset to the given output stream
     *
     * @param dataset the dataset to serialise
     * @param output the destination output stream
     */
    public void save(DataSet dataset, OutputStream output);
}
