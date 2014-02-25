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
/*
 */
package eu.diversify.disco.population.decorators;

import eu.diversify.disco.population.PopulationBuilder;
import eu.diversify.disco.population.PopulationTest;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * TODO: To refactor and integrate with the new population test
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class DynamicPopulationTest extends PopulationTest {
    public static final String SPECIE_LION = "Lion";
    public static final String SPECIE_HIPPOPOTAMUS = "Hippopotamus";

      @Override
    public void testShiftNumberOfIndividualsInByIndex() {
        super.testShiftNumberOfIndividualsInByIndex();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testShiftNumberOfIndividualsInBySpecieName() {
        super.testShiftNumberOfIndividualsInBySpecieName();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testAddSpecie() {
        super.testAddSpecie();
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByIndex() {
        super.testRemoveSpecieByIndex(); //To change body of generated methods, choose Tools | Templates.
        assertSame(getActual(), getInitial());
    }

    @Override
    public void testRemoveSpecieByName() {
        super.testRemoveSpecieByName(); //To change body of generated methods, choose Tools | Templates.
        assertSame(getActual(), getInitial());
    }
    
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
        
        final int ni = population.getTotalNumberOfIndividuals();
        final int ns = population.getNumberOfSpecies();
        final int nLions = population.getNumberOfIndividualsIn(SPECIE_LION);

        Individual individual = new Animal(SPECIE_LION);
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
        DynamicPopulation result = (DynamicPopulation) new PopulationBuilder().dynamic().make();
        Individual i1 = new Animal(SPECIE_HIPPOPOTAMUS);
        i1.join(result);
        Individual i2 = new Animal(SPECIE_LION);
        i2.join(result);
        return result;
    }

    @Override
    public PopulationBuilder getBuilder() {
        return new PopulationBuilder().dynamic();
    }
    
}