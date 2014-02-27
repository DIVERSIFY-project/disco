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
package eu.diversify.disco.experiments.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Test the behaviour of runner
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(Parameterized.class)
public class RunnerTest extends TestCase {

    public String commandLine;
    public List<String> expectedSetupFiles;

    public RunnerTest(String commandLine, List<String> setupFiles) {
        this.commandLine = commandLine;
        this.expectedSetupFiles = new ArrayList<String>(setupFiles);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[]{
            "java -jar test.jar setup.yml",
            splitAsList("setup.yml")
        });
        data.add(new Object[]{
            "java -jar test.jar setup1.yml setup2.yml",
            splitAsList("setup1.yml setup2.yml")
        });
        data.add(new Object[]{
            "java -jar test.jar setup1.yml setup2.yaml",
            splitAsList("setup1.yml setup2.yaml")
        });
        data.add(new Object[]{
            "java -jar test.jar setup1.yml setup-no-extension setup2.yaml",
            splitAsList("setup1.yml setup2.yaml")
        });
        data.add(new Object[]{
            "java -jar test.jar setup-no-extension",
            splitAsList("setup.yml") // Default setup file
        });
        data.add(new Object[]{
            "java -jar test.jar",
            splitAsList("setup.yml") // Default setup file
        });
        return data;
    }

    private static List<String> splitAsList(String text) {
        return Arrays.asList(splitAsArray(text));
    }

    private static String[] splitAsArray(String text) {
        return text.trim().split("\\s+");
    }

    @Test
    public void testExtractArgument() {
        String[] args = RunnerTest.splitAsArray(commandLine);
        Runner runner = new Runner();
        List<String> setupFiles = runner.extractSetupFiles(args);
        assertEquals(expectedSetupFiles, setupFiles);
    }
}
