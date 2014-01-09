/**
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
/**
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
package eu.diversify.disco.population;

import eu.diversify.disco.population.exceptions.DuplicateSpecieId;
import eu.diversify.disco.population.exceptions.NegativeIndividualCount;
import eu.diversify.disco.population.exceptions.UnknownSpecie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This the population models, which contains species identified by a name and
 * individuals counts.
 *
 * All modifications on the model must be made through the population object.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Population {

    private final HashMap<String, Specie> species;

    /**
     * Create a new empty population model
     */
    public Population() {
        this.species = new HashMap<String, Specie>();
    }

    /**
     * @return the total number of individuals in the population
     */
    public int getIndividualCount() {
        int sum = 0;
        for (String name : this.species.keySet()) {
            sum += this.species.get(name).getIndividualCount();
        }
        return sum;
    }

    /**
     * Create a new specie in this population
     *
     * @param name the name of the specie to create
     * @throws DuplicateSpecieId when the given specie name if already in use in
     * this population.
     *
     * @return the newly created specie.
     */
    public Specie addSpecie(String name) throws DuplicateSpecieId {
        if (this.species.containsKey(name)) {
            throw new DuplicateSpecieId(this, name);
        }
        Specie specie = new Specie(name);
        species.put(name, specie);
        return specie;
    }

    /**
     * Create a new specie in this population, identified by the given name, and
     * containing the given count of individual
     *
     * @param name the name of the specie
     * @param initialCount the initial count of individuals in the new specie
     * @throws DuplicateSpecieId when the given specie name if already in use in
     * this population.
     * @throws DuplicateSpecieId when the given specie name if already in use in
     * this population.
     * @return the newly created specie.
     */
    public Specie addSpecie(String name, int initialCount) throws DuplicateSpecieId, NegativeIndividualCount {
        if (this.species.containsKey(name)) {
            throw new DuplicateSpecieId(this, name);
        }
        Specie specie = new Specie(name, initialCount);
        species.put(name, specie);
        return specie;
    }

    /**
     * @return the collections of species in this population
     */
    public List<Specie> getSpecies() {
        ArrayList<Specie> result = new ArrayList<Specie>(species.values());
        return Collections.unmodifiableList(result);
    }

    /**
     * Extract the specie with the given name or raise an exception if no such
     * specie exists.
     *
     * @param specieName the name of the specie to look for
     * @return the specie with the specified name
     * @throws UnknownSpecie if no specie has the selected name
     */
    public Specie getSpecie(String specieName) throws UnknownSpecie {
        Specie s = this.species.get(specieName);
        if (s == null) {
            throw new UnknownSpecie(this, specieName);
        }
        return s;
    }

    /**
     * Delete the specie identified by the given name
     *
     * @param specieName the name of the specie to delete
     * @return the specie that was deleted
     */
    public Specie deleteSpecie(String specieName) {
        return this.species.remove(specieName);
    }
}
