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
package eu.diversify.disco.population.diversity;

import eu.diversify.disco.population.Population;

/**
 * General interface of diversity metrics, modelled as a function from
 * population to Real values.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class AbstractDiversityMetric implements DiversityMetric {

    
    @Override
    public final double applyTo(Population population) {
        if (population.isEmpty()) {
            throw new IllegalArgumentException("Diversity is not defined for empty populations!");
        }
        double[] fractions = population.toArrayOfFractions();
        return computeAbsolute(population.getTotalNumberOfIndividuals(), fractions);
    }

    
    protected abstract double computeAbsolute(int totalNumberOfIndividuals, double[] fractions);

    @Override
    public final double maximum(Population population) {
        double[] fractions = new double[population.getNumberOfSpecies()];
        int total = population.getTotalNumberOfIndividuals();
        double mean = population.getMeanNumberOfIndividuals() / total;
        for (int i = 0; i < fractions.length; i++) {
            fractions[i] = mean;
        }
        return computeAbsolute(total, fractions);
    }

    @Override
    public final double minimum(Population population) {
        double[] fractions = new double[population.getNumberOfSpecies()];
        int total = population.getTotalNumberOfIndividuals();
        fractions[0] = 1D;
        return computeAbsolute(total, fractions);
    }


    @Override
    public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    @Override
    public final DiversityMetric normalise() {
        return new NormalisedDiversityMetric(this);
    }
    
    
}
