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

    /**
     * @param field the field whose value may be missing
     * @return true if the value is missing, i.e., is there is no value
     */
    public boolean isMissing(Field field) {
        return this.values.get(field) == null;
    }

}
