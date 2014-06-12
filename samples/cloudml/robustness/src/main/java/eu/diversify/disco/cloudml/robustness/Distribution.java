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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Provide statistics on a collection of real numbers
 */
public class Distribution {
    
    private final List<Double> samples;
    
    public Distribution(Double... samples) {
        this(Arrays.asList(samples));
    }
    
    public Distribution(Collection<Double> samples) {
        this.samples = new ArrayList<Double>(samples.size());
        for (Double eachSample: samples) {
            this.samples.add(eachSample);
        }
    }
        
    public double minimum() {
        return Collections.min(samples);
    }
    
    public double maximum() {
        return Collections.max(samples);
    }
    
    public double mean() {
        double sum = 0;
        for(double eachSample: samples) {
            sum += eachSample;
            
        }
        return sum / samples.size();
    }
    
    public double variance() {
        final double mean = mean();
        double sum = 0;
        for(double eachSample: samples) {
            sum += Math.pow(eachSample-mean, 2);
            
        }
        return sum / samples.size();
    }
    
    public double standardDeviation() {
        return Math.sqrt(variance());
    }
    
    @Override
    public String toString() {
        return String.format("%.2f in [%.2f, %.2f] +/- %.2f", mean(), minimum(), maximum(), standardDeviation());
    }

}
