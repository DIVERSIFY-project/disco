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
package eu.diversify.disco.experiments.controllers.singlerun;

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

    private String directory;
    private String standardOutput;
    private String errorOutput;

    
    @Test
    public void acceptanceTest() throws IOException, InterruptedException {
        unzipDistribution("singlerun-dist.zip");
        setUpDirectory("target/singlerun");
        runCommand("java -jar singlerun-final.jar");
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

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
        standardOutput = "";
        String line = null;
        while ((line = stdInput.readLine()) != null) {
            standardOutput += line;
        }

        // read any errors from the attempted command
        errorOutput = "";
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((line = stdError.readLine()) != null) {
            errorOutput = "";
        }
        
        p.waitFor();
    }

    private void checkCsvOutput() {
        File csvOutput = new File("target/singlerun/results.csv");
        assertThat("CSV generated", csvOutput.exists());
    }

    private void checkPdfOutput() {
        File pdfOutput = new File("target/singlerun/results.pdf");
        assertThat("PDF generated", pdfOutput.exists());
    }

    private void checkNoError() {
        //FIXME: assertThat("no error reported in stdout", standardOutput, not(containsString("Error")));
        assertThat("no error reported in stdout", errorOutput, not(containsString("Error")));
    }

    private String escape(String directory) {
        return directory.replaceAll("\"", "");
    }
}
