package eu.diversify.disco.experiments.commons.data;

import java.util.HashMap;

/**
 * Represent a single data point (aka an observation) which can be pushed into
 * a data set.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Data {

    private final Schema schema;
    private final HashMap<Field, Object> values;

    /**
     * Create a new data point according to the given schema.
     *
     * This constructor shall not be used directly.
     *
     * @param schema the schema to be used
     */
    Data(Schema schema) {
        this.schema = schema;
        this.values = new HashMap<Field, Object>();
        for (Field field : schema.getFields()) {
            this.values.put(field, null);
        }
    }

    /**
     * @return the schema associated with this data point
     */
    public Schema getSchema() {
        return this.schema;
    }

    /**
     * Set a given integer field in this data
     *
     * @param field the field to set
     * @param value the value to set
     */
    public void set(Field field, Object value) {
        if (!this.values.containsKey(field)) {
            throw new IllegalArgumentException("Unknown field '" + field.getName() + "'");
        }
        if (field.getType() != value.getClass()) {
            throw new IllegalArgumentException("The selected field does not have the required type (expected Integer)");
        }
        this.values.put(field, value);
    }

    /**
     * @param field the field of interest
     * @return the value contained in the selected field
     */
    public Object get(Field field) {
        if (!this.values.containsKey(field)) {
            throw new IllegalArgumentException("Unknown field '" + field.getName() + "'");
        }
        return this.values.get(field);
    }

}
