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
package eu.diversify.disco.experiments.population.eda;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.Data;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.experiments.commons.data.Field;
import eu.diversify.disco.experiments.commons.data.Schema;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import eu.diversify.disco.population.random.Generator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Experiment where we generate a large number of population, uniformly
 * distributed, and we log various property for exploratory data analysis.
 */
public class Eda implements Experiment {

    final static Field INDIVIDUAL_COUNT = new Field("individual count", Integer.class);
    final static Field SPECIE_COUNT = new Field("specie count", Integer.class);
    final static Field DIVERSITY_BEFORE = new Field("diversity before", Double.class);
    final static Field DIVERSITY_AFTER = new Field("diversity after", Double.class);
    final static Field SOURCE_SPECIE = new Field("source specie", Integer.class);
    final static Field TARGET_SPECIE = new Field("target specie", Integer.class);
    final static Schema RESULT_SCHEMA = new Schema(Arrays.asList(new Field[]{
        INDIVIDUAL_COUNT,
        SPECIE_COUNT,
        SOURCE_SPECIE,
        TARGET_SPECIE,
        DIVERSITY_BEFORE,
        DIVERSITY_AFTER,}), "n/a");
    final DiversityMetric diversity = new TrueDiversity(2).normalise();
    private final EdaSetup setup;

    public Eda(EdaSetup setup) {
        this.setup = setup;
    }

    @Override
    public List<DataSet> run() {
        final DataSet result = new DataSet(RESULT_SCHEMA);

        final Generator generator = new Generator();
        final Iterator<Population> populations = generator.makeMany(setup.getSampleCount(), setup.getPopulation());
        while (populations.hasNext()) {
            final Population p = populations.next();
            final Data data = RESULT_SCHEMA.newData();
            sampleImpactOnDiversity(data, p);
            result.add(data);
        }

        ArrayList<DataSet> results = new ArrayList<DataSet>();
        results.add(result);
        return results;
    }

    void sampleImpactOnDiversity(Data data, Population population) {
        data.set(INDIVIDUAL_COUNT, population.getTotalNumberOfIndividuals());
        data.set(SPECIE_COUNT, population.getNumberOfSpecies());

        data.set(DIVERSITY_BEFORE, diversity.applyTo(population));

        String source = chooseAnySpecieFrom(nonEmptySpecies(population));
        data.set(SOURCE_SPECIE, population.getNumberOfIndividualsIn(source));

        String target = chooseAnySpecieFrom(excludeSpecieNamed(population, source));
        data.set(TARGET_SPECIE, population.getNumberOfIndividualsIn(target));

        population.shiftNumberOfIndividualsIn(source, -1);
        population.shiftNumberOfIndividualsIn(target, +1);

        data.set(DIVERSITY_AFTER, diversity.applyTo(population));
    }

    // FIXME: extractAllNonEmptySpecies() Should be a feature of the population class
    List<String> nonEmptySpecies(Population population) {
        ArrayList<String> nonEmptySpecies = new ArrayList<String>(population.getNumberOfSpecies());
        for (String specie: population.getSpeciesNames()) {
            if (population.getNumberOfIndividualsIn(specie) > 0) {
                nonEmptySpecies.add(specie);
            }
        }
        return nonEmptySpecies;
    }

    String chooseAnySpecieFrom(List<String> candidates) {
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("Cannot choose an element in an empty collection");
        }
        final Random random = new Random();
        return candidates.get(random.nextInt(candidates.size()));
    }

    List<String> excludeSpecieNamed(Population population, String toExclude) {
        final ArrayList<String> filtered = new ArrayList<String>(population.getNumberOfSpecies());
        for (String specie: population.getSpeciesNames()) {
            if (!specie.equals(toExclude)) {
                filtered.add(specie);
            }
        }
        return filtered;
    }
}
