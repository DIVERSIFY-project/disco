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

import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.List;

/**
 * Macro actions on population, an ordered sequence of actions to be applied on
 * a population.
 */
public class Plan implements Action {

    private final ArrayList<Action> actions;

    public Plan(List<Action> actions) {
        this.actions = new ArrayList<Action>(actions);
    }

    @Override
    public Population applyTo(Population subject) {
        Population current = subject;
        for (Action action : actions) {
            current = action.applyTo(current);
        }
        return current;
    }

    @Override
    public boolean preserveTheNumberOfSpecies() {
        return impactOnTheNumberOfSpecies() == 0;
    }

    @Override
    public boolean preserveTheTotalNumberOfIndividuals() {
       return impactOnTheNumberOfIndividuals() == 0;
    }

    private void requireValid(Population target, String specieName) throws IllegalArgumentException {
        if (target == null) {
            throw new IllegalArgumentException("Unable to ensure minimum headcount on 'null'");
        }
        if (!target.hasAnySpecieNamed(specieName)) {
            final String error = String.format(
                    "Unknown specie named '%s'. Existing species are %s.",
                    specieName, 
                    target.getSpeciesNames().toString());
            throw new IllegalArgumentException(error);
        }
    }

    @Override
    public int impactOnSpecie(String specieName, Population target) {
        int totalImpact = 0;
        for(Action eachAction: actions) {
            totalImpact += eachAction.impactOnSpecie(specieName, target);
        }
        return totalImpact;
    }

    
    @Override
    public int impactOnTheNumberOfSpecies() {
        int impact = 0;
        for (Action action: actions) {
            impact += action.impactOnTheNumberOfSpecies();
        }
        return impact;
    }

    @Override
    public int impactOnTheNumberOfIndividuals() {
        int impact = 0;
        for (Action action: actions) {
            impact += action.impactOnTheNumberOfIndividuals();
        }
        return impact;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.actions != null ? this.actions.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Plan other = (Plan) obj;
        if (this.actions != other.actions && (this.actions == null || !this.actions.equals(other.actions))) {
            return false;
        }
        return true;
    }
    
    
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Script {");
        for(Action action: actions) {
            builder.append(action);
            builder.append("; ");
        }
        builder.append("}");
        return builder.toString();
    }

}
