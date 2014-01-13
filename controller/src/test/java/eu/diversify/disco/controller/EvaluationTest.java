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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
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
     * Test the linkage between evaluation of partial solutions
     */
    @Test
    public void testLinkage() {
        final Population p = new Population();
        p.addSpecie("Lion", 5);
        p.addSpecie("Sludge", 4);

        final Evaluation e1 = new Evaluation(p, 0.5, 0.45, 0.025);
        assertFalse(
                "Shall not have a previous evaluation",
                e1.hasPrevious());
        assertEquals(
                "Wrong number of iteration",
                1,
                e1.getIteration());

        final Evaluation e2 = new Evaluation(e1, p, 0.34, 0.23, 0.0034);
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

        final Evaluation result = new Evaluation(p, 0.5, 0.45, 0.025);
        final String text = result.toString();
        assertEquals(
                "Wrong formatting of evaluation",
                " - Iteration count: 1\n - Population: {Lion: 5, Sludge: 4}\n - Reference: 0.5\n - Diversity: 0.45\n - Error: 0.025\n",
                text);
    }
}