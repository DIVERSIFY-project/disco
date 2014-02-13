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
package eu.diversify.disco.experiments.commons;

import eu.diversify.disco.experiments.commons.data.DataSet;
import java.util.List;

/**
 * General behaviour of experiments
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Experiment {

    /**
     * Run the experiment against the predefined parameters and return the
     * associated result set
     *
     * @return the associated result set.
     */
    public List<DataSet> run();
}
