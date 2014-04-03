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
package eu.diversify.disco.experiments.population.sensitivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class AcceptanceIT {

    private String[] expectedOutputs = new String[]{
        "species",
        "individuals",
        "distribution"};
    private String directory;
    private String standardOutput;
    private String errorOutput;
    public String experimentName = "sensitivity";

    @Test
    public void acceptanceTest() throws IOException, InterruptedException {
        unzipDistribution(getDistributionName());
        setUpDirectory(getWorkingDirectory());
        runCommand(getRunCommand());
        checkLicensing();
        checkCsvOutput();
        checkPdfOutput();
        checkNoError();
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

    private void setUpDirectory(String directory) {
        this.directory = escape(directory);
    }

    private void runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(escape(command).split("\\s+"));
        builder.directory(new File(directory));
        Process p = builder.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        standardOutput = "";
        String line = null;
        while ((line = stdInput.readLine()) != null) {
            standardOutput += line;
        }

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        errorOutput = "";
        while ((line = stdError.readLine()) != null) {
            errorOutput = "";
        }

        p.waitFor();
    }

    private void checkCsvOutput() {
        for (String name : expectedOutputs) {
            final String path = String.format("target/sensitivity/%s.csv", name);
            File csvFile = new File(path);
            assertThat("CSV generated", csvFile.exists());
        }
    }

    private void checkPdfOutput() {
        for (String name : expectedOutputs) {
            final String path = String.format("target/sensitivity/%s.pdf", name);
            File pdfFile = new File(path);
            assertThat("PDF generated", pdfFile.exists());
        }
    }

    private void checkNoError() {
        assertThat("no error reported in stdout", standardOutput, not(containsString("Error")));
        assertThat("no error reported in stdout", errorOutput, not(containsString("Error")));
    }

    private void checkLicensing() {
        assertThat("copyright displayed", standardOutput, containsString("Copyright (C) 2013 SINTEF ICT"));
        assertThat("license displayed", standardOutput, containsString("LGPLv3+"));
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
}
