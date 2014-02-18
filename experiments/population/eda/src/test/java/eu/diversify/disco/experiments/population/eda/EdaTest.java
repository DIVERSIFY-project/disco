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
package eu.diversify.disco.experiments.population.eda;

import eu.diversify.disco.experiments.population.eda.EdaSetup;
import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.data.DataSet;
import eu.diversify.disco.population.random.Profile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yaml.snakeyaml.Yaml;

/**
 * Test the behaviour of the Random Generation experiment
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class EdaTest extends TestCase {

    public static final int SAMPLE_COUNT = 100;
    public static final int MIN_INDIVIDUAL_COUNT = 50;
    public static final int MAX_INDIVIDUAL_COUNT = 50000;
    public static final int MIN_SPECIE_COUNT = 10;
    public static final int MAX_SPECIE_COUNT = 100;

    @Test
    public void simpleRun() {
        EdaSetup setup = new EdaSetup();
        Profile profile = new Profile();
        profile.setIndividualsCount(MIN_INDIVIDUAL_COUNT, MAX_INDIVIDUAL_COUNT);
        profile.setSpeciesCount(MIN_SPECIE_COUNT, MAX_SPECIE_COUNT);
        setup.setPopulation(profile);
        setup.setSampleCount(SAMPLE_COUNT);

        Experiment experiment = setup.buildExperiment();
        List<DataSet> results = experiment.run();

        assertEquals("Wrong number of datasets", 1, results.size());
        assertEquals("Wrong number of data points", SAMPLE_COUNT, results.get(0).getSize());
    }

    @Test
    public void testLoadingSetupFromYamlFile() throws FileNotFoundException {
        final String fileToLoad = "../src/test/resources/setup.yml";
        final Yaml yaml = new Yaml();
        EdaSetup setup = (EdaSetup) yaml.loadAs(new FileInputStream(fileToLoad), EdaSetup.class);

        assertEquals("Wrong sample count", SAMPLE_COUNT, setup.getSampleCount());
        assertEquals(
                "Wrong min number of individuals",
                MIN_INDIVIDUAL_COUNT,
                setup.getPopulation().getNumberOfIndividuals().getMin());
        assertEquals(
                "Wrong max number of individuals",
                MAX_INDIVIDUAL_COUNT,
                setup.getPopulation().getNumberOfIndividuals().getMax());
        assertEquals(
                "Wrong min number of species",
                MIN_SPECIE_COUNT,
                setup.getPopulation().getNumberOfSpecies().getMin());
        assertEquals(
                "Wrong max number of species",
                MAX_SPECIE_COUNT,
                setup.getPopulation().getNumberOfSpecies().getMax());

    }
}
