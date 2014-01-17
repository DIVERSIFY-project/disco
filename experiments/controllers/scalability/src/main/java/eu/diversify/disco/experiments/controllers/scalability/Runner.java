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

package eu.diversify.disco.experiments.controllers.scalability;

import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.BreadthFirstExplorer;
import eu.diversify.disco.controller.Case;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.FileNotFoundException;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println(DISCLAIMER);
        System.out.println("");

        System.out.println("Sensitivity to species count:");
        Experiment exp1 = new Experiment();
        exp1.setIndividualsCount(new int[]{2000});
        exp1.setSpeciesCounts(new int[]{2, 4, 8, 16, 32, 64 /**, 128, 256, 512, 1024 **/});
        exp1.addController("Hill Climber", new HillClimber(new TrueDiversity()));
        exp1.addController("Adaptive Hill Climber", new AdaptiveHillClimber(new TrueDiversity()));               
        exp1.run();
        try {
            exp1.saveResultsAs("species-scalability.csv"); 
        
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to write 'species-scalability.csv'");
        }
        
        
        System.out.println("Sensitivity to species count:");
        Experiment exp2 = new Experiment();
        exp2.setIndividualsCount(new int[]{25, 50, 100, 200, 400, 800, 1600, 3200, 6400, 12800});
        exp2.setSpeciesCounts(new int[]{25});
        exp2.addController("Hill Climber", new HillClimber(new TrueDiversity()));
        exp2.addController("Adaptive Hill Climber", new AdaptiveHillClimber(new TrueDiversity()));               
        exp2.run();
        try {
            exp2.saveResultsAs("individuals-scalability.csv"); 
        
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to write 'individuals-scalability.csv'");
        }
        
        
        System.out.println("That's all folks!");
    }
    
}
