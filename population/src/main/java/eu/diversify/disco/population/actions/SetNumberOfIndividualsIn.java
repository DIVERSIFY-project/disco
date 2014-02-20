/*
 */
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;

/**
 * Set the number of individual in a given specie, identified either by its
 * index or by its name.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class SetNumberOfIndividualsIn extends SpecieAccess {

    private final int numberOfIndividuals;

    public SetNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals) {
        super(specieIndex);
        this.numberOfIndividuals = numberOfIndividuals;
    }

    public SetNumberOfIndividualsIn(String specieName, int numberOfIndividuals) {
        super(specieName);
        this.numberOfIndividuals = numberOfIndividuals;
    }

    @Override
    public PopulationValue applyTo(PopulationValue subject) {
        return subject.setNumberOfIndividualsIn(getSpecieIndex(subject), numberOfIndividuals);
    }
    
}
