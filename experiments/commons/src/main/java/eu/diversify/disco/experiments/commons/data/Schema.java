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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
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
     *
     * @param fields the list of field which must be placed in each record
     */
    public Schema(Collection<Field> fields, String missingFlag) {
        this.missingFlag = missingFlag;
        this.fields = new HashMap<String, Field>();
        for (Field field : fields) {
            if (this.fields.containsKey(field.getName())) {
                throw new IllegalArgumentException("Duplicated field name '" + field.getName() + "'");
            }
            this.fields.put(field.getName(), field);
        }
    }

    /**
     * @return a new dataset matching this schema
     */
    public DataSet newDataSet() {
        return new DataSet(this);
    }

    /**
     * A new dataset matching this schema, with the given name
     * @param dataSetName the name of the dataset
     * @return 
     */
    public DataSet newDataSet(String dataSetName) {
        return new DataSet(this, dataSetName);
    }

    /**
     * Create a new record which respect this scheme
     *
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

    public boolean hasField(String fieldName) {
        return this.fields.containsKey(fieldName);
    }

    public Field getField(String fieldName) {
        return fields.get(fieldName);
    }
}
