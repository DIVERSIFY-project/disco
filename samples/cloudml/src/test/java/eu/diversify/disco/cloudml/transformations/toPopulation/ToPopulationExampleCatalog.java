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

package eu.diversify.disco.cloudml.transformations.toPopulation;

import eu.diversify.disco.cloudml.transformations.MdmsModelCreator;
import eu.diversify.disco.population.Population;
import static eu.diversify.disco.population.PopulationBuilder.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.cloudml.core.Provider;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ToPopulationExampleCatalog {

    
    private final HashMap<String, ToPopulationExample> examples;
    
    public ToPopulationExampleCatalog() {
        this.examples = new HashMap<String, ToPopulationExample>();
        prepareCatalog();
    }

    private void prepareCatalog() {
        add("SensApp",
            cloudmlWithSensApp(),
            populationWithSensApp());

        add("MDMS",
            cloudmlWithMdms(),
            populationWithMdms());

        add("Hui's fake model",
            cloudmlWithHuisFakeModel(),
            populationWithHuisFakeModel());
    }

    private void add(String name, DeploymentModel input, Population output) {
        this.examples.put(name, new ToPopulationExample(name, input, output));
    }

    public Collection<String> getExampleNames() {
        return this.examples.keySet();
    }

    public ToPopulationExample get(String key) {
        if (!examples.containsKey(key)) {
            throw new IllegalArgumentException("Unknown example '" + key + "'");
        }
        return this.examples.get(key);
    }



    private static DeploymentModel cloudmlWithSensApp() {
        JsonCodec jsonCodec = new JsonCodec();
        DeploymentModel root = null;
        try {
            root = (DeploymentModel) jsonCodec.load(new FileInputStream(
                    "../src/main/resources/sensappAdmin.json"));
            for (NodeInstance ni : root.getNodeInstances()) {
                ni.getProperties().add(new Property("state", "onn"));
            }
            for (ArtefactInstance ai : root.getArtefactInstances()) {
                ai.getProperties().add(new Property("state", "onn"));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ToPopulationExample.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }
        return root;
    }

    private static Population populationWithSensApp() {
        return aPopulation()
                .withSpeciesNamed("cloudmlsensappgui", "cloudmlsensapp",
                                  "SensAppGUIWar", "MongoDB",
                                  "jettyWarContainer", "SensAppWar")
                .withDistribution(1, 1, 1, 1, 2, 1)
                .build();
    }

    private DeploymentModel cloudmlWithMdms() {
        return new MdmsModelCreator().create();
    }

    private Population populationWithMdms() {
        return aPopulation()
                .withSpeciesNamed("EC2", "MySQL", "Balancer", "RingoJS",
                                  "OpenJDK", "Rhino", "MDMS")
                .withDistribution(3, 1, 1, 1, 1, 1, 1)
                .build();
    }

    private DeploymentModel cloudmlWithHuisFakeModel() {
        DeploymentModel dm = new DeploymentModel();
        Provider hugeProvider = new Provider("huge",
                                             "../src/main/resources/credentials");
        Provider bigsmallProvider = new Provider("bigsmall",
                                                 "../src/main/resources/credentials");
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

        for (int i = 0; i < 5; i++) {
            dm.getNodeInstances().add(huge.instanciates("huge" + i));
        }

        for (int i = 0; i < 10; i++) {
            dm.getNodeInstances().add(big.instanciates("big" + i));
        }

        for (int i = 0; i < 10; i++) {
            dm.getNodeInstances().add(small.instanciates("small" + i));
        }

        for (NodeInstance ni : dm.getNodeInstances()) {
            ni.getProperties().add(new Property("state", "on"));
        }

        return dm;
    }

    private Population populationWithHuisFakeModel() {
        return aPopulation()
                .withSpeciesNamed("huge", "big", "small")
                .withDistribution(5, 10, 10)
                .build();
    }
}
