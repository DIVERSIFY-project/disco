/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Disco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco.  If not, see <http://www.gnu.org/licenses/>.
 */

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
