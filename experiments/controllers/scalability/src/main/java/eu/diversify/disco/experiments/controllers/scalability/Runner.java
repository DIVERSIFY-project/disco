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
package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Run the various controllers and measure the number of step they use to
 * diversify a given population.
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
    final static String INDIVIDUAL_SETUP = "setup_individuals.yml";
    final static String SPECIES_SETUP = "setup_species.yml";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println(DISCLAIMER);
        System.out.println("");

        System.out.println("Sensitivity to species count:");
        try {
            Experiment exp1 = new Experiment(SPECIES_SETUP);
            exp1.run();
            exp1.saveResultsAs("species-scalability.csv");

        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        
        } catch (ControllerInstantiationException ex) {
            System.err.println("ERROR: Unable to instantiate strategy '" + ex.getClassName());
        }


        System.out.println("Sensitivity to individuals counts:");
        try {
            Experiment exp2 = new Experiment(INDIVIDUAL_SETUP);
            exp2.run();
            exp2.saveResultsAs("individuals-scalability.csv");

        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        
        } catch (ControllerInstantiationException ex) {
            System.err.println("ERROR: Unable to instantiate strategy '" + ex.getClassName());
        }


        System.out.println("That's all folks!");
    }
}
