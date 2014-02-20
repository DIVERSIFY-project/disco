package eu.diversify.disco.population;

/**
 * A decorator which permits that a population is updated by the individuals
 * which compose it.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class DynamicPopulation implements IPopulation {

    private PopulationValue delegate;

    public DynamicPopulation() {
        delegate = PopulationValue.emptyPopulation();
    }

    @Override
    public int getTotalNumberOfIndividuals() {
        return delegate.getTotalNumberOfIndividuals();
    }

    @Override
    public int getNumberOfSpecies() {
        return delegate.getNumberOfSpecies();
    }

    @Override
    public int getNumberOfIndividualsIn(String specie) {
        return delegate.getNumberOfIndividualsIn(specie);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public PopulationValue shiftNumberOfIndividualsIn(int specieIndex, int offset) {
        delegate = delegate.shiftNumberOfIndividualsIn(specieIndex, offset);
        return delegate;
    }

    @Override
    public PopulationValue shiftNumberOfIndividualsIn(String specieName, int offset) {
        delegate = delegate.shiftNumberOfIndividualsIn(specieName, offset);
        return delegate;
    }

    @Override
    public int getNumberOfIndividualsIn(int specieIndex) {
        return delegate.getNumberOfIndividualsIn(specieIndex);
    }

    @Override
    public PopulationValue setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
       delegate = delegate.setNumberOfIndividualsIn(specieIndex, numberOfIndividuals);
       return delegate;
    }

    @Override
    public int getSpecieIndex(String specieName) {
        return delegate.getSpecieIndex(specieName);
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public PopulationValue addSpecie(String specieName) {
        delegate = delegate.addSpecie(specieName);
        return delegate;
    }

    @Override
    public boolean hasAnySpecieNamed(String specieName) {
        return delegate.hasAnySpecieNamed(specieName);
    }

    @Override
    public PopulationValue removeSpecie(int specieIndex) {
        delegate = delegate.removeSpecie(specieIndex);
        return delegate;
    }

    @Override
    public PopulationValue removeSpecie(String specieName) {
        delegate = delegate.removeSpecie(specieName);
        return delegate;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
