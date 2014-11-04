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
package eu.diversify.disco.cloudml.indicators.diversity;

import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicatorTest;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SensApp;
import org.junit.Test;

/**
 * Test the calculation of diversity
 */
public class DiversityCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testOrderingDiversity() throws FileNotFoundException, IOException {
        DiversityCalculator diversity = makeDiversityCalculator();
        Deployment homogeneousDeployment = makeHomogeneousDeployment();
        double lowDiversity = diversity.evaluateOn(homogeneousDeployment);
        Deployment diverseDeployment = makeDiverseDeployment();
        double highDiversity = diversity.evaluateOn(diverseDeployment);
        assertTrue(
                "Robust model shall have greater or equal robustness than fragile ones",
                lowDiversity <= highDiversity);
    }

    protected DiversityCalculator makeDiversityCalculator() {
        return new DiversityCalculator(new TrueDiversity());
    }

    private Deployment makeHomogeneousDeployment() throws FileNotFoundException, IOException {
        return SensApp.completeSensApp().build();
    }

    private Deployment makeDiverseDeployment() throws FileNotFoundException, IOException {
        return makeHomogeneousDeployment();
    }

    @Override
    protected DeploymentIndicator makeDeploymentIndicator() {
        return makeDiversityCalculator();
    }

}