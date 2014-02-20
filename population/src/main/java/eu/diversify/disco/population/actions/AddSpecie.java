/*
 */

package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;

/**
 * Create a new specie in a given population
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class AddSpecie implements Action {

    private final String specieName;
    
    public AddSpecie(String specieName) {
        if (specieName == null) {
            throw new IllegalArgumentException("Specie name shall not be null");
        }
        this.specieName = specieName;
    }

    @Override
    public PopulationValue applyTo(PopulationValue subject) {
        return subject.addSpecie(specieName);
    }
    
}
