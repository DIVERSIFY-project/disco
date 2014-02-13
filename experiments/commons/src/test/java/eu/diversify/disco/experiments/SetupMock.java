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
