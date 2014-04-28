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

import eu.diversify.disco.population.actions.Action;
import java.util.List;
import java.util.Map;

/**
 * The general interface of population as a mapping from species names to number
 * of individuals in species.
 */
public interface Population extends Iterable<Specie> {

    /**
     * FIXME: What is the meaning of a population with no specie? is it an empty
     * population? Is it a legal population?
     */
    public static final String DEFAULT_SPECIE_NAME_FORMAT = "s%d";
    public static final String DEFAULT_SPECIE_NAME_REGEX = "^s(\\d+)$";
    public static final Integer DEFAULT_NUMBER_OF_INDIVIDUALS_PER_SPECIE = 0;

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

    /**
     * @return a complete copy of the population
     */
    public PopulationBuilder deepCopy();

    /**
     * @return true if the given action is permitted on this population
     * @param action the action which relevance is to be tested
     */
    public boolean allows(Action action);

    /**
     * @return the list of all legal actions which can be performed on this
     * populations
     */
    public List<Action> allLegalActions(int scaleFactor);

    /**
     * @return the number of species
     */
    public int getSpeciesCount();

    
    /**
     * @return the specie at the given position
     * @param index the index of the specie of interest
     */
    public Specie getSpecie(int index);

    /**
     * @return the specie with a given name if it exists  
     * @param name the name of the specie of interest
     */
    public Specie getSpecie(String name);
    
    /**
     * @param specieName the name of the specie of interest
     * @return the index of the specie
     * @throws IllegalArgumentException if the given specie name is irrelevant
     */
    public int getSpecieIndex(String specieName);

    /**
     * @return the mean number of individuals per specie
     */
    public double getMeanHeadcount();

    /**
     * @return the variance of the number of individuals in each specie
     */
    public double getVariance();

    /**
     * @return the total number of individual in the whole population
     */
    public int getTotalHeadcount();

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
     * Create a new specie, with the given and no individual
     *
     * @param specieName the name of the new specie to create
     * @return the modified population
     */
    public Population addSpecie(String specieName);

    /**
     * @return return this population but where a new specie has been added with a
     * default name.
     */
    public Population addSpecie();
    
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
