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

package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.MdmsModelCreator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Sandbox for experimenting with the code
 */
@Ignore
@RunWith(JUnit4.class)
public class SandboxTest {

    
    @Test
    public void sandbox() throws FileNotFoundException {
        JsonCodec codec = new JsonCodec();
        
        Deployment mdms = new MdmsModelCreator().create();
            codec.save(mdms, new FileOutputStream("../src/test/resources/mdms.json"));
        
    }

}