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

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.constraints.Constraint;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Attach a set of constraint to the given population, so as to limit the
 * actions one can perform on the population.
 */
public class ConstrainedPopulation extends AbstractPopulationDecorator {

    private final ArrayList<Constraint> constraints;

    public ConstrainedPopulation(Population delegate, Collection<Constraint> constraints) {
        super(delegate);
        this.constraints = new ArrayList<Constraint>(constraints);
    }
    
    public boolean allows(Action action) {
        boolean supported = true;
        for (Constraint c: constraints) {
            supported &= c.allows(action);
        }
        return supported;
    }
            
}
