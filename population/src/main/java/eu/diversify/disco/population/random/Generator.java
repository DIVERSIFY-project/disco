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
package eu.diversify.disco.population.random;

import eu.diversify.disco.population.Population;
import java.util.Iterator;
import java.util.Random;

/**
 * Generate random populations according to a given profile
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Generator {

    /**
     * Generate a given number of populations
     *
     * @param count the number of population to generate
     * @param profile the profile describing the type of population to generate
     * @return a collection of population
     */
    public Iterator<Population> makeMany(int count, Profile profile) {
        return new IterativeGenerator(this, count, profile);
    }

    /**
     * Generate a single population with respect to the given profile
     *
     * @param profile the profile that the resulting population shall match
     * @return a population that match the given profile
     */
    public Population makeOne(Profile profile) {
        final Random random = new Random();
        
        final int s = profile.getNumberOfSpecies().sample();
        final int n = profile.getNumberOfIndividuals().sample();

        final int[] distribution  = new int[s];
        for(int i=0 ; i<n ; i++) {
            distribution[random.nextInt(s)] += 1;
        }
        
        return Population.fromDistribution(distribution);
    }

    /**
     *
     */
    private static class IterativeGenerator implements Iterator<Population> {

        private final Generator generator;
        private final int capacity;
        private final Profile profile;
        private int count = 0;

        public IterativeGenerator(Generator generator, int count, Profile profile) {
            this.generator = generator;
            this.capacity = count;
            this.profile = profile;
        }

        @Override
        public boolean hasNext() {
            return capacity > count;
        }

        @Override
        public Population next() {
            count++;
            return this.generator.makeOne(profile);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported for generated population."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
