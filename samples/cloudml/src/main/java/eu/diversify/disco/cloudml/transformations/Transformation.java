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


import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import static com.google.common.collect.Collections2.filter;
import eu.diversify.disco.cloudml.CloudML;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.Specie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;


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

    public NodeInstance provision(Node type, String name){
        NodeInstance ni = type.instanciates(name);
        
        return ni;
    }
    
    public ArtefactInstance provision(Artefact type, String name){
        ArtefactInstance ai = type.instanciates(name);
        for(ServerPort sp : type.getProvided())
            ai.getProvided().add(new ServerPortInstance(sp.getName(), sp, ai));
        
        for(ClientPort cp : type.getRequired())
            ai.getRequired().add(new ClientPortInstance(cp.getName(), cp, ai));
        
        return ai;
    }
    
    public Collection<Binding> fixBinding(DeploymentModel dm, ArtefactInstance ai){
        
        for(final ClientPortInstance cpi : ai.getRequired()){
            
            Collection<BindingInstance> bound = filter(dm.getBindingInstances(), new Predicate<BindingInstance>(){
                public boolean apply(BindingInstance t) {
                    return t.getClient() == cpi;
                }                
            });
            
            if(!bound.isEmpty()){
                continue;
            }
            
            Collection<Binding> potential = filter(dm.getBindingTypes().values(), new Predicate<Binding>(){

                public boolean apply(Binding t) {
                    //need to check: how to compare two port types?
                    return t.getClient().getName().equals(cpi.getType().getName());
                }

            });
                
            
            if (potential.isEmpty()) {
                throw new RuntimeException("No binding type found");
            }
            
            BindingInstance bdi = null;
            for (Binding bd : potential) {
                
                for(ArtefactInstance serverAi : dm.getArtefactInstances()){
                    for(ServerPortInstance spi : serverAi.getProvided()){
                        if(spi.getType().getName().equals(bd.getServer().getName())){
                            bdi = new BindingInstance(cpi, spi, bd);
                        }
                    }
                }
                if(bdi != null){
                    dm.getBindingInstances().add(bdi);
                    break;
                }
            }
            if(bdi == null){
                //Some client cannot be satisfied
                return potential;
            }
            
        }
        
        return Collections.EMPTY_LIST;
    }

    
    
}
