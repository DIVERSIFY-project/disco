package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;

/**
 * Shift (i.e., add) the number of individual in a given specie, identified by
 * either its name or its index.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ShiftNumberOfIndividualsIn extends SpecieAccess {

    private final int offset;

    public ShiftNumberOfIndividualsIn(int specieIndex, int offset) {
        super(specieIndex);
        this.offset = offset;
    }

    public ShiftNumberOfIndividualsIn(String specieName, int offset) {
        super(specieName);
        this.offset = offset;
    }

    @Override
    public PopulationValue applyTo(PopulationValue subject) {
        return subject.shiftNumberOfIndividualsIn(getSpecieIndex(subject), offset);
    }
}
