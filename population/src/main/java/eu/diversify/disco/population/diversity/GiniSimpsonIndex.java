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

package eu.diversify.disco.population.diversity;

/**
 * Implementation of the Gini-Simpson index
 */
public class GiniSimpsonIndex extends AbstractDiversityMetric {
    
    private static final String GINI_SIMPSON_INDEX_NAME = "Gini-Simpson index";

    @Override
    protected double computeAbsolute(int n, double[] fractions) {
        double sum = 0.;
        for (int i=0 ; i<fractions.length ; i++) {
            sum += Math.pow(fractions[i], 2);
        }
        return 1 - sum;
    }
    
    
    public String getName() {
        return GINI_SIMPSON_INDEX_NAME;
    }
}
