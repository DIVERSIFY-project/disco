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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.cloudml.robustness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cloudml.core.Deployment;

/**
 * Calculate the robustness of the given deployment model using an extinction
 * sequence.
 */
public class Robustness {

    private final int runCount;
    private final Simulator deployment;
    private ExtinctionSequence sequence;

    public Robustness(Deployment deployment) {
        this(deployment, DEFAULT_RUN_COUNT);
    }
    private static final int DEFAULT_RUN_COUNT = 1;

    public Robustness(Deployment deployment, int runCount) {
        this.runCount = runCount;
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
        final Map<Integer, List<Integer>> survivorCounts = new HashMap<Integer, List<Integer>>();

        for (int run = 0; run < runCount; run++) {
            deployment.markAllAsAlive();

            int survivorCount = deployment.countAliveComponents();
            append(survivorCounts, 0, survivorCount);

            for (int deathLevel = 1; deathLevel <= deployment.countComponents(); deathLevel++) {
                deployment.killOneComponent();
                survivorCount = deployment.countAliveComponents();
                append(survivorCounts, deathLevel, survivorCount);
            }
        }
        
        sequence = ExtinctionSequence.fromMap(survivorCounts);
    }

    private void append(Map<Integer, List<Integer>> data, int deadCount, int survivorCount) {
        List<Integer> survivorCounts = data.get(deadCount);
        if (survivorCounts == null) {
            survivorCounts = new ArrayList<Integer>();
            data.put(deadCount, survivorCounts);
        }
        survivorCounts.add(survivorCount);
    }

}
