
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
