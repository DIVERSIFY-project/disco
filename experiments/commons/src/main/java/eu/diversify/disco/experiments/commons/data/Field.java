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
