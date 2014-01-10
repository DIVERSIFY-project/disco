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

import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.controller.Evaluation;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.QuadraticMean;

/**
 * Run the various controller and measure the number of step they use to
 * diversify a given population
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println(DISCLAIMER);
        System.out.println("");
        
        final Population population = new Population();
        population.addSpecie("Hippos", 10);
        population.addSpecie("Snails", 14);
        population.addSpecie("Sludges", 32);
        population.addSpecie("Crocodiles", 5);

        final double reference = 0.14;
        final Controller controller = new HillClimber(new QuadraticMean());
        
        System.out.println("Initial population:");
        System.out.println(controller.evaluate(0, population, reference));

        System.out.println("After Hill Climbing:");
        Evaluation result = controller.applyTo(population, 0.14);
        System.out.println(result);

        System.out.println("That's all folks!");
    }
}
