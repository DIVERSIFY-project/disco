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
package eu.diversify.disco.cloudml.indicators;

import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the general behaviour of indicators
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class DeploymentIndicatorTest extends TestCase {
    
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(expected = IllegalArgumentException.class)
    public void testIndicatorAppliedOfNull() {
        DeploymentIndicator indicator = makeDeploymentIndicator();
        indicator.evaluateOn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRobustnessOfEmptyCloudMLDeployment() {
        DeploymentIndicator indicator = makeDeploymentIndicator();
        indicator.evaluateOn(new DeploymentModel());
    }

    protected abstract DeploymentIndicator makeDeploymentIndicator();
    
}