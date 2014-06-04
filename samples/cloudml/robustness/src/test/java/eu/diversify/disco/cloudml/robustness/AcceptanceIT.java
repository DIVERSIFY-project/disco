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
package eu.diversify.disco.cloudml.robustness;

import eu.diversify.disco.cloudml.robustness.testing.Run;
import eu.diversify.disco.cloudml.robustness.testing.RunInThread;
import java.io.*;
import junit.framework.TestCase;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.disco.cloudml.robustness.testing.DidNotRaiseAnyException.didNotReportAnyError;
import static eu.diversify.disco.cloudml.robustness.testing.DidShowRobustness.didShowRobustness;
import static eu.diversify.disco.cloudml.robustness.testing.DidShowCopyright.didShowCopyright;
import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of user acceptance tests
 */
@RunWith(JUnit4.class)
public class AcceptanceIT extends TestCase {

    private static final String EXTINCTION_SEQUENCE_CSV = "extinction_sequence.csv";

    @Test
    public void robustnessShouldBeOneForASingleVM() throws IOException, InterruptedException {
        final String testFile = "test.json";
        createModelWithASingleVM(testFile);

        Run run = RunInThread.withArguments(testFile);

        assertThat(run, didShowCopyright(2014, "SINTEF ICT"));
        assertThat(run, didNotReportAnyError());
        assertThat(run, didShowRobustness(100.0));

        ExtinctionSequence sequence = ExtinctionSequence.fromCsvFile(EXTINCTION_SEQUENCE_CSV);
        assertThat(sequence, is(not(nullValue())));

        deleteFiles(testFile, EXTINCTION_SEQUENCE_CSV);
    }

    private void createModelWithASingleVM(String location) throws FileNotFoundException {
        Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("My VM")
                        .providedBy("Ec2"))
                .build();

        new CodecsLibrary().saveAs(deployment, location);
    }

    @Test
    public void robustnessShouldBe75ForTwoSeparateVMs() throws IOException, InterruptedException {
        final String testFile = "test.json";
        createModelWithTwoSeparateVMs(testFile);

        Run run = RunInThread.withArguments(testFile);

        assertThat(run, didShowCopyright(2014, "SINTEF ICT"));
        assertThat(run, didNotReportAnyError());
        assertThat(run, didShowRobustness(75.0));

        ExtinctionSequence sequence = ExtinctionSequence.fromCsvFile(EXTINCTION_SEQUENCE_CSV);
        assertThat(sequence, is(not(nullValue())));

        deleteFiles(testFile, EXTINCTION_SEQUENCE_CSV);
    }

    private void createModelWithTwoSeparateVMs(String location) throws FileNotFoundException {
        Deployment deployment = aDeployment()
                .with(aProvider().named("Ec2"))
                .with(aVM()
                        .named("VM #1")
                        .providedBy("Ec2"))
                .with(aVM()
                        .named("VM #2")
                        .providedBy("Ec2"))
                .build();

        new CodecsLibrary().saveAs(deployment, location);
    }

    public void deleteFiles(String... fileNames) {
        for (String eachFile: fileNames) {
            final File file = new File(eachFile);
            file.delete();
        }
    }

}
