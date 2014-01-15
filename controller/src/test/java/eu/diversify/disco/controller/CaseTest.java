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
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.QuadraticMean;
import eu.diversify.disco.population.diversity.ShannonIndex;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of the control case
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class CaseTest extends TestCase {

    @Test
    public void testCaseEquality() {
        final Population p = new Population();
        p.addSpecie("Lion", 10);
        p.addSpecie("Snails", 23);

        Case c1 = new Case(p, 0.5, new QuadraticMean());
        assertTrue(
                "A case shall be equal to itself",
                c1.equals(c1));

        final Population p2 = new Population();
        p2.addSpecie("Lion", 10);
        p2.addSpecie("Snails", 23);

        Case c2 = new Case(p2, 0.5, new QuadraticMean());
        assertTrue(
                "A case shall be equal to an equivalent case",
                c1.equals(c2));

        final Population p3 = new Population();
        p3.addSpecie("Lion", 10);
        p3.addSpecie("Snails", 23);
        p3.addSpecie("Hippos", 45);

        Case c3 = new Case(p3, 0.5, new QuadraticMean());
        assertFalse(
                "Two cases shall not be equal if their population differ",
                c1.equals(c3));

        Case c4 = new Case(p2, 0.6, new QuadraticMean());
        assertFalse(
                "Two cases shall not be equal if their reference differ",
                c1.equals(c4));

        Case c5 = new Case(p2, 0.5, new ShannonIndex());
        assertFalse(
                "Two cases shall not be equal if their metric differ",
                c1.equals(c5));
        

    }

    @Test
    public void test() {
        final Population p = new Population();
        p.addSpecie("Lion", 10);
        p.addSpecie("Snails", 23);

        Case c1 = new Case(p, 0.5, new QuadraticMean());

        final Population p2 = new Population();
        p2.addSpecie("Lion", 10);
        p2.addSpecie("Snails", 22);

        Evaluation e1 = c1.evaluate(p2);
        assertEquals(
                "Wrong error",
                e1.getError(),
                Math.pow(0.5 - c1.getMetric().normalised(p2), 2));
        assertEquals(
                "Wrong diversity",
                e1.getDiversity(),
                c1.getMetric().normalised(p2));
        assertEquals(
                "Wrong reference",
                0.5,
                c1.getReference());



    }
}