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
package eu.diversify.disco.population.diversity;

import eu.diversify.disco.population.Population;

/**
 * A decorator that normalises the metric of interest
 */
public class NormalisedDiversityMetric implements DiversityMetric {

    private final AbstractDiversityMetric delegate;

    public NormalisedDiversityMetric(AbstractDiversityMetric delegate) {
        this.delegate = delegate;
    }

    @Override
    public double applyTo(Population population) {
        final double diversity = delegate.applyTo(population);
        final double min = delegate.minimum(population);
        final double max = delegate.maximum(population);
        return (diversity - min) / (max - min);
    }

    @Override
    public double minimum(Population population) {
        return 0D;
    }

    @Override
    public double maximum(Population population) {
        return 1D;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.delegate != null ? this.delegate.hashCode() : 0);
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
        final NormalisedDiversityMetric other = (NormalisedDiversityMetric) obj;
        if (this.delegate != other.delegate && (this.delegate == null || !this.delegate.equals(other.delegate))) {
            return false;
        }
        return true;
    }

    @Override
    public DiversityMetric normalise() {
        return this;
    }

    @Override
    public String getName() {
        return "normalised " + delegate.getName();
    }

    
    
}
