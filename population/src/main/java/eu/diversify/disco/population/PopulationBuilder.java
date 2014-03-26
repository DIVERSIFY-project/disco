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
/*
 */
package eu.diversify.disco.population;

import eu.diversify.disco.population.decorators.ImmutablePopulation;
import eu.diversify.disco.population.decorators.DynamicPopulation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder object to simplify the construction of populations.
 *
 * This follows the 'fluent interface' pattern to ensure that the construction
 * of complex populations remain readable.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class PopulationBuilder {

    public static String DEFAULT_SPECIE_NAME_PREFIX = "sp. #";
    private final PopulationData preparation;

    public static PopulationBuilder aPopulation() {
        return new PopulationBuilder();
    }
    
    private PopulationBuilder() {
        preparation = new PopulationData();
    }

    /**
     * Create a new population, copied from the given one
     *
     * @param population the population to copy
     * @return this builder object
     */
    public PopulationBuilder clonedFrom(Population population) {
        preparation.setSpeciesNames(population.getSpeciesNames());
        preparation.setDistribution(population.getDistribution());
        return this;
    }

    /**
     * @return the resulting population
     */
    public Population build() {
        Population result = preparation.make();
        return result;
    }

    /**
     * Set the distribution of population under construction
     *
     * @param distribution an array of integer representing how many individuals
     * belong to each specie
     * @return this builder object
     */
    public PopulationBuilder withDistribution(Integer... distribution) {
        preparation.setDistribution(Arrays.asList(distribution));
        return this;
    }

    /**
     * Set the name of species (in order) of the population under construction
     *
     * @param names the list of names of species
     * @return this builder object
     */
    public PopulationBuilder withSpeciesNamed(String... names) {
        preparation.setSpeciesNames(Arrays.asList(names));
        return this;
    }

    /**
     * Turn the population under construction into an immutable one
     *
     * @return this builder object
     */
    public PopulationBuilder immutable() {
        preparation.setMutable(false);
        return this;
    }

    /**
     * Turn the population under construction into an dynamic one that is
     * synchronised with the individuals which compose it.
     *
     * @return this builder object
     */
    public PopulationBuilder dynamic() {
        preparation.setDynamic(true);
        return this;
    }

    /**
     * Hold the data needed to build a population. Ensure that population
     * objects are build in another shot.
     */
    private static class PopulationData {

        private final ArrayList<Integer> distribution;
        private final ArrayList<String> speciesNames;
        private boolean mutable;
        private boolean dynamic;

        public PopulationData() {
            distribution = new ArrayList<Integer>();
            speciesNames = new ArrayList<String>();
            setDefaultValues();
        }

        private void setDefaultValues() {
            distribution.clear();
            speciesNames.clear();
            mutable = true;
            dynamic = false;
        }

        public boolean isMutable() {
            return mutable;
        }

        public void setMutable(boolean mutable) {
            this.mutable = mutable;
        }

        public void setDistribution(List<Integer> distribution) {
            this.distribution.clear();
            this.distribution.addAll(distribution);
        }

        public void setSpeciesNames(List<String> speciesNames) {
            this.speciesNames.clear();
            this.speciesNames.addAll(speciesNames);
        }

        public boolean isDynamic() {
            return dynamic;
        }

        public void setDynamic(boolean dynamic) {
            this.dynamic = dynamic;
        }

        /**
         * Build the final population
         *
         * @return
         */
        public Population make() {
            checkValidity();
            makeDefaultSpeciesNamesIfNeeded();
            makeDefaultDistributionIfNeeded();
            Population result = new ConcretePopulation(speciesNames, distribution);
            result = makeDynamicIfNeeded(result);
            result = makeImmutableIfNeeded(result);
            setDefaultValues();
            return result;
        }

        private void checkValidity() {
            boolean valid = !dynamic || mutable; // dynamicity implies mutability
            if (!valid) {
                throw new IllegalArgumentException("Dynamic populations shall be mutable");
            }
        }

        private Population makeDynamicIfNeeded(Population population) {
            Population result = population;
            if (dynamic) {
                result = new DynamicPopulation(population);
            }
            return result;
        }

        private Population makeImmutableIfNeeded(Population population) {
            Population result = population;
            if (!mutable) {
                result = new ImmutablePopulation(population);
            }
            return result;
        }

        private void makeDefaultSpeciesNamesIfNeeded() {
            if (speciesNames.isEmpty() && !distribution.isEmpty()) {
                for (int i = 1; i <= distribution.size(); i++) {
                    String name = String.format("%s%d", Population.DEFAULT_SPECIE_NAME_PREFIX, i);
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
}
