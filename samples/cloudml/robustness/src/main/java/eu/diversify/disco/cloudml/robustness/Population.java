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

import java.util.List;

/**
 * Capture the notion of populations where individuals can be killed and revive
 */
public abstract class Population {

    public abstract void kill(String victim);

    public abstract void reviveAll();

    public abstract List<String> getIndividualNames();
    
    public final int headcount() {
        return getIndividualNames().size();
    }

    public abstract List<String> getSurvivorNames();

    public final int survivorCount() {
        return getSurvivorNames().size();
    }

}
