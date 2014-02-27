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
package eu.diversify.disco.experiments.commons;

import eu.diversify.disco.experiments.commons.codecs.Codecs;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.errors.RScriptNotFound;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.yaml.snakeyaml.Yaml;

/**
 * Run a experiment identified by the class of its setup
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Runner {

    private final static String VIEW_R = "view.r";
    private final static String R_SCRIPT = "RScript";
    private final static String RESULTS_EXTENSION = ".csv";
    private final static String ABORT_COMMAND = "!abort";
    private final static String DISCLAIMER = "Disco v0.1\n"
            + "Copyright (C) 2013 SINTEF ICT\n\n"
            + "License LGPLv3+: GNU LGPL version 3 or later <http://gnu.org/licenses/lgpl.html>\n"
            + "This is free software: you are free to change and redistribute it.\n"
            + "There is NO WARRANTY, to the extent permitted by law.\n";
    private final static String CLOSING_MESSAGE = "That's all folks!";
    private final static String DEFAULT_SETUP_FILE = "setup.yml";

    /**
     * To run an experiment, we first load the setup file, build the associated
     * experiment, run it, serialise the results and generate their
     * visualisation.
     *
     * @param setupClass the class encapsulating the setup of the experiment
     *
     * @param setupFile the location of file containing the actual setup
     */
    public void run(Class setupClass, String setupFile) {
        System.out.println(DISCLAIMER);
        Setup setup = loadExperimentSetup(setupClass, setupFile);
        Experiment experiment = setup.buildExperiment();
        saveResults(experiment.run());
        generateVisualisations();
        System.out.println(CLOSING_MESSAGE);
    }

    /**
     * Run the first setup file found within the arguments list or the default
     * one if none is found.
     *
     * @param setupClass the Class of the setup
     * @param commandLineArguments the command line arguments
     */
    public void run(Class setupClass, String... commandLineArguments) {
        System.out.println(DISCLAIMER);
        List<String> setupFiles = extractSetupFiles(commandLineArguments);
        run(setupClass, setupFiles.get(0));
        /*
         * TODO: Should ideally run all the given setup files. So far it run
         * only the first one as in the following
         *
         * for (String setupFile : setupFiles) { run(setupClass, setupFile); }
         *
         */
        System.out.println(CLOSING_MESSAGE);
    }

    /**
     * Extract the setup files specified in the command line arguments. If none
     * are specified, return a list containing only de the default setup file.
     *
     * @param args the commands line arguments
     * @return the list of setup file to run
     */
    List<String> extractSetupFiles(String[] args) {
        ArrayList<String> setupFiles = new ArrayList<String>();
        for (String arg : args) {
            if (isSetupFile(arg)) {
                setupFiles.add(arg);
            }
        }
        if (setupFiles.isEmpty()) {
            setupFiles.add(DEFAULT_SETUP_FILE);
        }
        return setupFiles;
    }

    private boolean isSetupFile(String text) {
        return text.matches("[^\\.]*\\.(yaml|yml)$");
    }

    /**
     * To generate the the visualisation we call the RScript application,
     * assuming their is a 'view.r' script available in the current directory.
     */
    private void generateVisualisations() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(String.format("%s %s", R_SCRIPT, VIEW_R));
            final int errorCode = process.waitFor();
            System.out.println("Error code: " + errorCode);

        } catch (IOException ex) {
            System.out.println("ERROR: Unable to generate visualisation using R. Is R properly installed?'");
            throw new RScriptNotFound();

        } catch (InterruptedException ex) {
            System.out.println("Error while generating the visualisations.");
            System.out.println(" -> " + ex.getMessage());

        }
    }

    /**
     * To build the results of the experiment, we create a codecs and request
     * the serialisation in CSV, for each dataset.
     *
     * @param results the collections of results to serialise
     */
    private void saveResults(Collection<DataSet> results) {
        Codecs codecs = new Codecs();
        for (DataSet result : results) {
            String fileName = result.getName() + RESULTS_EXTENSION;
            try {
                codecs.saveAs(result, fileName);

            } catch (FileNotFoundException ex) {
                System.out.println("ERROR: Unable to write or create ' " + fileName + "'");
                System.out.println(" -> " + ex.getMessage());

            } catch (IOException ex) {
                System.out.println("ERROR: Unable to write or create ' " + fileName + "'");

            }
        }
    }

    /**
     * To load the setup file, we create a YAML parser and use it create the
     * setup object. If the given file does not exist, we prompt the user for
     * another, as long as we cannot read the given file.
     *
     * @param setupClass the class of setup object to build
     * @param setupFile the file to load
     *
     * @return the resulting setup object
     */
    private Setup loadExperimentSetup(Class setupClass, String setupFile) {
        final Yaml yaml = new Yaml();
        Setup setup = null;
        boolean abortRequested = false;
        while (setup == null && !abortRequested) {
            try {
                setup = (Setup) yaml.loadAs(new FileInputStream(setupFile), setupClass);

            } catch (FileNotFoundException ex) {
                System.out.println("ERROR: Unable to locate the setup file '" + setupFile + "'");
                System.out.println(" -> " + ex.getMessage());

                setupFile = promptForSetupFile().trim();
                if (setupFile.equals(ABORT_COMMAND)) {
                    abortRequested = true;
                }
            }
        }
        return setup;
    }

    /**
     * Prompt the user for a setup file to read or to abort the experiment
     *
     * @return the string inputed by the user
     */
    private String promptForSetupFile() {
        BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the path to the setup file: ('" + ABORT_COMMAND + "' to abort) ");
        String answer;
        try {
            answer = commandLine.readLine();

        } catch (IOException ex) {
            System.out.println("I/O ERROR while reading the command line. Quiting");
            answer = ABORT_COMMAND;
        }
        return answer;
    }
}
