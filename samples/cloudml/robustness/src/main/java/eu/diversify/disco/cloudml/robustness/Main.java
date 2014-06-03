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
package eu.diversify.disco.cloudml.robustness;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Component;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.Property;
import org.cloudml.core.Relationship;
import org.cloudml.core.RequiredPort;

/**
 * Entry point of the robustness calculator
 */
public class Main {

    public static void main(String[] args) throws IOException {
        final Map<Integer, Integer> lifeLevels = new HashMap<Integer, Integer>();

        CodecsLibrary codecs = new CodecsLibrary();
        Deployment deployment = codecs.load(args[0]);

        Simulator simulator = new Simulator(deployment);
        
        final int max = deployment.getComponents().size();
        int lifeLevel = simulator.countAliveComponents();
        lifeLevels.put(0, lifeLevel);

        for (int extinctionLevel = 1; extinctionLevel <= max; extinctionLevel++) {
            simulator.killOneComponent();
            lifeLevel = simulator.countAliveComponents();
            lifeLevels.put(extinctionLevel, lifeLevel);
        }
                

        ExtinctionSequence sequence = ExtinctionSequence.fromMap(lifeLevels);
        
        System.out.println(sequence);
        sequence.toCsvFile("extinction_sequence.csv");

        System.out.printf("Robustness: %.2f %%\r\n", sequence.getRobustness());
    }

  
}
