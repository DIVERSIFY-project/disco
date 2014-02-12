package eu.diversify.disco.experiments.commons.data;

import java.util.ArrayList;

/**
 * A set of data point. It's behaviour when new point are added is controlled by
 * separated handlers, which can be attached to it, as in many logging
 * frameworks.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class DataSet {

    private final String name;
    private final Schema schema;
    private final ArrayList<Data> data;

    
    /**
     * Create a new dataset for a specific schema, with "results" as a name
     *
     * @param schema the schema describing the fields of this dataset
     */
    public DataSet(Schema schema) {
        this.name = "results";
        this.schema = schema;
        this.data = new ArrayList<Data>();
    }

    /**
     * Create a new dataset for a specific schema
     *
     * @param schema the schema describing the fields of this dataset.
     * 
     */
    public DataSet(Schema schema, String name) {
        this.name = name;
        this.schema = schema;
        this.data = new ArrayList<Data>();
    }

    /**
     * @param the index of the needed data point
     * @param field the field of interest
     * @return the value contained for the selected field of the selected data point
     */
    public Object get(int i, Field field) {
        return this.data.get(i).get(field);
    }
    
    /**
     * @return the name of this data set
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return the schema associated with this dataset 
     */
    public Schema getSchema() {
        return this.schema;
    }
    
    /**
     * Append a new data point at the end of this data set
     *
     * @param data the data point to append
     */
    public void add(Data data) {
        if (!data.getSchema().equals(this.schema)) {
            throw new IllegalArgumentException("The data schema of the data set does not match the schema of the given data point");
        }
        this.data.add(data);
    }

    /**
     * @return the number of data point in the data set
     */
    public int getSize() {
        return this.data.size();
    }


}
