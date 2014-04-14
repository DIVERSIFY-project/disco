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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


/**
 * Test the Gini-Simpson index metric
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class GiniSimpsonIndexTest extends DiversityMetricTest {

    @Override
    public DiversityMetric normalisedMetric() {
        return new NormalisedDiversityMetric(new GiniSimpsonIndex());
    }
    
      @Test
    public void testName() {
        GiniSimpsonIndex si = new GiniSimpsonIndex();
        final String lowerCase = si.getName().toLowerCase(); 
        assertThat("index name", lowerCase, containsString("gini"));
        assertThat("index name", lowerCase, containsString("simpson"));
        assertThat("index name", lowerCase, containsString("index"));
    }
}
