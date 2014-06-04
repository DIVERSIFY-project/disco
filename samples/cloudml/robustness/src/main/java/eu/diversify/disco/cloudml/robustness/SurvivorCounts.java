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
/*
 */
package eu.diversify.disco.cloudml.robustness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Capture a distribution of survivor counts and provides some basic statistics
 * on it, such as min, max, and standard deviation.
 */
public class SurvivorCounts implements Comparable<SurvivorCounts> {

    private final int deadCount;
    private final List<Integer> survivorCounts;

    public SurvivorCounts(int extinctionLevel, int lifeLevel) {
        this(extinctionLevel, Collections.singletonList(lifeLevel));
    }

    public SurvivorCounts(int extinctionLevel, Integer... survivorCounts) {
        this(extinctionLevel, Arrays.asList(survivorCounts));
    }

    public SurvivorCounts(int deadCount, Collection<Integer> survivorCounts) {
        if (survivorCounts.isEmpty()) {
            final String error = String.format("Extinction sequences require at least 1 survivor count per dead count (none is given for dead count = %d)", deadCount);
            throw new IllegalArgumentException(error);
        }
        this.deadCount = validate(deadCount);
        this.survivorCounts = new ArrayList<Integer>();
        for (Integer each: survivorCounts) {
            this.survivorCounts.add(validate(each));
        }
    }

    private int validate(int count) {
        if (count < 0) {
            final String error = String.format("Counts must be positive (found: %d)", count);
            throw new IllegalArgumentException(error);
        }
        return count;

    }

    public int getDeadCount() {
        return deadCount;
    }

    public int getOneSurvivorCount() {
        return survivorCounts.get(0);
    }

    public double mean() {
        double sum = 0;
        for (Integer eachCount: survivorCounts) {
            sum += eachCount;
        }
        return sum / survivorCounts.size();
    }

    public int minimum() {
        int min = survivorCounts.get(0);
        for (int i = 1; i < survivorCounts.size(); i++) {
            if (min > survivorCounts.get(i)) {
                min = survivorCounts.get(i);
            }
        }
        return min;
    }

    public int maximum() {
        int max = survivorCounts.get(0);
        for (int i = 1; i < survivorCounts.size(); i++) {
            if (max < survivorCounts.get(i)) {
                max = survivorCounts.get(i);
            }
        }
        return max;
    }

    public double variance() {
        final double mean = mean();
        double sum = 0;
        for (int eachCount: survivorCounts) {
            sum += Math.pow(mean - eachCount, 2);
        }
        return sum / survivorCounts.size();
    }

    public double standardDeviation() {
        return Math.sqrt(variance());
    }

    public List<Integer> all() {
        return Collections.unmodifiableList(this.survivorCounts);
    }
    
    @Override
    public int compareTo(SurvivorCounts o) {
        return deadCount - o.getDeadCount();
    }

    @Override
    public String toString() {
        return "{" + "killed: " + deadCount + ", alive:" + getOneSurvivorCount() + '}';
    }

}
