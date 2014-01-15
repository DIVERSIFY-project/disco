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


package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.Evaluation;
import eu.diversify.disco.controller.HillClimber;
import eu.diversify.disco.population.diversity.TrueDiversity;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.cloudml.core.Provider;
       
        

/**
 * Hello world!
 *
 */
public class Runner 
{
    public static void main( String[] args )
    {
        CloudML model = null;
        Transformation transformation = new Transformation();
        Population population = transformation.forward(model);
        
        Controller controller = new HillClimber(new TrueDiversity());
        
        final double reference = 0.25; // The diversity level we aim at
        Evaluation result = controller.applyTo(population, reference);
        
        transformation.backward(model, result.getPopulation());
    }
    
    public static void initFakeModel(CloudML model){
        model.init();
        DeploymentModel dm = model.getRoot();
        
        Provider hugeProvider = new Provider("huge", "../src/main/resources/credentials");
        Provider bigsmallProvider = new Provider("bigsmall", "../src/main/resources/credentials");
        dm.getProviders().add(hugeProvider);
        dm.getProviders().add(bigsmallProvider);
        
        Node huge = new Node("huge");
        huge.setProvider(hugeProvider);
        Node big = new Node("big");
        big.setProvider(bigsmallProvider);
        Node small = new Node("small");
        small.setProvider(bigsmallProvider);
        
        dm.getNodeTypes().put("huge", huge);
        dm.getNodeTypes().put("big", big);
        dm.getNodeTypes().put("small", small);
        
        for(int i = 0; i < 5; i++)
            dm.getNodeInstances().add(huge.instanciates("huge"+i));
        
        for(int i = 0; i < 10; i++)
            dm.getNodeInstances().add(big.instanciates("big"+i));
        
        for(int i = 0; i< 10; i++)
            dm.getNodeInstances().add(small.instanciates("small"+i));
        
        for(NodeInstance ni : dm.getNodeInstances()){
            ni.getProperties().add(new Property("state","on"));
        }
    }
}
