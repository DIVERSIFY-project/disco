
package eu.diversify.disco.experiments;

import eu.diversify.disco.experiments.commons.Setup;
import eu.diversify.disco.experiments.commons.Experiment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dummy setup class
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class SetupMock implements Setup {
    
    private final ArrayList<Integer> population;
    
    public SetupMock() {
        this.population = new ArrayList<Integer>();
    }
    
    public List<Integer> getPopulation() {
        return Collections.unmodifiableList(this.population);
    }
    
    
    public void setPopulation(List<Integer> population) {
        this.population.clear();
        this.population.addAll(population);
    }
   
    @Override
    public Experiment buildExperiment() {
        return new ExperimentMock(this);
    }
    
}
