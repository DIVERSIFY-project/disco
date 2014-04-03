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
/*
 */
package eu.diversify.disco.experiments.commons.data;

import java.util.ArrayList;

public class SchemaBuilder {
    public static final String DEFAULT_MISSING_DATA_MARKER = "n/a";

    public static SchemaBuilder aSchema() {
        return new SchemaBuilder();
    }

    public static FieldBuilder aField() {
        return new FieldBuilder();
    }
    public static final Class<?> STRING = java.lang.String.class;
    public static final Class<?> INTEGER = java.lang.Integer.class;
    public static final Class<?> DOUBLE = java.lang.Double.class;
    public static final Class<?> FLOAT = java.lang.Float.class;
    public static final Class<?> BOOLEAN = java.lang.Boolean.class;
    public static final Class<?> CHARACTER = java.lang.Character.class;
    
    private final ArrayList<FieldBuilder> fields;
    private String missingDataMarker;
    
    private SchemaBuilder() {
        this.fields = new ArrayList<FieldBuilder>();
        this.missingDataMarker = DEFAULT_MISSING_DATA_MARKER;
    }

    public SchemaBuilder with(FieldBuilder fieldBuilder) {
        this.fields.add(fieldBuilder);
        return this;
    }
    
    public SchemaBuilder withMissingDataMarker(String missingDataMarker) {
        this.missingDataMarker = missingDataMarker;
        return this;
    }

    public Schema build() {
        ArrayList<Field> fields = new ArrayList<Field>();
        for (FieldBuilder builder : this.fields) {
            fields.add(builder.build());
        }
        return new Schema(fields, missingDataMarker);
    }
}
