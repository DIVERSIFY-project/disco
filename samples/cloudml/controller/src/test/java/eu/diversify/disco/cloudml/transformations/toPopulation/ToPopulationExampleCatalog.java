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
package eu.diversify.disco.cloudml.transformations.toPopulation;

import eu.diversify.disco.cloudml.transformations.MdmsModelCreator;
import eu.diversify.disco.population.Population;

import static eu.diversify.disco.population.PopulationBuilder.*;

import java.util.Collection;
import java.util.HashMap;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.VMInstance;
import org.cloudml.core.Property;
import org.cloudml.core.samples.SensApp;

import static eu.diversify.disco.cloudml.transformations.MdmsModelCreator.*;

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

    }

    private void add(String name, Deployment input, Population output) {
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

    private static Deployment cloudmlWithSensApp() {
        final Deployment root = SensApp.completeSensApp().build();
        for (VMInstance ni: root.getComponentInstances().onlyVMs()) {
            ni.getProperties().add(new Property("state", "onn"));
        }
        for (InternalComponentInstance ai: root.getComponentInstances().onlyInternals()) {
            ai.getProperties().add(new Property("state", "onn"));
        }
        return root;
    }

    private static Population populationWithSensApp() {
        return aPopulation()
                .withSpeciesNamed("ML", "SL",
                                  SensApp.SENSAPP_ADMIN, SensApp.MONGO_DB,
                                  SensApp.JETTY, SensApp.SENSAPP)
                .withDistribution(1, 1, 1, 1, 2, 1)
                .build();
    }

    private Deployment cloudmlWithMdms() {
        return new MdmsModelCreator().create();
    }

    private Population populationWithMdms() {
        return aPopulation()
                .withSpeciesNamed(LARGE_LINUX, LOAD_BALANCER, JS_ENGINE, OPEN_JDK,
                                  REDIS, MDMS)
                .withDistribution(3, 1, 1, 1, 1, 1)
                .build();
    }

}
