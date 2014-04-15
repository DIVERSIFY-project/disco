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
import eu.diversify.disco.population.actions.Action;
import java.util.List;
import java.util.Map;

/**
 * Factor the common code out of decorators object, and provide default implementations for some 
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
    public boolean allows(Action action) {
        return delegate.allows(action);
    }  

    @Override
    public List<Action> allLegalActions(int scaleFactor) {
        return delegate.allLegalActions(scaleFactor);
    }

    
    @Override
    public Population deepCopy() {
        return delegate.deepCopy();
    }

    @Override
    public int getHeadcountIn(int specieIndex) {
        return delegate.getHeadcountIn(specieIndex);
    }

    @Override
    public int getHeadcountIn(String specieName) {
        return delegate.getHeadcountIn(specieName);
    }

    @Override
    public int getSpeciesCount() {
        return delegate.getSpeciesCount();
    }

    @Override
    public int getSpecieIndex(String specieName) {
        return delegate.getSpecieIndex(specieName);
    }

    @Override
    public double getMeanHeadcount() {
        return delegate.getMeanHeadcount();
    }

    @Override
    public double getVariance() {
        return delegate.getVariance();
    }
    
    @Override
    public int getTotalHeadcount() {
        return delegate.getTotalHeadcount();
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
    public Population addSpecie() {
        delegate.addSpecie();
        return this;
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
    public Population setHeadcountIn(int specieIndex, int numberOfIndividuals) {
        delegate.setHeadcountIn(specieIndex, numberOfIndividuals);
        return this;
    }

    @Override
    public final Population setHeadcountIn(String specieName, int numberOfIndividuals) {
        return setHeadcountIn(getSpecieIndex(specieName), numberOfIndividuals);
    }
        

    @Override
    public Population shiftHeadcountIn(int specieIndex, int offset) {
        delegate.shiftHeadcountIn(specieIndex, offset);
        return this;
    }

    @Override
    public final Population shiftHeadcountIn(String specieName, int offset) {
        return shiftHeadcountIn(getSpecieIndex(specieName), offset);
    }

    @Override
    public List<Action> differenceWith(Population target) {
        return delegate.differenceWith(target);
    }

    @Override
    public List<String> sortSpeciesNamesAlphabetically() {
        return delegate.sortSpeciesNamesAlphabetically();
    }

    @Override
    public boolean isUniformlyDistributed() {
        return delegate.isUniformlyDistributed();
    }
       

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public double[] toArrayOfFractions() {
        return delegate.toArrayOfFractions();
    }

    @Override
    public Map<String, Integer> toMap() {
        return delegate.toMap();
    }
     
    

}
