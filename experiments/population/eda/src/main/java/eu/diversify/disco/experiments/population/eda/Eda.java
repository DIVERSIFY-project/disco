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

/**
 * Experiment where we generate a large number of population, uniformly
 * distributed, and we log various property for exploratory data analysis.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Eda implements Experiment {
    
    private final static Field INDIVIDUAL_COUNT = new Field("individual count", Integer.class);
    private final static Field SPECIE_COUNT = new Field("specie count", Integer.class);
    private final static Field DIVERSITY = new Field("diversity", Double.class);
    private final static Schema SCHEMA = new Schema(Arrays.asList(new Field[]{
        INDIVIDUAL_COUNT,
        SPECIE_COUNT,
        DIVERSITY
    }), "n/a");
    
    private final EdaSetup setup;
            
    
    public Eda(EdaSetup setup) {
        this.setup = setup;
    }

    @Override
    public List<DataSet> run() {
        final DiversityMetric diversity = new TrueDiversity(2);
        final DataSet result = new DataSet(SCHEMA);

        final Generator generator = new Generator();
        final Iterator<Population> populations = generator.makeMany(setup.getSampleCount(), setup.getPopulation());
        while(populations.hasNext()) {
            final Population p = populations.next();
            final Data data = SCHEMA.newData();
            data.set(DIVERSITY, diversity.normalised(p));
            data.set(INDIVIDUAL_COUNT, p.getIndividualCount());
            data.set(SPECIE_COUNT, p.getSpecies().size());
            result.add(data);
        }
        
        ArrayList<DataSet> results = new ArrayList<DataSet>();
        results.add(result);
        return results;
    }
        
}
