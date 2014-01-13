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
 * Adaptive Hill-Climbing with a twiddling mechanism.
 *
 * When the the search is successful, the adaptive hill climber will try a twice
 * bigger step in the next step. When the search is not successful, the step is
 * reduced, until it returns to one.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class AdaptiveHillClimber extends Controller {

    private int stepSize;

    /**
     * Create a new instance of the Adaptive Hill Climbing strategy, tailored to
     * use a particular diversity metric.
     *
     * @param metric the diversity metric to use
     */
    public AdaptiveHillClimber(DiversityMetric metric) {
        super(metric);
        this.stepSize = 1;
    }

    
    @Override
    protected Evaluation search(Evaluation current) {
        Evaluation output = refine(current);
        if (output.getError() >= current.getError()) {
            if (this.stepSize > 1) {
                this.stepSize /= 2;
                output = search(current);
            }
        } else {
            this.stepSize *= 2;
            output = search(output);
        }
        return output;
    }

    
    @Override
    protected List<Update> getLegalUpdates(Population population) {
        final ArrayList<Update> updates = new ArrayList<Update>();
        for (Specie s1 : population.getSpecies()) {
            if (s1.getIndividualCount() >= stepSize) {
                for (Specie s2 : population.getSpecies()) {
                    if (!s1.getName().equals(s2.getName())) {
                        final Update u = new Update();
                        u.setUpdate(s1.getName(), -stepSize);
                        u.setUpdate(s2.getName(), +stepSize);
                        updates.add(u);
                    }
                }
            }
        }
        return updates;
    }
}
