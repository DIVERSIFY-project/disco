/*
 */
package eu.diversify.disco.population;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface IPopulation {

    public boolean equals(Object obj);

    public int getNumberOfIndividualsIn(int specieIndex);

    public int getNumberOfIndividualsIn(String specieName);

    public int getNumberOfSpecies();

    public int getSpecieIndex(String specieName);

    public int getTotalNumberOfIndividuals();

    public boolean hasAnySpecieNamed(String specieName);

    public int hashCode();

    public boolean isEmpty();

    public IPopulation addSpecie(String specieName);

    public IPopulation removeSpecie(int specieIndex);

    public IPopulation removeSpecie(String specieName);

    public IPopulation setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals);

    public IPopulation shiftNumberOfIndividualsIn(int specieIndex, int offset);

    public IPopulation shiftNumberOfIndividualsIn(String specieName, int offset);

    public String toString();
    
}
