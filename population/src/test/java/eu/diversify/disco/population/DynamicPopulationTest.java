/*
 */
package eu.diversify.disco.population;

import java.util.Arrays;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class DynamicPopulationTest extends TestCase {
    public static final String SPECIE_LION = "Lion";
    public static final String SPECIE_HIPPOPOTAMUS = "Hippopotamus";

    @Test
    public void testNewIndividual() {
        DynamicPopulation p = makePopulation();

        Individual individual = new Animal(SPECIE_LION);
        individual.join(p);

        assertEquals(
                "Wrong number of individuals",
                p.getTotalNumberOfIndividuals(),
                3);

        assertEquals(
                "Wrong number of species",
                p.getNumberOfSpecies(),
                2);

        assertEquals(
                "Wrong number of individual in the specie 'Hippopotamus'",
                1,
                p.getNumberOfIndividualsIn(SPECIE_HIPPOPOTAMUS));

        assertEquals(
                "Wrong number of individual in the specie 'Hippopotamus'",
                2,
                p.getNumberOfIndividualsIn(SPECIE_LION));

    }
    
    @Test
    public void testIndividualDeath() {
        DynamicPopulation population = makePopulation();
        
        Individual individual = new Animal(SPECIE_LION);
        
        final int ni = population.getTotalNumberOfIndividuals();
        final int ns = population.getNumberOfSpecies();
        final int nLions = population.getNumberOfIndividualsIn(SPECIE_LION);
      
        individual.join(population);
        individual.leave(population);
        
        assertEquals(
                "Wrong number of individuals",
                ni,
                population.getTotalNumberOfIndividuals());
        
        assertEquals(
                "Wrong number of species",
                ns, 
                population.getNumberOfSpecies());
        assertEquals(
                "Wrong number of lions",
                nLions,
                population.getNumberOfIndividualsIn(SPECIE_LION));
    }

    @Test
    public void testIndividualNewSpecie() {
        DynamicPopulation p = makePopulation();
        
        Individual i = new Animal(SPECIE_HIPPOPOTAMUS);
        i.join(p);
                
        assertEquals(
                "Wrong number of species",
                p.getNumberOfSpecies(),
                2);

        i.addSpecie("Crocrodile");

        assertEquals(
                "Wrong number of individuals",
                p.getNumberOfSpecies(),
                3);
    }
    
    
    @Test
    public void testIndividualSpecieExtinction() {
        DynamicPopulation p = makePopulation();
        
        Individual i = new Animal(SPECIE_HIPPOPOTAMUS);
        i.join(p);
                
        assertEquals(
                "Wrong number of species",
                p.getNumberOfSpecies(),
                2);

        i.setSpecies(Arrays.asList(new String[]{SPECIE_LION}));

        assertEquals(
                "Wrong number of individuals",
                p.getNumberOfSpecies(),
                2);
    }
    

    
    private DynamicPopulation makePopulation() {
        DynamicPopulation result = new DynamicPopulation();
        Individual i1 = new Animal(SPECIE_HIPPOPOTAMUS);
        i1.join(result);
        Individual i2 = new Animal(SPECIE_LION);
        i2.join(result);
        return result;
    }
    
//public void testPopulationReaction
    
    // TODO test that adding twice an observer does not screw the configuration of population
    // TODO detection of unknown species
}