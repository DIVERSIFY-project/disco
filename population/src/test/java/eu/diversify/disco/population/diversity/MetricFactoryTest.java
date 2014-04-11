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

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test the behaviour of the MetricFactory
 *
 */
@RunWith(JUnit4.class)
public class MetricFactoryTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testShannonIndex() {
        final DiversityMetric m = MetricFactory.create("shannon index");
        assertThat("proper class", m, is(instanceOf(ShannonIndex.class)));
    }

    @Test
    public void testGiniSimpsonIndex() {
        final DiversityMetric m = MetricFactory.create("gini simpson index");
        assertThat("proper class", m, is(instanceOf(GiniSimpsonIndex.class)));
    }

    @Test
    public void testTrueDiversityWithoutParameter() {
        final DiversityMetric m = MetricFactory.create("true diversity");
        assertThat("proper class", m, is(instanceOf(TrueDiversity.class)));
    }

    @Test
    public void testTrueDiversityWithParameter() {
        final DiversityMetric m = MetricFactory.create("true diversity (theta = 2)");
        assertThat("proper class", m, is(instanceOf(TrueDiversity.class)));
    }

    @Test
    public void testStandardDeviation() {
        final DiversityMetric m = MetricFactory.create("standard deviation");
        assertThat("proper class", m, is(instanceOf(StandardDeviation.class)));
    }

    @Test
    public void testParserNoParameterWithLongNames() {
        String text = "yyy yy y yyy";
        MetricFactory expected = new MetricFactory();
        expected.setName("yyy yy y yyy");
        testParser(text, expected);
    }

    @Test
    public void testParserNoParameter() {
        String text = "yyy";
        MetricFactory expected = new MetricFactory();
        expected.setName("yyy");
        testParser(text, expected);
    }

    @Test
    public void testParserOneParameter() {
        String text = "yyy(p1 = 23.5)";
        MetricFactory expected = new MetricFactory();
        expected.setName("yyy");
        expected.setParameter("p1", 23.5);
        testParser(text, expected);
    }

    @Test
    public void testParserManyParameters() {
        String text = "yyy(p1 = 23.5, p2 = 6, p3 = 77)";
        MetricFactory expected = new MetricFactory();
        expected.setName("yyy");
        expected.setParameter("p1", 23.5);
        expected.setParameter("p2", 6.);
        expected.setParameter("p3", 77.);
        testParser(text, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserIllegalDescription() {
        String text = "yyy(p1 = 23.5";
        testParser(text, null);
    }

    private void testParser(String text, MetricFactory expected) {
        MetricFactory.Parser parser = new MetricFactory.Parser();
        MetricFactory actual = parser.parse(text);
        assertEquals(
                "Wrong description ",
                actual,
                expected);
    }

    @Test
    public void testDescriptionEquality() {
        MetricFactory d1 = new MetricFactory();
        d1.setName("yyy");
        d1.setParameter("p1", 23.5);
        d1.setParameter("p2", 6.);
        d1.setParameter("p2", 77.);

        assertTrue("Wrong description equality", d1.equals(d1));

        MetricFactory d2 = new MetricFactory();
        d2.setName("yyy");
        d2.setParameter("p1", 23.5);
        d2.setParameter("p2", 6.);
        d2.setParameter("p2", 77.);

        assertTrue("Wrong description equality", d1.equals(d2));

        // A parameter is missing
        MetricFactory d3 = new MetricFactory();
        d3.setName("yyy");
        d3.setParameter("p1", 23.5);
        d3.setParameter("p2", 6.);
        assertFalse("Wrong description equality", d1.equals(d3));

        // A parameter value is different
        MetricFactory d4 = new MetricFactory();
        d4.setName("yyy");
        d4.setParameter("p1", 23.5);
        d4.setParameter("p2", 6.);
        d4.setParameter("p3", 88.);
        assertFalse("Wrong description equality", d1.equals(d4));

        // A parameter name is different
        MetricFactory d5 = new MetricFactory();
        d5.setName("yyy");
        d5.setParameter("p1", 23.5);
        d5.setParameter("p2", 6.);
        d5.setParameter("px", 78.);
        assertFalse("Wrong description equality", d1.equals(d5));

        // the name of the metric is different
        MetricFactory d6 = new MetricFactory();
        d6.setName("xxx");
        d6.setParameter("p1", 23.5);
        d6.setParameter("p2", 6.);
        d6.setParameter("px", 77.);
        assertFalse("Wrong description equality", d1.equals(d6));
    }
}
