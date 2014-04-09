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

import eu.diversify.disco.population.constraints.Constraint;
import eu.diversify.disco.population.constraints.FixedNumberOfIndividuals;
import eu.diversify.disco.population.constraints.FixedNumberOfSpecies;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * A builder object to simplify the construction of populations.
 *
 * This follows the 'fluent interface' pattern to ensure that the construction
 * of complex populations remain readable.
 *
 */
public class PopulationBuilder {

    public static final boolean MUTABLE = false;
    public static final boolean FREE = false;
    private final ArrayList<Integer> distribution;
    private final ArrayList<String> speciesNames;
    private boolean immutable;
    private boolean fixedNumberOfSpecies;
    private boolean fixedNumberOfIndividuals;

    public static PopulationBuilder aPopulation() {
        return new PopulationBuilder();
    }

    private PopulationBuilder() {
        distribution = new ArrayList<Integer>();
        speciesNames = new ArrayList<String>();
        setDefaultValues();
    }

    private void setDefaultValues() {
        immutable = MUTABLE;
        fixedNumberOfSpecies = FREE;
        fixedNumberOfIndividuals = FREE;
    }

    /**
     * Create a new population, copied from the given one
     *
     * @param population the population to copy
     * @return this builder object
     */
    public PopulationBuilder clonedFrom(Population population) {
        withSpeciesNamed(population.getSpeciesNames());
        withDistribution(population.getDistribution());
        return this;
    }
    
    
    public PopulationBuilder fromMap(Map<String, Integer> map) {
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            speciesNames.add(entry.getKey());
            distribution.add(entry.getValue());
        }
        return this;
    }
    
    /**
     * Set the distribution of population under construction
     *
     * @param distribution an array of integer representing how many individuals
     * belong to each specie
     * @return this builder object
     */
    public PopulationBuilder withDistribution(Integer... distribution) {
        return withDistribution(Arrays.asList(distribution));
    }

    public PopulationBuilder withDistribution(Collection<Integer> distribution) {
        this.distribution.clear();
        this.distribution.addAll(distribution);
        return this;
    }

    /**
     * Set the name of species (in order) of the population under construction
     *
     * @param names the list of names of species
     * @return this builder object
     */
    public PopulationBuilder withSpeciesNamed(String... names) {
        return withSpeciesNamed(Arrays.asList(names));
    }

    public PopulationBuilder withSpeciesNamed(Collection<String> names) {
        speciesNames.clear();
        speciesNames.addAll(names);
        return this;
    }

    public PopulationBuilder withFixedNumberOfIndividuals() {
        fixedNumberOfIndividuals = true;
        return this;
    }

    public PopulationBuilder withFixedNumberOfSpecies() {
        fixedNumberOfSpecies = true;
        return this;
    }

    /**
     * Turn the population under construction into an immutable one
     *
     * @return this builder object
     */
    public PopulationBuilder immutable() {
        immutable = true;
        return this;
    }

    /**
     * @return the resulting population
     */
    public Population build() {
        makeDefaultSpeciesNamesIfNeeded();
        makeDefaultDistributionIfNeeded();
        Population result = new ConcretePopulation(speciesNames, distribution);
        result = setupConstraints(result);
        result = makeImmutableIfNeeded(result);
        setDefaultValues();
        return result;
    }

    private Population setupConstraints(Population population) {
        ArrayList<Constraint> constraints = new ArrayList<Constraint>();
        if (fixedNumberOfIndividuals) {
            constraints.add(new FixedNumberOfIndividuals());
        }
        if (fixedNumberOfSpecies) {
            constraints.add(new FixedNumberOfSpecies());
        }
        if (!constraints.isEmpty()) {
            return new ConstrainedPopulation(population, constraints);
        }
        return population;
    }

    private Population makeImmutableIfNeeded(Population population) {
        Population result = population;
        if (immutable) {
            result = new ImmutablePopulation(population);
        }
        return result;
    }

    private void makeDefaultSpeciesNamesIfNeeded() {
        if (speciesNames.isEmpty() && !distribution.isEmpty()) {
            for (int i = 1; i <= distribution.size(); i++) {
                String name = String.format(Population.DEFAULT_SPECIE_NAME_FORMAT, i);
                speciesNames.add(name);
            }
        }
    }

    private void makeDefaultDistributionIfNeeded() {
        if (distribution.isEmpty() && !speciesNames.isEmpty()) {
            for (String specie : speciesNames) {
                this.distribution.add(Population.DEFAULT_NUMBER_OF_INDIVIDUALS_PER_SPECIE);
            }
        }
    }
}
