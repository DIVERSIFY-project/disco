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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the behaviour of population updates
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class UpdateTest extends TestCase {

    
    public void testUpdateMultiplication() {        
        Update u = new Update();
        u.setUpdate("Lion", 3);
        u.setUpdate("Tiger", 5);
        
        final Update u2 = u.times(4);
        assertEquals(
                "Wrong number of updates after multiplication",
                2,
                u2.getImpactedSpecies().size());
        assertEquals(
                "Wrong update multiplcation",
                12,
                u2.getUpdate("Lion"));
        assertEquals(
                "Wrong update multiplication",
                20,
                u2.getUpdate("Tiger"));
        
    }
    
    /**
     * Test the retrieval of local updates
     */
    @Test
    public void testLocalUpdateAccess() {
        Update u = new Update();
        u.setUpdate("Lion", 3);
        u.setUpdate("Tiger", 5);

        assertEquals(
                "Wrong local updates",
                3,
                u.getUpdate("Lion"));

        assertEquals(
                "Wrong default update",
                0,
                u.getUpdate("Sludge"));

        assertTrue(
                "Wrong update containment test",
                u.contains("Lion", 3));

        assertFalse(
                "Wrong update containment test",
                u.contains("Lion", 4));

    }

    /**
     * Test the execution of two updates which cancel each other out
     */
    @Test
    public void testCancellingOutUpdates() {
        Population p = new Population();
        p.addSpecie("Lion", 50);
        p.addSpecie("Sludge", 23);
        p.addSpecie("Snail", 3);

        Update u1 = new Update();
        u1.setUpdate("Lion", 3);

        Update u2 = new Update();
        u2.setUpdate("Lion", -3);

        Population result = u2.applyTo(u1.applyTo(p));
        assertEquals(
                "Erroneous update",
                p,
                result);

    }
}