
package eu.diversify.disco.population;

import java.util.List;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Individual {

    public void join(DynamicPopulation p);

    public List<String> getSpecies();

    public void addSpecie(String specie);

    public void leave(DynamicPopulation population);

    public void setSpecies(List<String> asList);
    
}
