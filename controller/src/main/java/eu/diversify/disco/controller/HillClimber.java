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
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.Specie;
import eu.diversify.disco.population.diversity.DiversityMetric;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class HillClimber extends Controller {
    
    /**
     * Create a new hill climber controller
     * @param metric 
     */
    public HillClimber(DiversityMetric metric) {
        super(metric);
    }

    protected List<Update> getLegalUpdates(Population population) {
        ArrayList<Update> updates = new ArrayList<Update>();
        for (Specie s1 : population.getSpecies()) {
            if (s1.getIndividualCount() > 0) {
                Update u = new Update();
                u.setUpdate(s1.getName(), -1);
                for (Specie s2 : population.getSpecies()) {
                    if (!s1.getName().equals(s2.getName())) {
                        u.setUpdate(s2.getName(), +1);
                    }
                }
                updates.add(u);
            }
        }
        return updates;
    }
}
