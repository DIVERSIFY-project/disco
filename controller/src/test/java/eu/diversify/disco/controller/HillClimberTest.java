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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Check the behaviour of the HillClimber controller
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class HillClimberTest extends ControllerTest {
    
    public Controller factory() {
        return new HillClimber(new TrueDiversity());
    }
    
    
    /**
     * Test the extraction of legal updates
     */
    @Test
    public void testLegalUpdates() {
        final HillClimber controller = (HillClimber) factory();
        final Population p = new Population();
        p.addSpecie("Lion", 2);
        p.addSpecie("Tiger", 0);
        final List<Update> updates = controller.getLegalUpdates(p);
        assertEquals(
                "Wrong number of updates",
                1,
                updates.size()
                );
        
        assertEquals(
                "Wrong negative update",
                -1,
                updates.get(0).getUpdate("Lion")
                );
        assertEquals(
                "Wrong positive update",
                +1,
                updates.get(0).getUpdate("Tiger")
                );
        assertEquals(
                "Wrong number of species impacted",
                2,
                updates.get(0).getImpactedSpecies().size());

        
        final Population p2 = new Population();
        p2.addSpecie("Lion", 5);
        p2.addSpecie("Tiger", 2);
        List<Update> updates2 = controller.getLegalUpdates(p2);
        assertEquals(
                "Wrong number of updates",
                2,
                updates2.size()
                );
        assertEquals(
                "Wrong first update",
                -1,
                updates2.get(0).getUpdate("Lion")
                );
        assertEquals(
                "Wrong first update",
                1,
                updates2.get(0).getUpdate("Tiger")
                );
        
        
    }
    
}