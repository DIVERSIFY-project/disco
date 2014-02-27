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
 * Factor the common code out of decorators object, and provide default implementations for some 
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class AbstractPopulationDecorator implements Population {
    
    private Population delegate;
    
    public AbstractPopulationDecorator(Population delegate) {
        this.delegate = delegate;        
    }
    
    protected Population getDelegate() {
        return this.delegate;
    }
    
    protected void setDelegate(Population newDelegate) {
        this.delegate = newDelegate;
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public Population deepCopy() {
        return delegate.deepCopy();
    }

    @Override
    public int getNumberOfIndividualsIn(int specieIndex) {
        return delegate.getNumberOfIndividualsIn(specieIndex);
    }

    @Override
    public int getNumberOfIndividualsIn(String specieName) {
        return delegate.getNumberOfIndividualsIn(specieName);
    }

    @Override
    public int getNumberOfSpecies() {
        return delegate.getNumberOfSpecies();
    }

    @Override
    public int getSpecieIndex(String specieName) {
        return delegate.getSpecieIndex(specieName);
    }

    @Override
    public double getMeanNumberOfIndividuals() {
        return delegate.getMeanNumberOfIndividuals();
    }

    @Override
    public int getTotalNumberOfIndividuals() {
        return delegate.getTotalNumberOfIndividuals();
    }

    @Override
    public List<String> getSpeciesNames() {
        return delegate.getSpeciesNames();
    }

    @Override
    public List<Integer> getDistribution() {
        return delegate.getDistribution();
    }

    @Override
    public double getFractionIn(int specieIndex) {
        return delegate.getFractionIn(specieIndex);
    }

    @Override
    public double getFractionIn(String specieName) {
        return delegate.getFractionIn(specieName);
    }

    @Override
    public boolean hasAnySpecieNamed(String specieName) {
        return delegate.hasAnySpecieNamed(specieName);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Population renameSpecie(int specieIndex, String newName) {
        delegate.renameSpecie(specieIndex, newName);
        return this;
    }

    @Override
    public final Population renameSpecie(String oldName, String newName) {
        return renameSpecie(getSpecieIndex(oldName), newName);
    }
    
    @Override
    public Population addSpecie(String specieName) {
        delegate.addSpecie(specieName);
        return this;
    }

    @Override
    public Population removeSpecie(int specieIndex) {
        delegate.removeSpecie(specieIndex);
        return this;
    }

    @Override
    public final Population removeSpecie(String specieName) {
       return removeSpecie(getSpecieIndex(specieName));
    }

    @Override
    public Population setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        delegate.setNumberOfIndividualsIn(specieIndex, numberOfIndividuals);
        return this;
    }

    @Override
    public final Population setNumberOfIndividualsIn(String specieName, int numberOfIndividuals) {
        return setNumberOfIndividualsIn(getSpecieIndex(specieName), numberOfIndividuals);
    }
        

    @Override
    public Population shiftNumberOfIndividualsIn(int specieIndex, int offset) {
        delegate.shiftNumberOfIndividualsIn(specieIndex, offset);
        return this;
    }

    @Override
    public final Population shiftNumberOfIndividualsIn(String specieName, int offset) {
        return shiftNumberOfIndividualsIn(getSpecieIndex(specieName), offset);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public double[] toArrayOfFractions() {
        return delegate.toArrayOfFractions();
    }

}
