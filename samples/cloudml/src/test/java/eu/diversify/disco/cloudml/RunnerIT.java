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
package eu.diversify.disco.cloudml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;

@RunWith(JUnit4.class)
public class RunnerIT extends TestCase {

    @Before
    public void setUp() throws IOException {
        Files.copy(Paths.get("../src/test/resources/mdms.json"), Paths.get("mdms.json"), StandardCopyOption.REPLACE_EXISTING);
    }
    
    @Test
    public void runWithMdms() {
        String[] params = new String[] {
            "mdms.json", "0.75"
        };
        Runner.main(params);
        
        assertThat("dot file created", new File("mdms.dot").exists(), is(true));
    }
    
 }