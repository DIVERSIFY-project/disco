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
 * An alternative diversity metric based on the standard deviation of relative
 * species size
 */
public class StandardDeviation extends AbstractDiversityMetric {
    public static final String STANDARD_DEVIATION_NAME = "standard deviation";

    @Override
    protected double computeAbsolute(int totalNumberOfIndividuals, double[] fractions) {
        final double mu = 1D / fractions.length;
        double total = 0D;
        for (int i=0 ; i<fractions.length ; i++) {
            total += Math.pow(fractions[i] - mu, 2);
        }
        double sd =  Math.sqrt(total / fractions.length);
        return 1D / (1D + sd);
    }
    
    public String getName() {
        return STANDARD_DEVIATION_NAME;
    }
}
