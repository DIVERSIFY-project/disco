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

package eu.diversify.disco.controller.solvers;

import eu.diversify.disco.controller.solvers.searches.HillClimbing;
import eu.diversify.disco.controller.solvers.searches.IterativeSearch;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@RunWith(JUnit4.class)
public class HillClimbingTest extends SolverTest {

    @Override
    public IterativeSearch solverUnderTest() {
        return new IterativeSearch(new HillClimbing());
    }

    @Override
    public void verifySolverName(String solverName) {
        final String name = solverName.toLowerCase();
        assertThat("solver name", name, containsString("hill"));
        assertThat("solver name", name, containsString("climbing"));
    }
    
}