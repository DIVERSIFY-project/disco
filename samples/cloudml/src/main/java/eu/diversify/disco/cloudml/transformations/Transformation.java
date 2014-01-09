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

import eu.diversify.disco.cloudml.CloudML;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.Specie;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NodeInstance;

/**
 *
 * @author Hui Song
 * @since 0.1
 */
public class Transformation {


    public Population forward(CloudML model) {
        Population population = new Population();
        DeploymentModel dm = model.getRoot();
        
        for(String name : dm.getNodeTypes().keySet()){
            population.addSpecie(name);
        }
        
        for(String name : dm.getArtefactTypes().keySet()){
            population.addSpecie(name);
        }
        
        for(NodeInstance ni : dm.getNodeInstances())
            addOne(population, ni.getType().getName());
        
        
        for(ArtefactInstance ai: dm.getArtefactInstances())
            addOne(population, ai.getType().getName());
        
        return population;
        
    }
    
    public void addOne(Population population, String specieName){
        Specie specie = population.getSpecie(specieName);
        specie.setIndividualCount(specie.getIndividualCount()+1);
    }

    public void backward(CloudML model, Population toBe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
