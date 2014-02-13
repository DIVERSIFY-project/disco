
package eu.diversify.disco.experiments.population.sensitivity;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.Setup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class SensitivitySetup implements Setup {
    
    private static final int DEFAULT_SIZE = 100;
    private int size;
    private final ArrayList<String> metrics;
    
    /**
     * Build a new SensitivitySetup with a default size of 
     */
    public SensitivitySetup() {
        this.size = DEFAULT_SIZE;
        this.metrics = new ArrayList<String>();
    }
    
    /**
     * @return the size of the population to use
     */
    public int getSize() {
        return this.size;
    }
    
    
    /**
     * Set the size of the population to use
     * @param size the size as a number of individuals
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    
    /**
     * @return the list of metrics (their name) selected for analysis 
     */
    public List<String> getMetrics() {
        return Collections.unmodifiableList(this.metrics);
    }
    
    
    /**
     * Set the list of metrics to be used for analysis
     * @param metrics the list of metrics names
     */
    public void setMetrics(List<String> metrics) {
        this.metrics.clear();
        this.metrics.addAll(metrics);
    }
    
    @Override
    public Experiment buildExperiment() {
        return new SensitivityExperiment(this);
    }
    
    
}
