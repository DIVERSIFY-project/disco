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
package eu.diversify.disco.population;

import eu.diversify.disco.population.actions.Action;
import java.util.List;
import java.util.Map;

/**
 * The general interface of population as a mapping from species names to number
 * of individuals in species.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Population {

    /**
     * FIXME: What is the meaning of a population with no specie? is it an empty
     * population? Is it a legal population?
     */
    public static final String DEFAULT_SPECIE_NAME_PREFIX = "sp. #";
    public static final Integer DEFAULT_NUMBER_OF_INDIVIDUALS_PER_SPECIE = 0;

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

    /**
     * @return a complete copy of the population
     */
    public Population deepCopy();

    /**
     * @param specieIndex the index of the specie from interest, starting from 1
     * @return the number of individuals in the specie at index 'specieIndex'
     * @throws IllegalArgumentException if the given index is irrelevant
     */
    public int getNumberOfIndividualsIn(int specieIndex);

    /**
     * @param specieName the name of the specie of interest
     * @return the number of individuals in the selected specie
     * @throws IllegalArgumentException if the given specie name is irrelevant
     */
    public int getNumberOfIndividualsIn(String specieName);

    /**
     * @return the number of species
     */
    public int getNumberOfSpecies();

    /**
     * @param specieName the name of the specie of interest
     * @return the index of the specie
     * @throws IllegalArgumentException if the given specie name is irrelevant
     */
    public int getSpecieIndex(String specieName);

    /**
     * @return the mean number of individuals per specie
     */
    public double getMeanNumberOfIndividuals();

    /**
     * @return the variance of the number of individuals in each specie
     */
    public double getVariance();

    /**
     * @return the total number of individual in the whole population
     */
    public int getTotalNumberOfIndividuals();

    /**
     * @return the list of species name ordered by index
     */
    public List<String> getSpeciesNames();

    /**
     * @return the list of number of individuals contained in each species,
     * ordered by specie index
     */
    public List<Integer> getDistribution();

    /**
     * @param specieIndex the index of the population of interest, starting at 1
     * @return the fraction of the whole population as a scalar value in [0, 1]
     * @throws IllegalArgumentException if the given index is irrelevant
     */
    public double getFractionIn(int specieIndex);

    /**
     * @param specieName the name of specie of interest
     * @return the fraction of the whole population as a scalar value in [0, 1]
     * @throws IllegalArgumentException if the given name is irrelevant
     */
    public double getFractionIn(String specieName);

    /**
     * Check whether the population contains a specie with the given name
     *
     * @param specieName the name of interest
     * @return true if the population does contains a specie with the given name
     */
    public boolean hasAnySpecieNamed(String specieName);

    /**
     * @return true if the population does not contains any individuals (i.e.,
     * whether the total number of individuals is zero)
     */
    public boolean isEmpty();

    /**
     * Update the name of the specie at in the given index, with the given name
     *
     * @param specieIndex the index of interest
     * @param newName the new name to use for the selected specie
     * @return the modified population
     */
    public Population renameSpecie(int specieIndex, String newName);

    /**
     * Update the name of the specie at in the given index, with the given name
     *
     * @param oldName the name of the specie whose name must be changed
     * @param newName the new name to use for the selected specie
     * @return the modified population
     */
    public Population renameSpecie(String oldName, String newName);

    /**
     * Create a new specie, with the given and no individual
     *
     * @param specieName the name of the new specie to create
     * @return the modified population
     */
    public Population addSpecie(String specieName);

    /**
     * Remove the selected specie from the population
     *
     * @param specieIndex the index of the specie to remove
     * @return the modified population
     * @throws IllegalArgumentException if the given index is irrelevant
     */
    public Population removeSpecie(int specieIndex);

    /**
     * Remove the selected specie from the population
     *
     * @param specieName the name of the specie to remove
     * @return the modified population
     * @throws IllegalArgumentException if the given name is irrelevant
     */
    public Population removeSpecie(String specieName);

    /**
     * Set the number of individual in the selected population
     *
     * @param specieIndex the index of the specie of interest
     * @param numberOfIndividuals the number of individual to associate with the
     * selected specie
     * @return the modified population
     * @throws IllegalArgumentException if the given index is irrelevant
     */
    public Population setNumberOfIndividualsIn(int specieIndex, int numberOfIndividuals);

    /**
     * Set the number of individual in the selected population
     *
     * @param specieIndex the name of the specie of interest
     * @param numberOfIndividuals the number of individual to associate with the
     * selected specie
     * @return the modified population
     * @throws IllegalArgumentException if the given index is irrelevant
     */
    public Population setNumberOfIndividualsIn(String specieName, int numberOfIndividuals);

    /**
     * Adjust the number of individual in the selected specie by the given
     * addend
     *
     * @param specieIndex the index of the specie whose number of individuals
     * shall be adjusted
     * @param offset the change to add to the number of specie
     * @return the modified population
     * @throws IllegalArgumentException if the given index is irrelevant
     */
    public Population shiftNumberOfIndividualsIn(int specieIndex, int offset);

    /**
     * Adjust the number of individual in the selected specie by the given
     * addend
     *
     * @param specieIndex the index of the specie whose number of individuals
     * shall be adjusted
     * @param offset the change to add to the number of specie
     * @return the modified population
     * @throws IllegalArgumentException if the given name is irrelevant
     */
    public Population shiftNumberOfIndividualsIn(String specieName, int offset);

    /**
     * @param target the population to be compared to
     * @return a list of actions, which, applied on this population, would
     * produce the target population.
     */
    public List<Action> differenceWith(Population target);

    /**
     * @return the list of species name by alphabetical order
     */
    public List<String> sortSpeciesNamesAlphabetically();

    /**
     * @return true if the population is uniformly distributed, i.e., if the
     * variance is 0.
     */
    public boolean isUniformlyDistributed();

    /**
     * @return a string representing the content of the population
     */
    public String toString();

    /**
     * @return an array containing the relative distribution of the population
     */
    public double[] toArrayOfFractions();

    /**
     * Convert the population into a mapping from specie name to individual
     * counts
     *
     * @return an equivalent map
     */
    public Map<String, Integer> toMap();
}
