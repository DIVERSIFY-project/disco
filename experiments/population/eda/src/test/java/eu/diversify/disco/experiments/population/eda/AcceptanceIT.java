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
package eu.diversify.disco.experiments.population.eda;

import eu.diversify.disco.experiments.testing.Tester;
import java.io.IOException;
import org.junit.Test;

public class AcceptanceIT {
   
    @Test
    public void acceptanceTest() throws IOException, InterruptedException {
       new Tester("eda", "results.csv", "results.pdf").test();
    }
   
}
