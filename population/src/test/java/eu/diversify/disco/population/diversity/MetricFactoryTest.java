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

/**
 * Test the behaviour of the MetricFactory
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class MetricFactoryTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNominalUsage() {
        MetricFactory factory = new MetricFactory();
        DiversityMetric m1 = factory.instantiate("shannon index");
        assertFalse(
                "instantiation returns null",
                m1 == null);
        assertTrue(
                "instantiation builds the wrong class",
                m1 instanceof ShannonIndex);

        DiversityMetric m2 = factory.instantiate("true Diversity (theta = 2)");
        assertFalse(
                "instantiation returns null",
                m2 == null);
        assertTrue(
                "instantiation builds the wrong class",
                m2 instanceof TrueDiversity);
        assertEquals(
                "wrong parameter values",
                2.,
                ((TrueDiversity) m2).getTheta());

    }

    @Test
    public void testParserNoParameterWithLongNames() {
        String text = "yyy yy y yyy";
        MetricFactory.Description expected = new MetricFactory.Description();
        expected.setName("yyy yy y yyy");
        testParser(text, expected);
    }

    @Test
    public void testParserNoParameter() {
        String text = "yyy";
        MetricFactory.Description expected = new MetricFactory.Description();
        expected.setName("yyy");
        testParser(text, expected);
    }

    @Test
    public void testParserOneParameter() {
        String text = "yyy(p1 = 23.5)";
        MetricFactory.Description expected = new MetricFactory.Description();
        expected.setName("yyy");
        expected.setParameter("p1", 23.5);
        testParser(text, expected);
    }

    @Test
    public void testParserManyParameters() {
        String text = "yyy(p1 = 23.5, p2 = 6, p3 = 77)";
        MetricFactory.Description expected = new MetricFactory.Description();
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

    private void testParser(String text, MetricFactory.Description expected) {
        MetricFactory.Parser parser = new MetricFactory.Parser();
        MetricFactory.Description actual = parser.parse(text);
        assertEquals(
                "Wrong description ",
                actual,
                expected);
    }

    @Test
    public void testDescriptionEquality() {
        MetricFactory.Description d1 = new MetricFactory.Description();
        d1.setName("yyy");
        d1.setParameter("p1", 23.5);
        d1.setParameter("p2", 6.);
        d1.setParameter("p2", 77.);

        assertTrue("Wrong description equality", d1.equals(d1));

        MetricFactory.Description d2 = new MetricFactory.Description();
        d2.setName("yyy");
        d2.setParameter("p1", 23.5);
        d2.setParameter("p2", 6.);
        d2.setParameter("p2", 77.);

        assertTrue("Wrong description equality", d1.equals(d2));

        // A parameter is missing
        MetricFactory.Description d3 = new MetricFactory.Description();
        d3.setName("yyy");
        d3.setParameter("p1", 23.5);
        d3.setParameter("p2", 6.);
        assertFalse("Wrong description equality", d1.equals(d3));

        // A parameter value is different
        MetricFactory.Description d4 = new MetricFactory.Description();
        d4.setName("yyy");
        d4.setParameter("p1", 23.5);
        d4.setParameter("p2", 6.);
        d4.setParameter("p3", 88.);
        assertFalse("Wrong description equality", d1.equals(d4));

        // A parameter name is different
        MetricFactory.Description d5 = new MetricFactory.Description();
        d5.setName("yyy");
        d5.setParameter("p1", 23.5);
        d5.setParameter("p2", 6.);
        d5.setParameter("px", 78.);
        assertFalse("Wrong description equality", d1.equals(d5));

        // the name of the metric is different
        MetricFactory.Description d6 = new MetricFactory.Description();
        d6.setName("xxx");
        d6.setParameter("p1", 23.5);
        d6.setParameter("p2", 6.);
        d6.setParameter("px", 77.);
        assertFalse("Wrong description equality", d1.equals(d6));
    }
}
