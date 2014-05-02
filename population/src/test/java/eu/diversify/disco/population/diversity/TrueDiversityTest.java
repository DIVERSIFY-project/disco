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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Test the true diversity used as a diversity metric.
 */
@RunWith(JUnit4.class)
public class TrueDiversityTest extends DiversityMetricTest {

    @Override
    public DiversityMetric normalisedMetric() {
        return new NormalisedDiversityMetric(new TrueDiversity());
    }

    @Test
    public void nameShouldBeAboutTrueDiversity() {
        TrueDiversity td = new TrueDiversity(3); 
        final String lowerCase = td.getName().toLowerCase(); 
        assertThat("index name", lowerCase, containsString("true"));
        assertThat("index name", lowerCase, containsString("diversity"));
        assertThat("index name", lowerCase, containsString("theta"));
        assertThat("index name", lowerCase, containsString("3"));
    }
}
