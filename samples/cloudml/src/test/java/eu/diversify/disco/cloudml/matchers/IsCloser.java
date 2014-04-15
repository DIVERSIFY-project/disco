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
 */

package eu.diversify.disco.cloudml.matchers;

import eu.diversify.disco.cloudml.transformations.ToPopulation;
import eu.diversify.disco.population.Population;
import org.cloudml.core.DeploymentModel;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


public class IsCloser extends TypeSafeMatcher<DeploymentModel> { 

    final Population reference;
    final Population opponent;
        
    public IsCloser(Population reference, DeploymentModel opponent) {
        this.reference = reference;
        this.opponent = new ToPopulation().applyTo(opponent);
    }

    @Override
    public boolean matchesSafely(DeploymentModel deployment) {
        Population actual = new ToPopulation().applyTo(deployment);
        return distanceBetween(reference, actual) <= distanceBetween(reference, opponent);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("reference population");
    }
    
    private static double distanceBetween(Population reference, Population other) {
        double sum = 0;
        for (String specieName: reference.getSpeciesNames()) {
            sum += Math.pow(reference.getHeadcountIn(specieName) - other.getHeadcountIn(specieName), 2);
        }
        return Math.sqrt(sum);
    }
    
    
}
