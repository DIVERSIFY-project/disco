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
import org.cloudml.core.Deployment;
import org.cloudml.indicators.Robustness;
import org.cloudml.indicators.Selection;

/**
 * General Interface of a robustness calculator for CloudML models
 */
public class RobustnessCalculator extends DeploymentIndicator {

    @Override
    protected double doEvaluation(Deployment deployment) {
        final Robustness robustness = Robustness.ofSelfRepairing(deployment, Selection.SERVICE, Selection.NOT_SERVICE);
        return robustness.value();
    }

    
    
}
