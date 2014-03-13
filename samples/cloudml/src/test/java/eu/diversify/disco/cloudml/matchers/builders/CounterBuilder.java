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
package eu.diversify.disco.cloudml.matchers.builders;

import eu.diversify.disco.cloudml.matchers.HasNArtefactInstanceOfType;
import eu.diversify.disco.cloudml.matchers.HasNBindingOfType;
import eu.diversify.disco.cloudml.matchers.HasNNodeInstanceOfType;


public class CounterBuilder {

    private int count;
    private String name;
    
    public CounterBuilder withCount(int count) {
        this.count = count;
        return this;
    }
    
    public HasNArtefactInstanceOfType artefactsOfType(String name) {
        this.name = name;
        return new HasNArtefactInstanceOfType(count, name);
    }
    
    public HasNNodeInstanceOfType nodesOfType(String name) {
        this.name = name;
        return new HasNNodeInstanceOfType(count, name);
    }    
    
     public HasNBindingOfType bindingsOfType(String name) {
        this.name = name;
        return new HasNBindingOfType(count, name);
    }    
}
