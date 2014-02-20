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
public class PopulationValue {

    public static final String DEFAULT_SPECIE_NAME_PREFIX = "sp. #";

    public static PopulationValue fromDistributionOfIndividuals(String specieNamePrefix, List<Integer> distribution) {
        return new PopulationValue(specieNamePrefix, distribution);
    }

    public static PopulationValue fromDistributionOfIndividuals(String specieNamePrefix, Integer[] distribution) {
        return new PopulationValue(specieNamePrefix, Arrays.asList(distribution));
    }

    public static PopulationValue fromDistributionOfIndividuals(Integer[] distribution) {
        return new PopulationValue(DEFAULT_SPECIE_NAME_PREFIX, Arrays.asList(distribution));
    }

    public static PopulationValue fromDistributionOfIndividuals(List<String> speciesName, List<Integer> distribution) {
        return new PopulationValue(speciesName, distribution);
    }

    public static PopulationValue fromDistributionOfIndividuals(String[] speciesName, Integer[] distribution) {
        return new PopulationValue(Arrays.asList(speciesName), Arrays.asList(distribution));
    }
    private final ArrayList<String> speciesName;
    private final ArrayList<Integer> individuals;

    public PopulationValue() {
        this.speciesName = new ArrayList<String>();
        this.individuals = new ArrayList<Integer>();
    }

    private PopulationValue(String specieNamePrefix, List<Integer> distribution) {
        this.speciesName = new ArrayList<String>();
        this.individuals = new ArrayList<Integer>();
        for (int i = 0; i < distribution.size(); i++) {
            final String name = String.format("%s%d", specieNamePrefix, i + 1);
            this.speciesName.add(name);
            this.individuals.add(distribution.get(i));
        }
    }

    private PopulationValue(List<String> speciesName, List<Integer> distribution) {
        if (speciesName.size() < distribution.size()) {
            throw new IllegalArgumentException("Missing species name");
        }
        if (speciesName.size() > distribution.size()) {
            throw new IllegalArgumentException("Missing individual counts");
        }
        this.speciesName = new ArrayList<String>(speciesName);
        this.individuals = new ArrayList<Integer>(distribution);
    }

    private PopulationValue(PopulationValue model) {
        this.speciesName = new ArrayList<String>(model.speciesName);
        this.individuals = new ArrayList<Integer>(model.individuals);
    }

    public boolean isEmpty() {
        return getTotalNumberOfIndividuals() == 0;
    }

    public int getTotalNumberOfIndividuals() {
        int total = 0;
        for (int count : this.individuals) {
            total += count;
        }
        return total;
    }

    public int getNumberOfSpecies() {
        return this.speciesName.size();
    }

    public PopulationValue addIndividualIn(int specieIndex) {
        int count = getNumberOfIndividualsIn(specieIndex);
        return setNumberOfIndividualsIn(specieIndex, count + 1);
    }

    public PopulationValue addIndividualIn(String specieName) {
        return addIndividualIn(getSpecieIndex(specieName));
    }

    public int getNumberOfIndividualsIn(int specieIndex) {
        checkSpecieIndex(specieIndex);
        return individuals.get(specieIndex - 1);
    }

    public int getNumberOfIndividualsIn(String specieName) {
        return individuals.get(getSpecieIndex(specieName));
    }

    public PopulationValue setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        checkSpecieIndex(specieIndex);
        PopulationValue result = new PopulationValue(this);
        if (numberOfIndividuals < 0) {
            throw new IllegalArgumentException("Specie " + specieIndex + " cannot have a negative number of individuals");
        }
        result.individuals.set(specieIndex - 1, numberOfIndividuals);
        return result;
    }

    public PopulationValue removeIndividualFrom(int specieIndex) {
        int count = getNumberOfIndividualsIn(specieIndex);
        return setNumberOfIndividualsIn(specieIndex, count - 1);
    }

    public PopulationValue removeIndividualFrom(String specieName) {
        return removeIndividualFrom(getSpecieIndex(specieName));
    }

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

    public PopulationValue addSpecie(String specieName) {
        if (hasAnySpecieNamed(specieName)) {
            throw new IllegalArgumentException("Duplicated specie name '" + specieName + "'");
        }
        PopulationValue result = new PopulationValue(this);
        result.speciesName.add(specieName);
        result.individuals.add(0);
        return result;
    }

    public boolean hasAnySpecieNamed(String specieName) {
        return speciesName.contains(specieName);
    }

    public PopulationValue removeSpecie(int specieIndex) {
        checkSpecieIndex(specieIndex);
        PopulationValue result = new PopulationValue(this);
        result.speciesName.remove(specieIndex - 1);
        result.individuals.remove(specieIndex - 1);
        return result;
    }

    public PopulationValue removeSpecie(String specieName) {
        return removeSpecie(getSpecieIndex(specieName));
    }

    private void checkSpecieIndex(int specieIndex) {
        if (specieIndex > speciesName.size()) {
            throw new IllegalArgumentException("No specie with index '" + specieIndex + "'");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < this.individuals.size(); i++) {
            builder.append(speciesName.get(i));
            builder.append(": ");
            builder.append(individuals.get(i));
            if (i < individuals.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
