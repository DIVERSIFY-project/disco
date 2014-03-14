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
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.util.DotPrinter;
import eu.diversify.disco.controller.problem.Solution;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;

/**
 * Run the transformation from a DeploymentModel to a population model,
 * diversify the population model, and adjust the DeploymentModel model.
 *
 * @author Franck Chauvel
 * @author Hui Song
 *
 * @since 0.1
 */
public class Runner {

    public final static String DISCLAIMER = "Disco v0.1\n"
            + "Copyright (C) 2013 SINTEF ICT\n\n"
            + "License LGPLv3+: GNU LGPL version 3 or later <http://gnu.org/licenses/lgpl.html>\n"
            + "This is free software: you are free to change and redistribute it.\n"
            + "There is NO WARRANTY, to the extent permitted by law.\n";
    public static final double DEFAULT_DIVERSITY_REFERENCE = 0.25;
    public static final String DEFAULT_CLOUDML_MODEL = "sensapp.json";

    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.run(args);
    }

    private ArrayList<String> filesToLoad;
    private double diversity;

    public Runner() {
        this.filesToLoad = new ArrayList<String>();
        this.filesToLoad.add(DEFAULT_CLOUDML_MODEL);
        this.diversity = DEFAULT_DIVERSITY_REFERENCE;
    }

    public void run(String[] arguments) {
        System.out.println(DISCLAIMER);

        final Options options = Options.fromCommandLineArguments(arguments);
        options.launchDiversityController();
    }

}
