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
package eu.diversify.disco.cloudml.robustness.testing;

import java.io.File;
import java.io.IOException;

/**
 * RunInThread the robustness calculator and collect the standard output and the
 standard error.
 */
public class RunInThread implements Run {


    public static RunInThread withArguments(String fileName) throws IOException, InterruptedException {
        return new RunInThread(".", new String[]{"java", "-jar", "robustness-final.jar",
                                         fileName});

    }

    private final String[] commandLine;
    private final OutputCollector standardOutput;
    private final OutputCollector standardError;

    public RunInThread(String workingDirectory, String[] arguments) throws IOException, InterruptedException {
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

    @Override
    public String toString() {
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
 
  
    @Override
    public String getStandardOutput() {
        return standardOutput.getOutput();
    }

    @Override
    public String getStandardError() {
        return standardError.getOutput();
    }

  
}
