/*
 */
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;

/**
 * Generalise the behaviour of actions which access a specie
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class SpecieAccess implements Action {

    public static final String UNDEFINED_SPECIE_NAME = "###!!!UNDEFINED!!!###";
    public static final int UNDEFINED_INDEX = -1;
    
    private final int specieIndex;
    private final String specieName;

    public SpecieAccess(int specieIndex) {
        this.specieIndex = specieIndex;
        this.specieName = UNDEFINED_SPECIE_NAME;
    }
    
    public SpecieAccess(String specieName) {
        this.specieIndex = UNDEFINED_INDEX;
        this.specieName = specieName;
    }

    protected int getSpecieIndex(PopulationValue population) {
        int index = specieIndex;
        if (index == UNDEFINED_INDEX) {
            index = population.getSpecieIndex(specieName);
        }
        return index;
    }
}
