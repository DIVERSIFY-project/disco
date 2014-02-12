package eu.diversify.disco.experiments.commons.data;

import java.util.Collection;
import java.util.HashMap;

/**
 * A simple schema class for datasets
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class Schema {
    
    final String missingFlag;
    final HashMap<String, Field> fields;
    
    /**
     * Create a new scheme based on collection of fields
     * @param fields the list of field which must be placed in each record
     */
    public Schema(Collection<Field> fields, String missingFlag) {
        this.missingFlag = missingFlag;
        this.fields = new HashMap<String, Field>();
        for (Field field: fields) {
            if (this.fields.containsKey(field.getName())) {
                throw new IllegalArgumentException("Duplicated field name '" + field.getName() + "'");
            }
            this.fields.put(field.getName(), field);
        }
    }

    /**
     * Create a new record which respect this scheme
     * @return the brand new Record object
     */
    public Data newData() {
        return new Data(this);
    }

    /**
     * @return the list of field defined in this schema
     */
    public Collection<Field> getFields() {
        return this.fields.values();
    }
    
}
