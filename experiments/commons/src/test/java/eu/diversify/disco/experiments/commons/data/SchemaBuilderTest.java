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

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.disco.experiments.commons.data.SchemaBuilder.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*; 
import org.junit.Test;

@RunWith(JUnit4.class)
public class SchemaBuilderTest extends TestCase {

    @Test
    public void testSchemaBuilder() {
        Schema schema = aSchema()
                .with(aField().named("name").ofType(STRING))
                .with(aField().named("age").ofType(INTEGER))
                .build();

        assertThat("field count", schema.getFields().size(), is(equalTo(2)));
        assertThat("field name", schema.hasField("name"));        
        assertThat("field name type", schema.getField("name").getType().equals(STRING));        
        assertThat("field age", schema.hasField("age"));        
        assertThat("field age type", schema.getField("age").getType().equals(INTEGER));         
    }
}