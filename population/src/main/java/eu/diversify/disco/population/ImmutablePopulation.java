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

import eu.diversify.disco.population.Population;
import java.util.List;

/**
 * Ensure immutability of a population
 *
 * We only need to override the mutators methods which accepts a specie index so
 * they return an new object and let the original one unchanged.
 *
 * Other mutators are expressed with respect to the other one in
 * AbstractDecorator
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ImmutablePopulation extends AbstractPopulationDecorator {

    public ImmutablePopulation(Population population) {
        super(population);
    }

    @Override
    public Population addSpecie() {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.addSpecie());
    }

    @Override
    public Population addSpecie(String specieName) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.addSpecie(specieName));
    }

    @Override
    public Population removeSpecie(int specieIndex) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.removeSpecie(specieIndex));
    }

    @Override
    public Population setHeadcountIn(int specieIndex, int numberOfIndividuals) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.setHeadcountIn(specieIndex, numberOfIndividuals));
    }

    @Override
    public Population shiftHeadcountIn(int specieIndex, int offset) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.shiftHeadcountIn(specieIndex, offset));
    }

    @Override
    public String toString() {
        return getDelegate().toString();
    }

    @Override
    public double[] toArrayOfFractions() {
        return getDelegate().toArrayOfFractions();
    }

    @Override
    public Population renameSpecie(int specieIndex, String newName) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.renameSpecie(specieIndex, newName));
    }

    @Override
    public Population deepCopy() {
        return new ImmutablePopulation((getDelegate().deepCopy()));
    }

    @Override
    public List<String> getSpeciesNames() {
        return getDelegate().getSpeciesNames();
    }

    @Override
    public List<Integer> getDistribution() {
        return getDelegate().getDistribution();
    }
}
