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

package eu.diversify.disco.experiments.population.eda;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.Setup;
import eu.diversify.disco.population.random.Profile;

/**
 * The setup for the random population generation
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class EdaSetup implements Setup {

    private Profile population;
    private int sampleCount;
    
    public EdaSetup() {
        this.sampleCount = 1000;
    }
    
    @Override
    public Experiment buildExperiment() {
        return new Eda(this);
    }
    
    public int getSampleCount() {
        return this.sampleCount;
    }
 
    public void setSampleCount(int count) {
        this.sampleCount = count;
    }
    
    public Profile getPopulation() {
        return this.population;
    }

    public void setPopulation(Profile population) {
        this.population = population;
    }

}
