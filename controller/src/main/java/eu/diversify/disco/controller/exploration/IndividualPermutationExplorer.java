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
package eu.diversify.disco.controller.exploration;

import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.Script;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find all the permutations of individuals that one can perform on a given
 * population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class IndividualPermutationExplorer implements ExplorationStrategy {

    @Override
    public List<Action> search(Solution solution, int scaleFactor) {
        final ArrayList<Action> actions = new ArrayList<Action>();
        final Population population = solution.getPopulation();
        for (int specie1 = 1; specie1 <= population.getNumberOfSpecies(); specie1++) {
            if (population.getNumberOfIndividualsIn(specie1) >= scaleFactor) {
                for (int specie2 = 1; specie2 <= population.getNumberOfSpecies(); specie2++) {
                    if (specie1 != specie2) {
                        final Action action = new Script(Arrays.asList(new Action[]{
                            new ShiftNumberOfIndividualsIn(specie1, -scaleFactor),
                            new ShiftNumberOfIndividualsIn(specie2, +scaleFactor)
                        }));
                        if (solution.canBeRefinedWith(action)) {
                            actions.add(action);
                        }
                    }
                }
            }
        }
        return actions;
    }
}
