/*
 */
package eu.diversify.disco.cloudml.indicators.cost;

/**
 * Test the behaviour of the the calculation of the cost as a linear function of
 * the size.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class CostAsSizeTest extends CostCalculatorTest {

    @Override
    protected CostCalculator makeCostCalculator() {
        return new CostAsSize();
    }
}