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
    
    
    public Data newData() {
        return schema.newData();
    }

    /**
     * @param index index of the needed data point
     * @param field the field of interest
     * @return the value contained for the selected field of the selected data point
     */
    public Object get(int index, Field field) {
        return this.data.get(index).get(field);
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
     * @param index the index of the data point to retrieve
     * @return the data point at the given index, if it exists
     */
    public Data getData(int index) {
        return this.data.get(index);
    }

    
    /**
     * @return the number of data point in the data set
     */
    public int getSize() {
        return this.data.size();
    }


}
