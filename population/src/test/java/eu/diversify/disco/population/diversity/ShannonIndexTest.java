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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test the behaviour of the Shannon Index
 */
public class ShannonIndexTest extends DiversityMetricTest {

    @Override
    public DiversityMetric normalisedMetric() {
        return new NormalisedDiversityMetric(new ShannonIndex());
    }
    
    @Test
    public void nameShouldBeAboutShannonIndex() {
        ShannonIndex si = new ShannonIndex();
        final String lowerCase = si.getName().toLowerCase();
        assertThat("shannon index name", lowerCase, containsString("shannon"));
        assertThat("shannon index name", lowerCase, containsString("index"));
    }
    
}
