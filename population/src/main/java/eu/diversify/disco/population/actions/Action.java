
package eu.diversify.disco.population.actions;

import eu.diversify.disco.population.PopulationValue;

/**
 * General behaviour of actions run against a population
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Action {
    
    public PopulationValue applyTo(PopulationValue subject);
    
}
