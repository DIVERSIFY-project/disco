
package eu.diversify.disco.experiments.population.sensitivity;

import eu.diversify.disco.experiments.commons.Runner;

/**
 * Launch the sensitivity analysis
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class Launcher {
   
    private static final String DEFAULT_CONFIG_FILE = "setup.yml";
    
    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.run(SensitivitySetup.class, DEFAULT_CONFIG_FILE);
    }
    
}
