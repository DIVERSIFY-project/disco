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
package eu.diversify.disco.experiments.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Generic acceptance test for experiments. The story goes as follows:
 */
public class Tester {

    private final String experimentName;
    private final ArrayList<String> expectedFiles;

    public Tester(String experimentName, String... expectedFiles) {
        this.experimentName = experimentName;
        this.expectedFiles = new ArrayList<String>(Arrays.asList(expectedFiles));
    }

    public void test() throws IOException, InterruptedException {
        unzipDistribution(getDistributionName());
        final Run execution = new Run(getWorkingDirectory(), getRunCommand());
        checkLicensing(execution);
        checkNoError(execution);
        checkCsvOutput();
        checkPdfOutput();
    }

    private void unzipDistribution(String archiveName) throws IOException {
        String fileName = escape(archiveName);
        ZipFile zipFile = new ZipFile(fileName);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                File entryDestination = new File("target/", entry.getName());
                entryDestination.getParentFile().mkdirs();
                InputStream in = zipFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }
    }

    private void checkCsvOutput() {
        for (String name : expectedFiles) {
            final String path = String.format("target/%s/%s.csv", experimentName, name);
            File csvFile = new File(path);
            assertThat("CSV generated", csvFile.exists());
        }
    }

    private void checkPdfOutput() {
        for (String name : expectedFiles) {
            final String path = String.format("target/%s/%s.pdf", experimentName, name);
            File pdfFile = new File(path);
            assertThat("PDF generated", pdfFile.exists());
        }
    }

    private void checkNoError(Run execution) {
        assertThat("no error reported in stdout", execution.getStandardOutput(), not(containsString("Error")));
        assertThat("no error reported in stdout", execution.getStandardError(), not(containsString("Error")));
    }

    private void checkLicensing(Run execution) {
        assertThat("copyright displayed", execution.getStandardOutput(), containsString("Copyright (C) 2013 SINTEF ICT"));
        assertThat("license displayed", execution.getStandardOutput(), containsString("LGPLv3+"));
    }

    private String escape(String directory) {
        return directory.replaceAll("\"", "");
    }

    public String getDistributionName() {
        return String.format("%s-dist.zip", experimentName);
    }

    public String getWorkingDirectory() {
        return String.format("target/%s", experimentName);
    }

    public String getRunCommand() {
        return String.format("java -jar %s-final.jar", experimentName);
    }

    private static final class Run {

        private final String standardOutput;
        private final String standardError;

        public Run(String directory, String commandLine) throws IOException, InterruptedException {
            ProcessBuilder builder = new ProcessBuilder(commandLine.split("\\s+"));
            builder.directory(new File(directory));
            Process p = builder.start();
            standardOutput = readStandardOutput(p).toString();
            standardError = readStandardError(p).toString();
            p.waitFor();
        }

        public String getStandardOutput() {
            return standardOutput;
        }

        public String getStandardError() {
            return standardError;
        }

        public StringBuilder readStandardOutput(Process p) throws IOException {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line = null;
            while ((line = stdInput.readLine()) != null) {
                output.append(line);
            }
            return output;
        }

        public StringBuilder readStandardError(Process p) throws IOException {
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder error = new StringBuilder();
            String line = "";
            while ((line = stdError.readLine()) != null) {
                error.append(line);
            }
            return error;
        }
    }
}