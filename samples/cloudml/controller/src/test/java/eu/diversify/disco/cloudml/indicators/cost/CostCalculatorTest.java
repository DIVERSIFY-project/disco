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

package eu.diversify.disco.cloudml.indicators.cost;

import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicatorTest;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SensApp;
import org.junit.Test;

/**
 * Test the behaviour of cost calculator
 */
public abstract class CostCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testCostOrdering() throws FileNotFoundException, IOException {
        CostCalculator cost = makeCostCalculator();
        Deployment cheapModel = makeCheapDeployment();
        double lowPrice = cost.evaluateOn(cheapModel);
        Deployment expensiveModel = makeExpensiveDeployment();
        double highPrice = cost.evaluateOn(expensiveModel);
        assertTrue(
                "Cheap models shall cost less that expensive ones",
                lowPrice <= highPrice);
    }

    protected abstract CostCalculator makeCostCalculator();

    private Deployment makeCheapDeployment() throws FileNotFoundException, IOException {
        return SensApp.completeSensApp().build();
    }

    private Deployment makeExpensiveDeployment() throws FileNotFoundException, IOException {
        return makeCheapDeployment();
    }

    @Override
    final protected DeploymentIndicator makeDeploymentIndicator() {
        return makeCostCalculator();
    }
}