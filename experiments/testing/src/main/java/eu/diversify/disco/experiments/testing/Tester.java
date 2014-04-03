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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Generic acceptance test for experiments.
 */
public class Tester {

    private final String experimentName;
    private final ArrayList<String> expectedCsvFiles;
    private final ArrayList<String> expectedPdfFiles;

    public Tester(String experimentName, String... expectedFiles) {
        this.experimentName = experimentName;
        this.expectedCsvFiles = new ArrayList<String>();
        this.expectedPdfFiles = new ArrayList<String>();
        for (String file : expectedFiles) {
            final String extension = getExtension(file);
            if (extension.equals(".csv")) {
                expectedCsvFiles.add(file);
            }
            else if (extension.equals(".pdf")) {
                expectedPdfFiles.add(file);
            }
            else {
                String message = String.format("Unsupported file extension '%s' (file: %s)", extension, file);
                throw new IllegalArgumentException(message);
            }
        }
    }

    private String getExtension(String file) {
        final Pattern regex = Pattern.compile("[^\\.]+(\\.\\w+)\\s*$");
        final Matcher matcher = regex.matcher(file);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        else {
            final String message = String.format("Unable to extract extension from '%s'", file);
            throw new IllegalArgumentException(message);
        }
    }
    
    public List<String> getExpectedCsvFiles() {
        return Collections.unmodifiableList(expectedCsvFiles);
    }
    
    public List<String> getExpectedPdfFiles() {
        return Collections.unmodifiableList(expectedPdfFiles);
    }

    public void test() throws IOException, InterruptedException {
        unzipDistribution(getDistributionName());
        final Run execution = new Run(getWorkingDirectory(), getRunCommand());
        checkReadMeFile();
        checkLicenseAndCopyrightAreDisplayed(execution);
        checkNoErrorIsReported(execution);
        checkCsvFilesAreGenerated();
        checkPdfAreGenerated();
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

    private void checkReadMeFile() throws IOException {
        File readme = new File(String.format("target/%s/README", experimentName));
        assertThat("readme provided", readme.exists());

        final String content = readContentOf(readme);
        assertThat("is about experiment", content, containsString(experimentName));
        assertThat("explain how to run", content, containsString(getRunCommand()));

    }

    private String readContentOf(File readme) throws IOException {
        final StringBuffer buffer = new StringBuffer();
        for (String line : Files.readAllLines(readme.toPath(), Charset.defaultCharset())) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    private void checkCsvFilesAreGenerated() {
        for (String name : expectedCsvFiles) {
            final String path = String.format("target/%s/%s", experimentName, name);
            File csvFile = new File(path);
            assertThat("CSV generated", csvFile.exists());
        }
    }

    private void checkPdfAreGenerated() {
        for (String name : expectedPdfFiles) {
            final String path = String.format("target/%s/%s", experimentName, name);
            File pdfFile = new File(path);
            assertThat("PDF generated", pdfFile.exists());
        }
    }

    private void checkNoErrorIsReported(Run execution) {
        assertThat("no error reported in stdout", execution.getStandardOutput(), not(containsString("Error")));
        assertThat("no error reported in stdout", execution.getStandardError(), not(containsString("Error")));
    }

    private void checkLicenseAndCopyrightAreDisplayed(Run execution) {
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

        private StringBuilder readStandardOutput(Process p) throws IOException {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line = null;
            while ((line = stdInput.readLine()) != null) {
                output.append(line);
            }
            return output;
        }

        private StringBuilder readStandardError(Process p) throws IOException {
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