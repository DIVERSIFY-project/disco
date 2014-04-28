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

package eu.diversify.disco.population;

import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.AddSpecie;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import static eu.diversify.disco.population.actions.ScriptBuilder.aScript;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConstrainedPopulationTest extends PopulationTest {

    @Override
    public PopulationBuilder aSamplePopulation() {
        return aPopulation()
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies();
    }
    
    @Test
    public void testLegalActions() {
        Population population = aSamplePopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(2, 0, 0)
                .build();

        List<Action> actual = population.allLegalActions(1);

        Action[] expected = new Action[]{
            aScript().shift("s1", -1).shift("s2", +1).build(),
            aScript().shift("s1", -1).shift("s3", +1).build(),};
        assertThat("Wrong number of actions", actual.size(), is(equalTo(2)));
        assertThat("legal actions", actual, containsInAnyOrder(expected));
    }

    @Test
    public void testLegalActionsWithScaleFactor() {
        Population population = aSamplePopulation()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(5, 5)
                .build();

        List<Action> actual = population.allLegalActions(4);

        Action[] expected = new Action[]{
            aScript().shift("s1", -4).shift("s2", +4).build(),
            aScript().shift("s2", -4).shift("s1", +4).build(),};
        assertThat("Wrong number of actions", actual.size(), is(equalTo(2)));
        assertThat("legal actions", actual, containsInAnyOrder(expected));
    }
    
    @Test
    public void testConstraintArePreservedByTheClone() {
        Population sut = aSamplePopulation().withDistribution(1, 2, 3, 4).build();
        
        Population clone = sut.deepCopy().build();
        
        assertThat("specie count change forbidden", !clone.allows(new AddSpecie()));
        assertThat("headcount change forbidden", !clone.allows(new ShiftNumberOfIndividualsIn(1, +1)));
    }
}