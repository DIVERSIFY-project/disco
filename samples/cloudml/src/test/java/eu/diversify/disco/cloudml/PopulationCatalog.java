/*
 */

package eu.diversify.disco.cloudml;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;

/**
 * Hold a set of predefined population, used for testing purpose
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
class PopulationCatalog {
    
    private final PopulationBuilder builder;
    
    public PopulationCatalog() {
        this.builder = new PopulationBuilder();
    }

    public Population createEmptyPopulation() {
        return this.builder.make();
    }
    
}
