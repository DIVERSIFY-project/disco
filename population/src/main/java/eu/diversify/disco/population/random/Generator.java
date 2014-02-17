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

package eu.diversify.disco.population.random;

import eu.diversify.disco.population.Population;
import java.util.Iterator;
import java.util.Random;

/**
 * Generate random population
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class Generator {


    
    public Iterator<Population> many(int count, Profile profile) {
        return new PartialGenerator(count, profile);
    }
    
    
    private static class PartialGenerator implements Iterator<Population> {

        private final int capacity;
        private final Profile profile;
        private int count = 0;
        
        public PartialGenerator(int count, Profile profile) {
            this.capacity = count;
            this.profile = profile;
        }
        
        @Override
        public boolean hasNext() {
            return capacity > count;
        }

        @Override
        public Population next() {
            Population result = new Population();
            int s = profile.getSpeciesCount().sample();
            int n = profile.getIndividualsCount().sample();
            Random random = new Random();
            double counts[] = new double[s];
            double total = 0;
            for (int i=0 ; i<s ; i++) {
                counts[i] = random.nextDouble();
                total += counts[i];
            }            
            for (int i=0 ; i<s ; i++) {
                result.addSpecie("specie " + (i+1), (int) Math.round((counts[i] / total * n)));
            }
            
            count++;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
