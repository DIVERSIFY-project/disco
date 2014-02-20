
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;

/**
 * Remove a given specie, identified either by its index or by its name
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class RemoveSpecie extends SpecieAccess {

    public RemoveSpecie(int specieIndex) {
        super(specieIndex);
    }

    public RemoveSpecie(String specieName) {
        super(specieName);
    }

    @Override
    public PopulationValue applyTo(PopulationValue subject) {
        return subject.removeSpecie(getSpecieIndex(subject));
    }
    
}
