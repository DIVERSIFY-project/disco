package eu.diversify.disco.population;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A value object representing a population. Intended to be immutable
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class PopulationValue implements IPopulation {

    public static final String DEFAULT_SPECIE_NAME_PREFIX = "sp. #";

    public static PopulationValue emptyPopulation() {
        return new PopulationValue();
    }
    
    public static PopulationValue fromDistributionOfIndividuals(String specieNamePrefix, List<Integer> distribution) {
        ArrayList<String> names = new ArrayList<String>(distribution.size());
        for(int i=0 ; i<distribution.size() ; i++) {
            String name = String.format("%s%d", specieNamePrefix, i+1);
            names.add(name);
        }
        return new PopulationValue(names, distribution);
    }

    public static PopulationValue fromDistributionOfIndividuals(String specieNamePrefix, Integer[] distribution) {
        return fromDistributionOfIndividuals(specieNamePrefix, Arrays.asList(distribution));
    }

    public static PopulationValue fromDistributionOfIndividuals(Integer[] distribution) {
        return fromDistributionOfIndividuals(DEFAULT_SPECIE_NAME_PREFIX, Arrays.asList(distribution));
    }

    public static PopulationValue fromDistributionOfIndividuals(List<String> speciesName, List<Integer> distribution) {
        return new PopulationValue(speciesName, distribution);
    }

    public static PopulationValue fromDistributionOfIndividuals(String[] speciesName, Integer[] distribution) {
        return new PopulationValue(Arrays.asList(speciesName), Arrays.asList(distribution));
    }
    private final ArrayList<String> speciesName;
    private final ArrayList<Integer> individuals;

    private PopulationValue() {
        this.speciesName = new ArrayList<String>();
        this.individuals = new ArrayList<Integer>();
    }


    private PopulationValue(List<String> speciesName, List<Integer> distribution) {
        if (speciesName.size() < distribution.size()) {
            throw new IllegalArgumentException("Missing species name");
        }
        if (speciesName.size() > distribution.size()) {
            throw new IllegalArgumentException("Missing individual counts");
        }
        this.speciesName = new ArrayList<String>();
        for(String specieName: speciesName) {
            checkIfSpecieNameIsValid(specieName);
            this.speciesName.add(specieName);
        }
        this.individuals = new ArrayList<Integer>();
        for(Integer count: distribution) {
            checkIfIndividualCountIsValid(count);
            this.individuals.add(count);
        }
    }

    private PopulationValue(PopulationValue model) {
        this.speciesName = new ArrayList<String>(model.speciesName);
        this.individuals = new ArrayList<Integer>(model.individuals);
    }

    @Override
    public boolean isEmpty() {
        return getTotalNumberOfIndividuals() == 0;
    }

    @Override
    public int getTotalNumberOfIndividuals() {
        int total = 0;
        for (int count : this.individuals) {
            total += count;
        }
        return total;
    }

    @Override
    public int getNumberOfSpecies() {
        return this.speciesName.size();
    }

    @Override
    public PopulationValue shiftNumberOfIndividualsIn(int specieIndex, int offset) {
        int count = getNumberOfIndividualsIn(specieIndex);
        return setNumberOfIndividualsIn(specieIndex, count + offset);
    }

    @Override
    public PopulationValue shiftNumberOfIndividualsIn(String specieName, int offset) {
        return shiftNumberOfIndividualsIn(getSpecieIndex(specieName), offset);
    }

    @Override
    public int getNumberOfIndividualsIn(int specieIndex) {
        checkSpecieIndexIsValid(specieIndex);
        return individuals.get(specieIndex - 1);
    }

    @Override
    public int getNumberOfIndividualsIn(String specieName) {
        return getNumberOfIndividualsIn(getSpecieIndex(specieName));
    }

    @Override
    public PopulationValue setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        checkSpecieIndexIsValid(specieIndex);
        PopulationValue result = new PopulationValue(this);
        if (numberOfIndividuals < 0) {
            throw new IllegalArgumentException("Specie " + specieIndex + " cannot have a negative number of individuals");
        }
        result.individuals.set(specieIndex - 1, numberOfIndividuals);
        return result;
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
        hash = 67 * hash + (this.individuals != null ? this.individuals.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PopulationValue other = (PopulationValue) obj;
        if (this.speciesName != other.speciesName && (this.speciesName == null || !this.speciesName.equals(other.speciesName))) {
            return false;
        }
        if (this.individuals != other.individuals && (this.individuals == null || !this.individuals.equals(other.individuals))) {
            return false;
        }
        return true;
    }

    @Override
    public PopulationValue addSpecie(String specieName) {
        checkIfSpecieNameIsValid(specieName);

        PopulationValue result = new PopulationValue(this);
        result.speciesName.add(specieName);
        result.individuals.add(0);
        return result;
    }

    @Override
    public boolean hasAnySpecieNamed(String specieName) {
        return speciesName.contains(specieName);
    }

    @Override
    public PopulationValue removeSpecie(int specieIndex) {
        checkSpecieIndexIsValid(specieIndex);
        PopulationValue result = new PopulationValue(this);
        result.speciesName.remove(specieIndex - 1);
        result.individuals.remove(specieIndex - 1);
        return result;
    }

    @Override
    public PopulationValue removeSpecie(String specieName) {
        return removeSpecie(getSpecieIndex(specieName));
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
        for (int i = 0; i < individuals.size(); i++) {
            builder.append(speciesName.get(i));
            builder.append(": ");
            builder.append(individuals.get(i));
            if (i < individuals.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(" ]");
        return builder.toString();
    }
}
