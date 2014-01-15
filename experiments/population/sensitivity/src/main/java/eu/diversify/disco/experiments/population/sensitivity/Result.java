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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.diversify.disco.experiments.population.sensitivity;

/**
 * Hold one single result of the experiment
 */
public class Result {
    private final int specie1;
    private final int specie2;
    private final String metric;
    private final String type;
    private final double value;

    public Result(int specie1, int specie2, String metric, String type, double value) {
        this.specie1 = specie1;
        this.specie2 = specie2;
        this.metric = metric;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%d, %d, %s, %s, %2.2e", specie1, specie2, metric, type, value);
    }
    
}
