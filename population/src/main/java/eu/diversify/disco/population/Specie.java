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


package eu.diversify.disco.population;

import eu.diversify.disco.population.exceptions.NegativeIndividualCount;

/**
 * Represent a single specie. Each specie is identified within a population by
 * its name.
 *
 * The name of specie cannot be changed
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Specie {

    private final String name;
    private int individualCount;

    /**
     * Create an new species without any individuals.
     *
     * @param name the name of this specie
     */
    Specie(String name) {
        this.name = name;
        this.individualCount = 0;
    }
    

    /**
     * Create a new specie with a given name and initial individual count
     *
     * @param name the name of this new specie
     * @param initialIndividualCount the initial number of individual belonging
     *
     * @throws NegativeIndividualCount when the given initial count is negative
     */
    Specie(String name, int initialIndividualCount) throws NegativeIndividualCount {
        if (initialIndividualCount < 0) {
            throw new NegativeIndividualCount(this, initialIndividualCount);
        }
        this.name = name;
        this.individualCount = initialIndividualCount;
    }

    /**
     * @return the name of this specie
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the number of individual in this specie
     */
    public int getIndividualCount() {
        return this.individualCount;
    }

    /**
     * Update the number of individual in this specie
     *
     * @param count the count of individual in this specie
     * @throws NegativeIndividualCount when the given count of individual is
     * negative
     */
    public void setIndividualCount(int count) throws NegativeIndividualCount {
        if (count < 0) {
            throw new NegativeIndividualCount(this, count);
        }
        this.individualCount = count;
    }
}
