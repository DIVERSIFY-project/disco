/*
 */
package eu.diversify.disco.cloudml.robustness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class SurvivorCounts implements Comparable<SurvivorCounts> {

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

    public int getSurvivorCount() {
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
        for (int i=1 ; i< survivorCounts.size() ; i++) {
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

    @Override
    public int compareTo(SurvivorCounts o) {
        return deadCount - o.getDeadCount();
    }

    @Override
    public String toString() {
        return "{" + "killed: " + deadCount + ", alive:" + getSurvivorCount() + '}';
    }

}
