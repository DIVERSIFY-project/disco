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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertTrue;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;
import org.junit.Test;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class DiversityCalculatorTest extends DeploymentIndicatorTest {

    @Test
    public void testOrderingDiversity() throws FileNotFoundException, IOException {
        DiversityCalculator diversity = makeDiversityCalculator();
        DeploymentModel homogeneousDeployment = makeHomogeneousDeployment();
        double lowDiversity = diversity.evaluateOn(homogeneousDeployment);
        DeploymentModel diverseDeployment = makeDiverseDeployment();
        double highDiversity = diversity.evaluateOn(diverseDeployment);
        assertTrue(
                "Robust model shall have greater or equal robustness than fragile ones",
                lowDiversity <= highDiversity);
    }

    protected DiversityCalculator makeDiversityCalculator() {
        return new DiversityCalculator(new TrueDiversity());
    }

    private DeploymentModel makeHomogeneousDeployment() throws FileNotFoundException, IOException {
        JsonCodec codec = new JsonCodec();
        InputStream stream = new FileInputStream("../src/test/resources/sensappAdmin.json");
        DeploymentModel model = (DeploymentModel) codec.load(stream);
        stream.close();
        return model;
    }

    private DeploymentModel makeDiverseDeployment() throws FileNotFoundException, IOException {
        return makeHomogeneousDeployment();
    }

    @Override
    protected DeploymentIndicator makeDeploymentIndicator() {
        return makeDiversityCalculator();
    }

}