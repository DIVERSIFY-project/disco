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
package eu.diversify.disco.population;

import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.AddSpecie;
import eu.diversify.disco.population.actions.RemoveSpecie;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A value object representing a population. Intended to be immutable
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ConcretePopulation implements Population {

    public static final String DEFAULT_SPECIE_NAME_PREFIX = "sp. #";
    // FIXME: Change the implementation of the list of species name to ensure access to species name in O(c) 
    private final ArrayList<String> speciesName;
    private final ArrayList<Integer> distribution;

    ConcretePopulation() {
        this.speciesName = new ArrayList<String>();
        this.distribution = new ArrayList<Integer>();
    }

    ConcretePopulation(List<String> speciesNames, List<Integer> distribution) {
        checkConsistencyBetween(speciesNames, distribution);
        this.speciesName = new ArrayList<String>();
        for (String specieName : speciesNames) {
            checkIfSpecieNameIsValid(specieName);
            this.speciesName.add(specieName);
        }
        this.distribution = new ArrayList<Integer>();
        for (Integer count : distribution) {
            checkIfIndividualCountIsValid(count);
            this.distribution.add(count);
        }
    }

    private ConcretePopulation(ConcretePopulation model) {
        this.speciesName = new ArrayList<String>(model.speciesName);
        this.distribution = new ArrayList<Integer>(model.distribution);
    }

    @Override
    public boolean isEmpty() {
        return getTotalNumberOfIndividuals() == 0;
    }

    @Override
    public int getTotalNumberOfIndividuals() {
        int total = 0;
        for (int count : this.distribution) {
            total += count;
        }
        return total;
    }

    @Override
    public int getNumberOfSpecies() {
        return this.speciesName.size();
    }

    @Override
    public Population shiftNumberOfIndividualsIn(int specieIndex, int offset) {
        int count = getNumberOfIndividualsIn(specieIndex);
        return setNumberOfIndividualsIn(specieIndex, count + offset);
    }

    @Override
    public Population shiftNumberOfIndividualsIn(String specieName, int offset) {
        return shiftNumberOfIndividualsIn(getSpecieIndex(specieName), offset);
    }

    @Override
    public int getNumberOfIndividualsIn(int specieIndex) {
        checkSpecieIndexIsValid(specieIndex);
        return distribution.get(specieIndex - 1);
    }

    @Override
    public int getNumberOfIndividualsIn(String specieName) {
        return getNumberOfIndividualsIn(getSpecieIndex(specieName));
    }

    @Override
    public Population renameSpecie(String oldName, String newName) {
        return renameSpecie(getSpecieIndex(oldName), newName);
    }

    @Override
    public Population setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        checkSpecieIndexIsValid(specieIndex);
        if (numberOfIndividuals < 0) {
            throw new IllegalArgumentException("Specie " + specieIndex + " cannot have a negative number of individuals");
        }
        distribution.set(specieIndex - 1, numberOfIndividuals);
        return this;
    }

    @Override
    public Population setNumberOfIndividualsIn(String specieName, int numberOfIndividuals) {
        setNumberOfIndividualsIn(getSpecieIndex(specieName), numberOfIndividuals);
        return this;
    }

    @Override
    public int getSpecieIndex(String specieName) {
        int index = speciesName.indexOf(specieName);
        if (index == -1) {
            throw new IllegalArgumentException("No specie with name '" + specieName + "'");
        }
        return index + 1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.speciesName != null ? this.speciesName.hashCode() : 0);
        hash = 67 * hash + (this.distribution != null ? this.distribution.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object != null) {
            if (object instanceof Population) {
                final Population other = (Population) object;
                result = this.toMap().equals(other.toMap());
            }
        }
        return result;
    }

    @Override
    public Population addSpecie(String specieName) {
        checkIfSpecieNameIsValid(specieName);
        speciesName.add(specieName);
        distribution.add(0);
        return this;
    }

    @Override
    public boolean hasAnySpecieNamed(String specieName) {
        return speciesName.contains(specieName);
    }

    @Override
    public Population removeSpecie(int specieIndex) {
        checkSpecieIndexIsValid(specieIndex);
        speciesName.remove(specieIndex - 1);
        distribution.remove(specieIndex - 1);
        return this;
    }

    @Override
    public Population removeSpecie(String specieName) {
        return removeSpecie(getSpecieIndex(specieName));
    }

    private void checkConsistencyBetween(List<String> speciesName, List<Integer> distribution) {
        if (speciesName.size() < distribution.size()) {
            throw new IllegalArgumentException("Missing species name");
        }
        if (speciesName.size() > distribution.size()) {
            throw new IllegalArgumentException("Missing individual counts");
        }
    }

    private void checkIfIndividualCountIsValid(Integer count) {
        if (count == null) {
            throw new IllegalArgumentException("Individual count shall not be null.");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Individual count shall not be negative");
        }
    }

    private void checkIfSpecieNameIsValid(String specieName) {
        if (specieName == null) {
            throw new IllegalArgumentException("Specie name shall not be null.");
        }
        if (specieName.equals("")) {
            throw new IllegalArgumentException("The empty string '' is not a valid specie name.");
        }
        if (hasAnySpecieNamed(specieName)) {
            throw new IllegalArgumentException("Duplicated specie name '" + specieName + "'");
        }
    }

    private void checkSpecieIndexIsValid(int specieIndex) {
        if (specieIndex < 1 || specieIndex > speciesName.size()) {
            throw new IllegalArgumentException("No specie with index '" + specieIndex + "'");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for (int i = 0; i < distribution.size(); i++) {
            builder.append(speciesName.get(i));
            builder.append(": ");
            builder.append(distribution.get(i));
            if (i < distribution.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(" ]");
        return builder.toString();
    }

    @Override
    public double getFractionIn(int specieIndex) {
        double count = getNumberOfIndividualsIn(specieIndex);
        return (count / getTotalNumberOfIndividuals());
    }

    @Override
    public double getFractionIn(String specieName) {
        return getFractionIn(getSpecieIndex(specieName));
    }

    @Override
    public double[] toArrayOfFractions() {
        double[] results = new double[getNumberOfSpecies()];
        for (int i = 0; i < results.length; i++) {
            results[i] = getFractionIn(i + 1);
        }
        return results;
    }

    @Override
    public double getMeanNumberOfIndividuals() {
        return ((double) getTotalNumberOfIndividuals()) / getNumberOfSpecies();
    }

    @Override
    public Population renameSpecie(int specieIndex, String newName) {
        checkSpecieIndexIsValid(specieIndex);
        speciesName.set(specieIndex - 1, newName);
        return this;
    }

    @Override
    public Population deepCopy() {
        return new ConcretePopulation(this);
    }

    @Override
    public List<String> getSpeciesNames() {
        return Collections.unmodifiableList(this.speciesName);
    }

    @Override
    public List<Integer> getDistribution() {
        return Collections.unmodifiableList(this.distribution);
    }

    @Override
    public double getVariance() {
        int s = getNumberOfSpecies();
        double mu = getMeanNumberOfIndividuals();
        double total = 0D;
        for (int index = 1; index <= s; index++) {
            total += Math.pow(getNumberOfIndividualsIn(index) - mu, 2);
        }
        return total / s;
    }

    
    @Override
    public boolean isUniformlyDistributed() {
        return getVariance() == 0;
    }
       

    @Override
    public List<Action> differenceWith(Population target) {
        final ArrayList<Action> actions = new ArrayList<Action>();
        final List<String> superfluousSpecies = new ArrayList<String>(getSpeciesNames());
        for (String foreign : target.getSpeciesNames()) {
            if (this.hasAnySpecieNamed(foreign)) {
                int delta = target.getNumberOfIndividualsIn(foreign) - this.getNumberOfIndividualsIn(foreign);
                if (delta != 0) {
                    actions.add(new ShiftNumberOfIndividualsIn(foreign, delta));
                }
            }
            else {
                actions.add(new AddSpecie(foreign));
                actions.add(new ShiftNumberOfIndividualsIn(foreign, target.getNumberOfIndividualsIn(foreign)));
            }
            superfluousSpecies.remove(foreign);
        }
        for (String specie: superfluousSpecies) {
            actions.add(new RemoveSpecie(specie));
        }
        return actions;
    }

    @Override
    public List<String> sortSpeciesNamesAlphabetically() {
        final List<String> speciesNames = new ArrayList<String>(getSpeciesNames());
        Collections.sort(speciesNames);
        return speciesNames;
    }

    @Override
    public Map<String, Integer> toMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int index = 0; index < this.distribution.size(); index++) {
            map.put(this.speciesName.get(index), this.distribution.get(index));
        }
        return map;
    }
}
