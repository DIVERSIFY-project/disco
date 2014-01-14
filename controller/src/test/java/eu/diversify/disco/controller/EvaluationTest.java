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
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.QuadraticMean;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Check the behaviour of the result object
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class EvaluationTest extends TestCase {

    /**
     * Test the equality operator between two evaluations
     */
    @Test
    public void testEquality() {
        final Population p = new Population();
        p.addSpecie("Lion", 5);
        p.addSpecie("Sludge", 4);

        Case c = new Case(p, 0.5, new QuadraticMean());
        final Evaluation e1 = c.evaluate(p);
        final Evaluation e2 = c.evaluate(p);
        assertTrue(
                "Two evaluations of similar populations for a given case shall be equal",
                e1.equals(e2));

        final Population p2 = new Population();
        p2.addSpecie("Lion", 6);
        p2.addSpecie("Sludge", 3);
        final Evaluation e3 = c.evaluate(p2);
        assertFalse(
                "Two evaluations of two different populations shall not be equal",
                e1.equals(e3));

    }

    /**
     * Test the linkage between evaluation of partial solutions
     */
    @Test
    public void testLinkage() {

        final Population p = new Population();
        p.addSpecie("Lion", 5);
        p.addSpecie("Sludge", 4);

        final Case c = new Case(p, 0.5, new QuadraticMean());

        final Population p2 = new Population();
        p2.addSpecie("Lion", 6);
        p2.addSpecie("Sludge", 3);

        final Evaluation e1 = c.evaluate(p2);
        assertFalse(
                "Shall not have a previous evaluation",
                e1.hasPrevious());
        assertEquals(
                "Wrong number of iteration",
                1,
                e1.getIteration());

        
        final Update u = new Update();
        u.setUpdate("Lion", +1);
        u.setUpdate("Sludge", -1);
        
        final Evaluation e2 = e1.refineWith(u);
        assertTrue(
                "Shall have a previous evaluation",
                e2.hasPrevious());
        assertNotNull(
                "The previous shall not be null",
                e2.getPrevious());
        assertEquals(
                "Wrong number of iteration",
                1,
                e1.getIteration());
    }

    /**
     * Test the formatting of the result
     */
    @Test
    public void testFormatting() {

        final Population p = new Population();
        p.addSpecie("Lion", 5);
        p.addSpecie("Sludge", 4);
        final Case c = new Case(p, 0.5, new QuadraticMean());

        final Evaluation result = c.evaluate(p);
        final String text = result.toString();
        assertEquals(
                "Wrong formatting of evaluation",
                " - Iteration count: 1\n - Population: {Lion: 5, Sludge: 4}\n - Reference: 0.5\n - Diversity: 0.9791177639715973\n - Error: 0.22955383175314326\n",
                text);
    }
}