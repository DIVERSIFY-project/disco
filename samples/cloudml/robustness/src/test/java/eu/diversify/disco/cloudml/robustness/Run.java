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

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Run the robustness calculator and collect the standard output and the
 * standard error.
 */
public class Run {

    public static double extractRobustness(String text) {
        final Matcher matcher = Pattern.compile("Robustness:\\s*(\\d+\\.\\d+)\\s*%").matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return -1;
    }

    public static Run withArguments(String fileName) throws IOException, InterruptedException {
        return new Run(".", new String[]{"java", "-jar", "robustness-final.jar",
                                         fileName});

    }

    private final String[] commandLine;
    private final OutputCollector standardOutput;
    private final OutputCollector standardError;

    private Run(String workingDirectory, String[] arguments) throws IOException, InterruptedException {
        commandLine = arguments;
        ProcessBuilder builder = new ProcessBuilder(arguments);
        builder.directory(new File(workingDirectory));
        Process p = builder.start();
        standardOutput = new OutputCollector(p.getInputStream());
        standardError = new OutputCollector(p.getErrorStream());
        collectOutputs(p);
        p.waitFor();
    }

    private void collectOutputs(Process p) throws IOException, InterruptedException {
        p.getOutputStream().close();
        standardError.start();
        standardOutput.start();
        standardOutput.join();
        standardError.join();
    }

    private String showDetails() {
        return String.format("\nCommand: %s\n-----\nSTDOUT:\n%s\n----\nSTDERR:\n%s", formatCommandLine(), getStandardOutput(), getStandardError());
    }

    private String formatCommandLine() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < commandLine.length; i++) {
            result.append(commandLine[i]);
            if (i < commandLine.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public void displayedRobustnessShouldBe(double expectation) {
        final double displayedRobustness = Run.extractRobustness(getStandardOutput());
        assertThat(hint("displayed robustness"), displayedRobustness, is(closeTo(expectation, 0.05)));
    }

    private String hint(String message) {
        return showDetails() + "\n" + message;
    }

    void extinctionSequenceShouldBeAvailableIn(String fileName) {
        final File file = new File(fileName);
        assertThat(hint("extinction sequence"), file.exists());
    }

    void deleteGeneratedFiles() {
        new File("extinction_sequence.csv").delete();
    }

    private String getStandardOutput() {
        return standardOutput.getOutput();
    }

    private String getStandardError() {
        return standardError.getOutput();
    }

    private static final String[] keywords = new String[]{
        "ERROR", "Error", "error", "Exception", "exception"
    };

    public void noErrorShouldBeReported() {
        for (String eachKeyword: keywords) {
            assertThat(hint("no error"), getStandardError(), not(containsString(eachKeyword)));
            assertThat(hint("no error"), getStandardOutput(), not(containsString(eachKeyword)));
        }
    }

}
