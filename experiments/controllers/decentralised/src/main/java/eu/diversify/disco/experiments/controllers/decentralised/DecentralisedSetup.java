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
/*
 */

package eu.diversify.disco.experiments.controllers.decentralised;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.Setup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DecentralisedSetup implements Setup {

    private final ArrayList<String> strategies;

    public DecentralisedSetup() {
        strategies = new ArrayList<String>();
    }
      
    
    @Override
    public Experiment buildExperiment() {
        return new DecentralisedExperiment(this);
    }
    
    
    public List<String> getStrategies() {
        return Collections.unmodifiableList(strategies);
    }
    
    public void setStrategies(List<String> strategies) {
        this.strategies.clear();
        this.strategies.addAll(strategies);
    }
    
}
