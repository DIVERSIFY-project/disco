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

import java.util.Arrays;
import java.util.Random;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.disco.experiments.commons.data.SchemaBuilder.*;

/**
 * Test the behaviour of the result set
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class DataSetTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
        
    private Field time = new Field("time", Integer.class);
    private Field random = new Field("random", Double.class);
    private Field checked = new Field("checked", Boolean.class);
    private Field sparse = new Field("sparse", String.class);

    @Test
    public void testIsMissing() {
        Schema schema = new Schema(Arrays.asList(new Field[]{time, random, checked, sparse}), "n/a");

        Data data = schema.newData();
        for (Field field : schema.getFields()) {
            assertTrue(
                    "Field must be missing before any value is set",
                    data.isMissing(field));
        }

        data.set(time, 10);
        assertFalse(
                "Field must be missing before any value is set",
                data.isMissing(time));
        for (Field field : schema.getFields()) {
            if (!field.equals(time)) {
                assertTrue(
                        "Field must be missing before any value is set",
                        data.isMissing(field));
            }
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDataSchema() {
        Field time2 = new Field("time", Integer.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{time, time2, checked, sparse}), "n/a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownFieldAssignment() {
        Schema schema = new Schema(Arrays.asList(new Field[]{random, checked, sparse}), "n/a");

        Data data = schema.newData();
        data.set(time, 0.23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownFieldRetrieval() {
        Schema schema = new Schema(Arrays.asList(new Field[]{random, checked, sparse}), "n/a");

        Data data = schema.newData();
        data.get(time);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalFieldAssignment() {
        Schema schema = new Schema(Arrays.asList(new Field[]{time, random, checked, sparse}), "n/a");

        Data data = schema.newData();
        data.set(time, 0.23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDataSetModification() {
        Schema schema1 = new Schema(Arrays.asList(new Field[]{time, random, checked, sparse}), "n/a");
        Schema schema2 = new Schema(Arrays.asList(new Field[]{time, checked, sparse}), "n/a");

        DataSet dataset1 = new DataSet(schema1);
        assertEquals("The brand new data set shall be empty", 0, dataset1.getSize());
        dataset1.add(schema1.newData());
        assertEquals("Adding data point shall increase data set size", 1, dataset1.getSize());

        dataset1.add(schema2.newData());

    }

    @Test
    public void testMainUsage() {
        final Random generator = new Random();
        
        Schema schema = aSchema()
                .with(aField().named("time").ofType(INTEGER))
                .with(aField().named("random").ofType(DOUBLE))
                .with(aField().named("checked").ofType(BOOLEAN))
                .with(aField().named("sparse").ofType(STRING))
                .build();

        DataSet dataset = schema.newDataSet();

        for (int i = 0; i < 100; i++) {
            Data data = dataset.newData();
            data.set("time", i); 
            data.set("random", generator.nextGaussian());
            data.set("checked", generator.nextBoolean());
            if (i % 3 == 0) {
                data.set("sparse", "coucou"); 
            }
            dataset.add(data);
        }

        assertEquals(
                "Wrong count of records",
                100,
                dataset.getSize());


    }
}
