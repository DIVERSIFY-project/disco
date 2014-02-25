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

import eu.diversify.disco.controller.exploration.IndividualPermutationExplorer;
import eu.diversify.disco.controller.problem.Solution;

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
public class AdaptiveHillClimber extends IterativeSearch {

    private int stepSize;

    /**
     * Create a new instance of the Adaptive Hill Climbing strategy, tailored to
     * use a particular diversity metric.
     *
     * @param metric the diversity metric to use
     */
    public AdaptiveHillClimber() {
        super(new IndividualPermutationExplorer());
        this.stepSize = 1;
    }

    
    @Override
    protected Solution search(Solution current) {
        Solution output = refine(current);
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
    protected int getScaleFactor() {
        return stepSize;
    }
    
    
    

   
}
