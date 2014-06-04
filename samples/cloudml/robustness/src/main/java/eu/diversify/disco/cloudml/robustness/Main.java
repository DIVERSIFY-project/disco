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
package eu.diversify.disco.cloudml.robustness;

import java.io.IOException;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;

/**
 * Entry point of the robustness calculator
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Copyright (c) 2014 - SINTEF ICT");
        
        Deployment deployment = new CodecsLibrary().load(args[0]);
        ExtinctionSequence sequence = new Robustness(deployment).getExtinctionSequence();
                
        System.out.println(sequence);
        sequence.toCsvFile("extinction_sequence.csv");

        System.out.printf("Robustness: %.2f %%\r\n", sequence.getRobustness());
    }

  
}
