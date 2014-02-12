
package eu.diversify.disco.experiments.commons.data;

/**
 * Describe one field in a record, including name and type
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class Field {
    
    private final String name;
    private final Class type;
    
    /**
     * Create a new field with the given name and datatype
     * @param name
     * @param type 
     */
    public Field(String name, Class type) {
        this.name = name;
        this.type = type;
    }
    
    /**
     * @return the data type of this field
     */
    public Class getType() {
        return this.type;
    }
    
    /**
     * @return the name of this field
     */
    public String getName() {
        return this.name;
    }
    
}
