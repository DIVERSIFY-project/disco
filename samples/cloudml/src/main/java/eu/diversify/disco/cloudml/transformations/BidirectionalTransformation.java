/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.cloudml.transformations;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NamedElement;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;

/**
 * Convert back and forth between CloudML models and Population models.
 * 
 * @author Hui Song
 * @author Franck Chauvel
 * 
 * @since 0.1
 */
public class BidirectionalTransformation {

    Random random = new Random();

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

    public NodeInstance provision(Node type, String name) {
        NodeInstance ni = type.instanciates(name);

        return ni;
    }

    public ArtefactInstance provision(Artefact type, String name) {
        ArtefactInstance ai = type.instanciates(name);
        for (ServerPort sp : type.getProvided()) {
            ai.getProvided().add(new ServerPortInstance(sp.getName(), sp, ai));
        }

        for (ClientPort cp : type.getRequired()) {
            ai.getRequired().add(new ClientPortInstance(cp.getName(), cp, ai));
        }

        return ai;
    }

    public void fixAllDestination(DeploymentModel dm) {
        Multimap<ArtefactInstance, ArtefactInstance> connected = HashMultimap.create();
        for (BindingInstance bi : dm.getBindingInstances()) {
            if (bi.getType().getClient().getIsRemote() && bi.getType().getServer().getIsRemote()) {
                continue;
            }
            connected.put(bi.getServer().getOwner(), bi.getClient().getOwner());
            connected.put(bi.getClient().getOwner(), bi.getServer().getOwner());
        }

        while (true) {
            for (ArtefactInstance ai : dm.getArtefactInstances()) {
                if (ai.getDestination() != null) {
                    continue;
                }
                for (ArtefactInstance c : connected.get(ai)) {
                    if (c.getDestination() != null) {
                        ai.setDestination(c.getDestination());
                        break;
                    }
                }
            }

            boolean finished = true;
            for (ArtefactInstance ai : dm.getArtefactInstances()) {
                if (ai.getDestination() == null) {
                    NodeInstance ni = dm.getNodeInstances().get(random.nextInt(dm.getNodeInstances().size()));
                    ai.setDestination(ni);
                    finished = false;
                    break;
                }
            }
            if (finished) {
                break;
            }
        }
    }

    public Collection<Binding> fixBinding(DeploymentModel dm, ArtefactInstance ai) {

        List<Binding> lacks = new ArrayList<Binding>();

        for (final ClientPortInstance cpi : ai.getRequired()) {

            Collection<BindingInstance> bound = filter(dm.getBindingInstances(), new Predicate<BindingInstance>() {
                public boolean apply(BindingInstance t) {
                    return t.getClient().equals(cpi);
                }
            });

            if (!bound.isEmpty()) {
                continue;
            }

            Collection<Binding> potential = filter(dm.getBindingTypes().values(), new Predicate<Binding>() {
                public boolean apply(Binding t) {
                    //need to check: how to compare two port types?
                    return t.getClient().getName().equals(cpi.getType().getName());
                    //return t.getClient().equals(cpi.getType());
                }
            });


            if (potential.isEmpty()) {
                throw new RuntimeException("No binding type found");
            }

            BindingInstance bdi = null;
            for (Binding bd : potential) {

                for (ArtefactInstance serverAi : dm.getArtefactInstances()) {
                    for (ServerPortInstance spi : serverAi.getProvided()) {
                        if (spi.getType().getName().equals(bd.getServer().getName())) {
                            bdi = new BindingInstance(cpi, spi, bd);
                            break;
                        }
                    }
                    if (bdi != null) {
                        break;
                    }
                }
                if (bdi != null) {
                    dm.getBindingInstances().add(bdi);
                    break;
                }
            }
            if (bdi == null) {
                //Some client cannot be satisfied
                lacks.addAll(potential);
            }

        }

        return lacks;
    }

    public <T extends NamedElement> String uniqueId(List<T> pool, String prefix) {
        Collection<String> names = transform(pool, new Function<T, String>() {
            public String apply(T f) {
                return f.getName();
            }
        });

        String name = prefix + random.nextInt(1000);
        while (names.contains(name)) {
            name = prefix += random.nextInt(1000);
        }
        return name;
    }

 
}
