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
package eu.diversify.disco.cloudml.robustness;

/**
 * Represent an action performed on a population: killing on individual or
 * making them all alive
 */
public abstract class Action {

    public static Action kill(String victim) {
        return new Kill(victim);
    }

    public static Action reviveAll() {
        return ReviveAll.getInstance();
    }

    private final int killedCount;

    public Action(int killedCount) {
        this.killedCount = killedCount;
    }
    
    public void applyTo(Simulation simulation) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int killedCount() {
        return killedCount;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }
}
