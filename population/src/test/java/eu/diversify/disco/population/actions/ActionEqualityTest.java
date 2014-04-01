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
package eu.diversify.disco.population.actions;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(JUnit4.class)
public class ActionEqualityTest {

    @Test
    public void testRemoveSpecieEqualityWhenEquals() {
        RemoveSpecie r1 = new RemoveSpecie("s1");
        RemoveSpecie r2 = new RemoveSpecie("s1");

        assertThat("reflexivity", r1, is(equalTo(r1)));
        assertThat("equality", r1, is(equalTo(r2)));
    }

    @Test
    public void testRemoveSpecieEqualityWhenNotEqual() {
        RemoveSpecie r1 = new RemoveSpecie("s1");
        RemoveSpecie r2 = new RemoveSpecie("s2");
        RemoveSpecie r3 = new RemoveSpecie(1);

        assertThat("equality", r1, is(not(equalTo(r2))));
        assertThat("equality", r1, is(not(equalTo(r3))));
    }
    
    @Test
    public void testAddSpecieEqualityWhenEqual() {
        AddSpecie a1 = new AddSpecie("s1");
        AddSpecie a2 = new AddSpecie("s1");
        
        assertThat("reflexivity", a1, is(equalTo(a1)));
        assertThat("equality", a1, is(equalTo(a2)));
    }
    
    @Test
    public void testAddSpecieEqualityWhenNotEqual() {
        AddSpecie a1 = new AddSpecie("s1");
        AddSpecie a2 = new AddSpecie("s2");
        
        assertThat("equality", a1, is(not(equalTo(a2))));
    }
    
    @Test
    public void testSetNumberOfIndividualsInEqualityWhenEquals() {
        SetNumberOfIndividualsIn a1 = new SetNumberOfIndividualsIn("s1", 3);
        SetNumberOfIndividualsIn a2 = new SetNumberOfIndividualsIn("s1", 3);
        
        assertThat("reflexivity", a1, is(equalTo(a1)));
        assertThat("equality", a1, is(equalTo(a2)));
    }
    
    @Test
    public void testSetNumberOfIndividualsInEqualityWhenNotEquals() {
        SetNumberOfIndividualsIn a1 = new SetNumberOfIndividualsIn("s1", 3);
        SetNumberOfIndividualsIn a2 = new SetNumberOfIndividualsIn("s1", 4);
        SetNumberOfIndividualsIn a3 = new SetNumberOfIndividualsIn("s2", 3);
        SetNumberOfIndividualsIn a4 = new SetNumberOfIndividualsIn("s3", 4);
        
        assertThat("inequality", a1, is(not(equalTo(a2))));
        assertThat("inequality", a1, is(not(equalTo(a3))));
        assertThat("inequality", a1, is(not(equalTo(a4))));
    }
    
     @Test
    public void testShiftNumberOfIndividualsInEqualityWhenEquals() {
        ShiftNumberOfIndividualsIn a1 = new ShiftNumberOfIndividualsIn("s1", 3);
        ShiftNumberOfIndividualsIn a2 = new ShiftNumberOfIndividualsIn("s1", 3);
        
        assertThat("reflexivity", a1, is(equalTo(a1)));
        assertThat("equality", a1, is(equalTo(a2)));
    }
    
    @Test
    public void testShiftNumberOfIndividualsInEqualityWhenNotEquals() {
        ShiftNumberOfIndividualsIn a1 = new ShiftNumberOfIndividualsIn("s1", 3);
        ShiftNumberOfIndividualsIn a2 = new ShiftNumberOfIndividualsIn("s1", 4);
        ShiftNumberOfIndividualsIn a3 = new ShiftNumberOfIndividualsIn("s2", 3);
        ShiftNumberOfIndividualsIn a4 = new ShiftNumberOfIndividualsIn("s3", 4);
        
        assertThat("inequality", a1, is(not(equalTo(a2))));
        assertThat("inequality", a1, is(not(equalTo(a3))));
        assertThat("inequality", a1, is(not(equalTo(a4))));
    }
    
    
    @Test
    public void testScriptEquality() {
        Plan a1 = new Plan(Arrays.asList(new Action[]{
            new AddSpecie("s1"),
            new ShiftNumberOfIndividualsIn("s1", +3),
            new RemoveSpecie("s2")
        }));
        
        Plan a2 = new Plan(Arrays.asList(new Action[]{
            new AddSpecie("s1"),
            new ShiftNumberOfIndividualsIn("s1", +3),
            new RemoveSpecie("s2")
        }));
        
        assertThat("equality", a1, is(equalTo(a1)));
        assertThat("equality", a1, is(equalTo(a2)));
    }

    @Test
    public void testScriptInEquality() {
        Plan a1 = new Plan(Arrays.asList(new Action[]{
            new AddSpecie("s1"),
            new ShiftNumberOfIndividualsIn("s1", +3),
            new RemoveSpecie("s2")
        }));
        
        Plan a2 = new Plan(Arrays.asList(new Action[]{
            new AddSpecie("s1"),
            new RemoveSpecie("s2")
        }));
        
        assertThat("equality", a1, is(not(equalTo(a2))));
    }
    
    
}