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

import eu.diversify.disco.population.constraints.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * A builder object to simplify the construction of populations.
 *
 * This follows the 'fluent interface' pattern to ensure that the construction
 * of complex populations remain readable.
 */
public class PopulationBuilder {

    public static final boolean MUTABLE = false;
    public static final boolean FREE = false;

    private final ArrayList<Integer> distribution;
    private final ArrayList<String> speciesNames;
    private final ArrayList<Constraint> constraints;
    private boolean immutable;

    public static PopulationBuilder aPopulation() {
        return new PopulationBuilder();
    }

    private PopulationBuilder() {
        distribution = new ArrayList<Integer>();
        speciesNames = new ArrayList<String>();
        constraints = new ArrayList<Constraint>();
        setDefaultValues();
    }

    private void setDefaultValues() {
        distribution.clear();
        speciesNames.clear();
        constraints.clear();
        immutable = MUTABLE;
    }

    public PopulationBuilder fromMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry: map.entrySet()) {
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
        constraints.add(new FixedNumberOfIndividuals());
        return this;
    }

    public PopulationBuilder withFixedNumberOfSpecies() {
        constraints.add(new FixedNumberOfSpecies());
        return this;
    }

    /**
     * Ensure that the headcount of the selected species will never fall below
     * the given threshold.
     *
     * @param minimalHeadCount the threshold below which the headcount must not
     * drop
     * @param specieName the name of the specie subject to this constraint
     * @return this population builder
     */
    public PopulationBuilder withAtLeast(int minimalHeadCount, String specieName) {
        constraints.add(new AtLeast(specieName, minimalHeadCount));
        return this;
    }

    /**
     * Ensure that the headcount of the selected species will not exceed the
     * given threshold.
     *
     * @param maximalHeadCount the threshold that must not be exceeded
     * @param specieName the specie subject to this constraint
     * @return this population builder
     */
    public PopulationBuilder withAtMost(int maximalHeadCount, String specieName) {
        constraints.add(new AtMost(specieName, maximalHeadCount));
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
        Population result = new MutablePopulation(speciesNames, distribution, constraints);
        if (immutable) {
            result = new ImmutablePopulation(speciesNames, distribution, constraints);
        }
        setDefaultValues();
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
            for (String specie: speciesNames) {
                this.distribution.add(Population.DEFAULT_NUMBER_OF_INDIVIDUALS_PER_SPECIE);
            }
        }
    }
}
