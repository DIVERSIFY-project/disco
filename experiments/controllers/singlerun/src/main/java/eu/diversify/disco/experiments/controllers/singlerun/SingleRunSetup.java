
package eu.diversify.disco.experiments.controllers.singlerun;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.Setup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Capture the various option on data used to setup this experiment
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class SingleRunSetup implements Setup {

    private final ArrayList<Integer> population;
    private final HashSet<String> strategies;
    private double reference;
    
    /**
     * Create an empty setup, with the following default values.
     * 
     *  - Initial population p = [12, 6, 5]
     *  - Reference: 0.1
     *  - Selected Strategies: Hill Climbing and Adaptive Hill Climbing
     * 
     */
    public SingleRunSetup() {
        this.population = new ArrayList<Integer>();
        this.population.add(12);
        this.population.add(6);
        this.population.add(5);
        
        this.strategies = new HashSet<String>();
        this.strategies.add("Hill Climbing");
        this.strategies.add("Adaptive Hill Climbing");
        
        this.reference = .1;
    }
    
    /**
     * @return a list containing the names of the selected strategies
     */
    public List<String> getStrategies() {
        return Collections.unmodifiableList(new ArrayList<String>(this.strategies));
    }
    
    /**
     * Update the list of selected strategies
     * @param strategies the list of names of the selected strategies
     */
    public void setStrategies(List<String> strategies) {
        this.strategies.clear();
        this.strategies.addAll(strategies);
    }
    
    /**
     * @return the list of species count selected for the initial population
     */
    public List<Integer> getPopulation() {
        return Collections.unmodifiableList(this.population);
    }
    
    /**
     * Update the initial population
     * @param population the new initial population, as a list of specie count
     */
    public void setPopulation(List<Integer> population) {
        this.population.clear();
        this.population.addAll(population);
    }
    
    /**
     * @return the reference diversity
     */
    public double getReference() {
        return this.reference;
    }
    
    /**
     * Update the reference diversity level of this setup
     * @param reference the new reference diversity level to use
     */
    public void setReference(double reference) {
        this.reference = reference;
    }

    @Override
    public Experiment buildExperiment() {
        return new SingleRunExperiment(this);
    }
    
}
