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

package eu.diversify.disco.cloudml.transformations;

import eu.diversify.disco.population.Population;
import org.cloudml.core.DeploymentModel;

/**
 * Convert back and forth between CloudML models and Population models.
 * 
 * @author Hui Song
 * @author Franck Chauvel
 * 
 * @since 0.1
 */
public class BidirectionalTransformation {

    private final ToPopulation toPopulation;
    private final ToCloudML toCloudML;
 
    public BidirectionalTransformation() {
        this.toPopulation = new ToPopulation();
        this.toCloudML = new ToCloudML();
    }
    
    public Population toPopulation(DeploymentModel model) {
       return toPopulation.applyTo(model);
    }
    
    public void toCloudML(DeploymentModel deployment, Population toBe) {
        toCloudML.applyTo(deployment, toBe);
    }
 
}
