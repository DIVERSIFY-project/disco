package eu.diversify.disco.experiments.commons.codecs;

import eu.diversify.disco.experiments.commons.data.DataSet;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A single facade object which select the relevant codecs based on file name
 * extension.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Codecs {

    private final HashMap<String, DataCodec> codecs;

    /**
     * Build the codec library
     */
    public Codecs() {
        this.codecs = new HashMap<String, DataCodec>();
        this.codecs.put(".csv", new CsvDataCodec());
    }

    /**
     * Save a dataset in a given format
     *
     * @param dataset the data set to be written on disc
     * @param filename the name of the file to be created to store the data set
     */
    public void saveAs(DataSet dataset, String filename) throws FileNotFoundException, IOException {
        String extension = extractExtension(filename);
        if (extension == null) {
            throw new IllegalArgumentException("File names must have an extension, in order to select the appropriate codec");
        }
        DataCodec codec = this.codecs.get(extension);
        if (codec == null) {
            throw new IllegalArgumentException("Unsupported file format '" + extension + "'");
        }
        FileOutputStream output = new FileOutputStream(filename);
        codec.save(dataset, output);
        output.close();
    }

    /**
     * Extract the extension of the given filename. The resulting extension is
     * something like ".csv" for instance.
     * 
     *
     * @param fileName the filename whose extension is needed
     * @return the extension of the given file name or null if the given file name has no extension 
     */
    private String extractExtension(String fileName) {
        String extension = null;
        Pattern pattern = Pattern.compile(".*(\\.\\w+)$");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.matches()) {
            extension = matcher.group(1);
        }
        return extension;
    }
}
