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
package eu.diversify.disco.population;

import eu.diversify.disco.population.constraints.Constraint;
import java.util.Collection;
import java.util.List;

/**
 * Ensure immutability of a population, by return a copy of the population
 * on prepare update
 */
public class ImmutablePopulation extends MutablePopulation {

    public ImmutablePopulation(List<String> speciesNames, List<Integer> distribution, Collection<Constraint> constraints) {
        super(speciesNames, distribution, constraints);
    }

    @Override
    public MutablePopulation prepareUpdate() {
        return (MutablePopulation) deepCopy().build();
    }

    @Override
    public PopulationBuilder deepCopy() {
        return super.deepCopy().immutable();
    }
    
    

}
