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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.Specie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Update which can be undergone on a population. An update is a modification of
 * one or several species' individual count.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Update {

    public final LinkedHashMap<String, Integer> updates;

    /**
     * Create an empty update, which is the update that does nothing aside of
     * duplicating the given population.
     */
    public Update() {
        this.updates = new LinkedHashMap<String, Integer>();
    }

    /**
     * Set an update to be performed on the specific specie of a population
     *
     * @param specie the specie whose individual count must be updated
     * @param update the change to apply to the selected specie
     */
    public void setUpdate(String specie, int update) {
        this.updates.put(specie, update);
    }

    /**
     * Apply this update. The result is th fresh population and the given
     * population has not been changed.
     *
     * @param population the population to update
     *
     * @return a fresh population resulting from applying this update on the
     * given population.
     */
    public Population applyTo(Population population) {
        final Population result = new Population();
        for (Specie specie : population.getSpecies()) {
            final String name = specie.getName();
            int count = specie.getIndividualCount();
            if (this.updates.containsKey(name)) {
                count += updates.get(name);
            }
            result.addSpecie(name, count);
        }
        return result;
    }

    /**
     * Return the update associated with the given specie
     *
     * @param specie the specie whose update is needed
     * @return the update associated with the selected specie
     */
    public int getUpdate(String specie) {
        int result = 0;
        if (this.updates.containsKey(specie)) {
            result = this.updates.get(specie);
        }
        return result;
    }

    /**
     * @return the list of impacted species (their names)
     */
    public List<String> getImpactedSpecies() {
        return Collections.unmodifiableList(new ArrayList<String>(this.updates.keySet()));
    }

    /**
     * Check whether this update contains the given modification
     *
     * @return true if this update effectively perform the given update on the
     * given specie
     */
    public boolean contains(String specie, int update) {
        return this.getUpdate(specie) == update;
    }
}
