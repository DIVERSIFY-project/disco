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
/*
 */
package eu.diversify.disco.population.decorators;

import eu.diversify.disco.population.Population;
import java.util.List;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ImmutablePopulation extends AbstractPopulationDecorator {

  
    public ImmutablePopulation(Population population) {
        super(population);
    }

    @Override
    public boolean equals(Object obj) {
        return getDelegate().equals(obj);
    }

    @Override
    public int getNumberOfIndividualsIn(int specieIndex) {
        return getDelegate().getNumberOfIndividualsIn(specieIndex);
    }

    @Override
    public int getNumberOfIndividualsIn(String specieName) {
        return getDelegate().getNumberOfIndividualsIn(specieName);
    }

    @Override
    public int getNumberOfSpecies() {
        return getDelegate().getNumberOfSpecies();
    }

    @Override
    public int getSpecieIndex(String specieName) {
        return getDelegate().getSpecieIndex(specieName);
    }

    @Override
    public double getMeanNumberOfIndividuals() {
        return getDelegate().getMeanNumberOfIndividuals();
    }

    @Override
    public int getTotalNumberOfIndividuals() {
        return getDelegate().getTotalNumberOfIndividuals();
    }

    @Override
    public double getFractionIn(int specieIndex) {
        return getDelegate().getFractionIn(specieIndex);
    }

    @Override
    public double getFractionIn(String specieName) {
        return getDelegate().getFractionIn(specieName);
    }

    @Override
    public boolean hasAnySpecieNamed(String specieName) {
        return getDelegate().hasAnySpecieNamed(specieName);
    }

    @Override
    public int hashCode() {
        return getDelegate().hashCode();
    }

    @Override
    public boolean isEmpty() {
        return getDelegate().isEmpty();
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
    public Population removeSpecie(String specieName) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.removeSpecie(specieName));
    }

    @Override
    public Population setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.setNumberOfIndividualsIn(specieIndex, numberOfIndividuals));
    }

    @Override
    public Population shiftNumberOfIndividualsIn(int specieIndex, int offset) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.shiftNumberOfIndividualsIn(specieIndex, offset));
    }

    @Override
    public Population shiftNumberOfIndividualsIn(String specieName, int offset) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.shiftNumberOfIndividualsIn(specieName, offset));
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
    public Population renameSpecie(String oldName, String newName) {
        Population copy = getDelegate().deepCopy();
        return new ImmutablePopulation(copy.renameSpecie(oldName, newName));
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
