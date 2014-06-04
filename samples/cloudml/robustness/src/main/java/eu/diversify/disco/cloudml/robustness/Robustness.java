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

import java.util.HashMap;
import java.util.Map;
import org.cloudml.core.Deployment;

/**
 * Calculate the robustness of the given deployment model using an extinction
 * sequence.
 */
public class Robustness {

    private final Simulator deployment;
    private ExtinctionSequence sequence;

    public Robustness(Deployment deployment) {
        this.deployment = new Simulator(deployment);
        this.sequence = null;
    }

    public double value() {
        return getExtinctionSequence().getRobustness();
    }
    
    public ExtinctionSequence getExtinctionSequence() {
        if (sequence == null) {
            computeRobustness();
        }
        return sequence;
    }

    private void computeRobustness() {
        final Map<Integer, Integer> lifeLevels = new HashMap<Integer, Integer>();

        deployment.markAllAsAlive();

        int lifeLevel = deployment.countAliveComponents();
        lifeLevels.put(0, lifeLevel);

        for (int deathLevel = 1; deathLevel <= deployment.countComponents(); deathLevel++) {
            deployment.killOneComponent();
            lifeLevel = deployment.countAliveComponents();
            lifeLevels.put(deathLevel, lifeLevel);
        }

        sequence = ExtinctionSequence.fromMap(lifeLevels);
    }

}
