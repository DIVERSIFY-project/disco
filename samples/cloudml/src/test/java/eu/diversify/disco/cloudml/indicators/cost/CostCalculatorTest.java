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