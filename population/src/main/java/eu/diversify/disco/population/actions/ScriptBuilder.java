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
package eu.diversify.disco.population.actions;

import java.util.ArrayList;

public class ScriptBuilder {

    public static ScriptBuilder aScript() {
        return new ScriptBuilder();
    }
    private final ArrayList<Action> actions;

    private ScriptBuilder() {
        this.actions = new ArrayList<Action>();
    }

    public ScriptBuilder shift(String specieName, int offset) {
        actions.add(new ShiftNumberOfIndividualsIn(specieName, offset));
        return this;
    }

    public ScriptBuilder shift(int specieIndex, int offset) {
        actions.add(new ShiftNumberOfIndividualsIn(specieIndex, offset));
        return this;
    }

    public ScriptBuilder addSpecie() {
        actions.add(new AddSpecie());
        return this;
    }

    public ScriptBuilder addSpecie(String specieName) {
        actions.add(new AddSpecie(specieName));
        return this;
    }

    public ScriptBuilder removeSpecie(String specieName) {
        actions.add(new RemoveSpecie(specieName));
        return this;
    }

    public Plan build() {
        return new Plan(actions);
    }
}
