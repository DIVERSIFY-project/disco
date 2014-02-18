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

package eu.diversify.disco.population.random;

import java.util.Random;

/**
 * Simple class to capture ranges of values, such as [min, max[.
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class Range {
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 100;
    
    private int min;
    private int max;

    
    public Range() {
        this.min = DEFAULT_MIN;
        this.max = DEFAULT_MAX;
    }
    
    /**
     * Build a new closed range of values.
     *
     * @param min the lower bound of the range
     * @param max the upper bound of the range
     */
    public Range(int min, int max) {
        if (max - min < 1) {
            throw new IllegalArgumentException("The range must contain at least one element");
        }
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if (max - min < 1) {
            throw new IllegalArgumentException("The range must contain at least one element");
        }
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max - min < 1) {
            throw new IllegalArgumentException("The range must contain at least one element");
        }
        this.max = max;
    }

    public void reset(Range range) {
        if (max - min < 1) {
            throw new IllegalArgumentException("The range must contain at least one element");
        }
        this.min = range.getMin();
        this.max = range.getMax();        
    }
    
    public void reset(int min, int max) {
        if (max - min < 1) {
            throw new IllegalArgumentException("The range must contain at least one element");
        }
        this.min = min;
        this.max = max;
    }

    
    /**
     * Check whether the given value lies in this range
     * @param value the value to be tested
     * @return true if the value lies in this range
     */
    public boolean contains(int value) {
        return value >= min && value < max;
    }

    
    /**
     * Sample a value uniformly distributed over this range
     * @return the sampled value
     */
    public int sample() {
        Random random = new Random();
        return min + random.nextInt(max - min);
    }

}
