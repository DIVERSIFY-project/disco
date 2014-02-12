
package eu.diversify.disco.experiments.commons.codecs;

import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A naive codec to export dataset as CSV files
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class CsvDataCodec implements DataCodec {

    @Override
    public void save(DataSet dataset, OutputStream output) {
        PrintStream out = new PrintStream(output);
        ArrayList<Field> fields = new ArrayList<Field>(dataset.getSchema().getFields());
        
        // Print the header of the CSV file
        for (int i=0 ; i< fields.size() ; i++) {
            final Field field = fields.get(i);
            out.print(field.getName());
            if (i<fields.size() - 1) {
                out.print(",");
            }
        }
        out.print(System.lineSeparator());
        
        // Print the content of the data set
        for(int i = 0 ; i<dataset.getSize() ; i++) {
            for (int j=0 ;  j<fields.size() ; j++) {
                final Field field = fields.get(j);
                out.print(dataset.get(i, field));
                if (j < fields.size() - 1) {
                    out.print(",");
                }
            }            
            out.print(System.lineSeparator());
        }
    }
    
}
