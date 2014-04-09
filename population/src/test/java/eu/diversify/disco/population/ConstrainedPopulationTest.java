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
/*
 */
package eu.diversify.disco.population;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.PopulationTest;
import eu.diversify.disco.population.actions.Action;
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
    public PopulationBuilder getBuilder() {
        return aPopulation()
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies();

    }
    
    
    

    @Test
    public void testLegalActions() {
        Population population = aPopulation()
                .withSpeciesNamed("s1", "s2", "s3")
                .withDistribution(2, 0, 0)
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
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
        Population population = aPopulation()
                .withSpeciesNamed("s1", "s2")
                .withDistribution(5, 5)
                .withFixedNumberOfIndividuals()
                .withFixedNumberOfSpecies()
                .build();

        List<Action> actual = population.allLegalActions(4);

        Action[] expected = new Action[]{
            aScript().shift("s1", -4).shift("s2", +4).build(),
            aScript().shift("s2", -4).shift("s1", +4).build(),};
        assertThat("Wrong number of actions", actual.size(), is(equalTo(2)));
        assertThat("legal actions", actual, containsInAnyOrder(expected));
    }
}