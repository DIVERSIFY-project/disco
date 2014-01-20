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
package eu.diversify.disco.experiments.controllers.scalability;

import java.util.ArrayList;
import java.util.List;

/**
 * Store the parameters for the scalability experiment
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Setup {

    private final ArrayList<Integer> speciesCounts;
    private final ArrayList<Integer> individualsCounts;
    private final ArrayList<String> strategies;

    /**
     * Create a new setup for this experiment
     */
    public Setup() {
        this.strategies = new ArrayList<String>();
        this.speciesCounts = new ArrayList<Integer>();
        this.individualsCounts = new ArrayList<Integer>();
    }

    /**
     * Update the list of species counts to be used during the experiment
     *
     * @param counts the new list of species counts to be used during the
     * experiment
     */
    public void setSpeciesCounts(List<Integer> counts) {
        this.speciesCounts.clear();
        for (final int i : counts) {
            this.speciesCounts.add(i);
        }
    }

    /**
     * @return the list of species count to be used during the experiment
     */
    public List<Integer> getSpeciesCounts() {
        return this.speciesCounts;
    }

    /**
     * Set the list of individuals counts to be used in the experiment
     *
     * @param counts
     */
    public void setIndividualsCounts(List<Integer> counts) {
        this.individualsCounts.clear();
        for (final int c : counts) {
            this.individualsCounts.add(c);
        }
    }

    /**
     * @return the list of individuals counts used during the experiment
     */
    public List<Integer> getIndividualsCounts() {
        return this.individualsCounts;
    }

    /**
     * @return the list of control strategies to be used in the experiment
     */
    public List<String> getStrategies() {
        return this.strategies;
    }

    /**
     * Set the list of strategies to be used during the experiment
     *
     * @param strategies the list of strategies' name to be used during the
     * experiment
     */
    public void setStrategies(List<String> strategies) {
        this.strategies.clear();
        for (final String strategy : strategies) {
            this.strategies.add(strategy);
        }
    }
}
