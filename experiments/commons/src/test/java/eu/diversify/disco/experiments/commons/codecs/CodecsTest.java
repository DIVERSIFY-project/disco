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
package eu.diversify.disco.experiments.commons.codecs;

import eu.diversify.disco.experiments.commons.codecs.Codecs;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Test the behaviour of codecs
 */
@RunWith(JUnit4.class)
public class CodecsTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DataSet ds;
    
    @Before
    public void setup() {
                // Create a schema for the data set
        Field time = new Field("time", Integer.class);
        Field rnorm = new Field("rnorm", Double.class);
        Schema schema = new Schema(Arrays.asList(new Field[]{time, rnorm}), "n/a");

        // Create and populate a data set
        ds = new DataSet(schema, "test");
        Data d = schema.newData();
        d.set(time, 1);
        d.set(rnorm, 0.34);
        ds.add(d);
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUnknownExtension() throws FileNotFoundException, IOException {
        Codecs codecs = new Codecs();
        codecs.saveAs(ds, "test.json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingExtension() throws FileNotFoundException, IOException {
        Codecs codecs = new Codecs();
        codecs.saveAs(ds, "test");
    }
    

}
