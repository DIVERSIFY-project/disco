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
package eu.diversify.disco.experiments.population.sensitivity;

import eu.diversify.disco.population.diversity.GiniSimpsonIndex;
import eu.diversify.disco.population.diversity.QuadraticMean;
import eu.diversify.disco.population.diversity.ShannonIndex;
import java.io.FileNotFoundException;

/**
 * Run the sensitivity analysis, both regarding sensitivity to individual
 * variations and to species variations.
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

    public static void main(String args[]) {
        System.out.println(DISCLAIMER);
        System.out.println("");
        
        
        System.out.println("Sensitivity to individual distribution ...");
        Individuals exp1 = new Individuals();
        exp1.addMetric("True Diversity", new QuadraticMean());
        exp1.addMetric("Shannon Index", new ShannonIndex());
        exp1.addMetric("Gini-Simpson Index", new GiniSimpsonIndex());

        exp1.setPopulationSize(100);
        exp1.run();
        try {
            exp1.saveResultAs("individuals.csv");
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to save results in 'individual.csv'");
        }
        System.out.println("Complete.");
        
        System.out.println("");
        System.out.println("Sensitivity to species count variations ...");
        
        Species exp2 = new Species();
        exp2.addMetric("True Diversity", new QuadraticMean());
        exp2.addMetric("Shannon Index", new ShannonIndex());
        exp2.addMetric("Gini-Simpson Index", new GiniSimpsonIndex());

        exp2.setPopulationSize(100);
        exp2.run();
        try {
            exp2.saveResultAs("species.csv");
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to save results in 'species.csv'");
        }
        
        System.out.println("Complete.");
        System.out.println("That's all folks!");
        
    }
    
    
}
