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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.diversify.disco.experiments.commons;

import eu.diversify.disco.experiments.commons.codecs.Codecs;
import eu.diversify.disco.experiments.commons.data.DataSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * Run a experiment identified by the class of its setup
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Runner {

    final static String DISCLAIMER = "Disco v0.1\n"
            + "Copyright (C) 2013 SINTEF ICT\n\n"
            + "License LGPLv3+: GNU LGPL version 3 or later <http://gnu.org/licenses/lgpl.html>\n"
            + "This is free software: you are free to change and redistribute it.\n"
            + "There is NO WARRANTY, to the extent permitted by law.";

    /**
     * Run load the setup of the experiment and call the R to generate
     * visualisations of the results
     *
     * @param setupClass the class encapsulating the setup of the experiment
     *
     * @param setupFile the location of file containing the actual setup
     */
    public void run(Class setupClass, String setupFile) {
        System.out.println(DISCLAIMER);
        System.out.println("");

        Yaml yaml = new Yaml();
        Setup setup = null;
        try {
            setup = (Setup) yaml.loadAs(new FileInputStream(setupFile), setupClass);

        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to load setup file");
            System.out.println(" -> " + ex.getMessage());
            System.exit(-1);
        }

        Experiment experiment = setup.buildExperiment();
        Codecs codecs = new Codecs();
        for (DataSet result : experiment.run()) {
            String fileName = result.getName() + ".csv";
            try {
                codecs.saveAs(result, fileName);

            } catch (FileNotFoundException ex) {
                System.out.println("ERROR: Unable to write or create ' " + fileName + "'");
                System.out.println(" -> " + ex.getMessage());
                System.exit(-1);

            } catch (IOException ex) {
                System.out.println("ERROR: Unable to write or create ' " + fileName + "'");
                System.exit(-1);
            }
        }

        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("RScript view.r");
            process.waitFor();

        } catch (IOException ex) {
            System.out.println("ERROR: Unable to generate visualisation with R '");
            System.out.println(" -> " + ex.getMessage());
            System.exit(-1);

        } catch (InterruptedException ex) {
            System.out.println("ERROR: Unable to generate visualisation with R");
            System.out.println(" -> " + ex.getMessage());
            System.exit(-1);

        }

        System.out.println("That's all folks!");
    }
}
