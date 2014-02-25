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

/**
 * Implementation of the diversity metric as a quadratic mean of the species
 * population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class TrueDiversity extends AbstractDiversityMetric {

    private final double theta;

    /**
     * Create a new True Diversity metric, by default based on a quadratic mean
     */
    public TrueDiversity() {
        this.theta = 2;
    }

    /**
     * @return the theta parameter
     */
    public double getTheta() {
        return this.theta;
    }

    /**
     * Create a customised true diversity metric
     *
     * @param theta the theta parameter which select the underlying means
     * (geometric, arithmetic, quadratic, etc.)
     */
    public TrueDiversity(double theta) {
        this.theta = theta;
    }


    @Override
    protected double computeAbsolute(int totalNumberOfIndividuals, double[] fractions) {
        double sum = 0.;
        for (int i=0 ; i<fractions.length ; i++) {
            sum += Math.pow(fractions[i], theta);
        }
        return Math.pow(sum / fractions.length, -1D / theta);
    }
}
