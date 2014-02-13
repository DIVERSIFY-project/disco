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

package eu.diversify.disco.experiments.data;

import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.Schema;
import java.util.Arrays;
import java.util.Random;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    
    
    @Test(expected= IllegalArgumentException.class)
    public void testIllegalDataSchema() {
        Field time = new Field("time", Integer.class);
        Field time2 = new Field("time", Integer.class);
        Field checked = new Field("checked", Boolean.class);
        Field sparse = new Field("sparse", String.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{time, time2, checked, sparse}), "n/a");      
    }
    
    @Test(expected= IllegalArgumentException.class)
    public void testUnknownFieldAssignment() {
        Field time = new Field("time", Integer.class);
        Field random = new Field("random", Double.class);
        Field checked = new Field("checked", Boolean.class);
        Field sparse = new Field("sparse", String.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{random, checked, sparse}), "n/a");
        
        Data data = schema.newData();
        data.set(time, 0.23);
    }
    
    @Test(expected= IllegalArgumentException.class)    
    public void testUnknownFieldRetrieval() {
        Field time = new Field("time", Integer.class);
        Field random = new Field("random", Double.class);
        Field checked = new Field("checked", Boolean.class);
        Field sparse = new Field("sparse", String.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{random, checked, sparse}), "n/a");
        
        Data data = schema.newData();
        data.get(time);
    }

    @Test(expected= IllegalArgumentException.class)
    public void testIllegalFieldAssignment() {
        Field time = new Field("time", Integer.class);
        Field random = new Field("random", Double.class);
        Field checked = new Field("checked", Boolean.class);
        Field sparse = new Field("sparse", String.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{time, random, checked, sparse}), "n/a");
        
        Data data = schema.newData();
        data.set(time, 0.23);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalDataSetModification() {
        Field time = new Field("time", Integer.class);
        Field random = new Field("random", Double.class);
        Field checked = new Field("checked", Boolean.class);
        Field sparse = new Field("sparse", String.class);
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
        
        // Prepare the data schema
        Field time = new Field("time", Integer.class);
        Field random = new Field("random", Double.class);
        Field checked = new Field("checked", Boolean.class);
        Field sparse = new Field("sparse", String.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{time, random, checked, sparse}), "n/a");
        
        DataSet dataset = new DataSet(schema);
        
        for (int i=0 ; i<100 ; i++) {
            Data data = schema.newData();
            data.set(time, i);
            data.set(random, generator.nextGaussian());
            data.set(checked, generator.nextBoolean());
            if (i % 3 == 0) {
                data.set(sparse, "coucou");
            }
            dataset.add(data);
        }
        
        assertEquals(
                "Wrong count of records",
                100,
                dataset.getSize());
        
        
    }
    
}
