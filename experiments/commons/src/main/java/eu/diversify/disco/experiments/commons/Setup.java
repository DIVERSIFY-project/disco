
package eu.diversify.disco.experiments.commons;

/**
 * General behaviour of the setup of experiments
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Setup {
    
    /**
     * @return a new instance of the experiment associated with this setup
     */
    public Experiment buildExperiment();
    
}
