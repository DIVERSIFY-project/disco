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
package eu.diversify.disco.controller.solvers.searches;

import eu.diversify.disco.controller.exploration.ExplorationStrategy;

public class AdaptiveHillClimbing extends HillClimbing {

    public static final int SPEED_FACTOR = 2;
    private static final int MINIMUM_DEPTH = 1;
    private int depth;

    public AdaptiveHillClimbing(ExplorationStrategy finder) {
        super(finder);
        depth = DEFAULT_SCALE_FACTOR;
    }

    @Override
    protected void findImprovement() {
        super.findImprovement();
        if (hasMoreImprovement()) {
            speedUp();
        }
        else {
            slowDownAndRetry();
        }
    }

    private void slowDownAndRetry() {
        if (canSlowDown()) {
            slowDown();
            findImprovement();
        }
    }

    @Override
    protected int getScaleFactor() {
        return depth;
    }

    private boolean canSlowDown() {
        return depth > MINIMUM_DEPTH;
    }

    private void slowDown() {
        depth /= SPEED_FACTOR;
    }

    private void speedUp() {
        depth *= SPEED_FACTOR;
    }
}
