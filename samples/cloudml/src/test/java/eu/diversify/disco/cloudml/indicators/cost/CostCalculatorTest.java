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
package eu.diversify.disco.cloudml.indicators.cost;

import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicatorTest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;

/**
 * Test the behaviour of cost calculator
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class CostCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testCostOrdering() throws FileNotFoundException, IOException {
        CostCalculator cost = makeCostCalculator();
        DeploymentModel cheapModel = makeCheapDeploymentModel();
        double lowPrice = cost.evaluateOn(cheapModel);
        DeploymentModel expensiveModel = makeExpensiveDeploymentModel();
        double highPrice = cost.evaluateOn(expensiveModel);
        assertTrue(
                "Cheap models shall cost less that expensive ones",
                lowPrice <= highPrice);
    }

    protected abstract CostCalculator makeCostCalculator();

    private DeploymentModel makeCheapDeploymentModel() throws FileNotFoundException, IOException {
        JsonCodec codec = new JsonCodec();
        InputStream stream = new FileInputStream("../src/test/resources/sensappAdmin.json");
        DeploymentModel model = (DeploymentModel) codec.load(stream);
        stream.close();
        return model;
    }

    private DeploymentModel makeExpensiveDeploymentModel() throws FileNotFoundException, IOException {
        return makeCheapDeploymentModel();
    }

    @Override
    final protected DeploymentIndicator makeDeploymentIndicator() {
        return makeCostCalculator();
    }
}