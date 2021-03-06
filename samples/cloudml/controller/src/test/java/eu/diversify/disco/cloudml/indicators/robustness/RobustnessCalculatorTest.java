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

package eu.diversify.disco.cloudml.indicators.robustness;

import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicatorTest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SensApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Generic test suite for robustness calculations
 */
@RunWith(JUnit4.class)
public abstract class RobustnessCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testOrderingRobustness() throws FileNotFoundException, IOException {
        RobustnessCalculator robustness = makeRobustnessCalculator();
        Deployment fragileDeployment = makeFragileDeployment();
        double r1 = robustness.evaluateOn(fragileDeployment);
        Deployment robustDeployment = makeRobustDeployment();
        double r2 = robustness.evaluateOn(robustDeployment);
        assertTrue(
                "Robust model shall have greater or equal robustness than fragile ones",
                r1 <= r2);
    }

    protected abstract RobustnessCalculator makeRobustnessCalculator();

    private Deployment makeFragileDeployment() throws FileNotFoundException, IOException {
        return SensApp.completeSensApp().build();
    }

    private Deployment makeRobustDeployment() throws FileNotFoundException, IOException {
        return makeFragileDeployment();
    }

    @Override
    protected DeploymentIndicator makeDeploymentIndicator() {
        return new DummyRobustnessCalculator();
    }
}