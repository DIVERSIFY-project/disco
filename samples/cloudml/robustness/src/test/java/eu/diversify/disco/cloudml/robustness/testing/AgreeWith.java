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

package eu.diversify.disco.cloudml.robustness.testing;

import eu.diversify.disco.cloudml.robustness.ExtinctionSequence;
import java.util.List;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.junit.matchers.TypeSafeMatcher;

/**
 *
 */
public class AgreeWith extends TypeSafeMatcher<ExtinctionSequence> {

    private final Map<Integer, List<Integer>> data;

    public AgreeWith(Map<Integer, List<Integer>> data) {
        this.data = data;
    }
           
    @Override
    public boolean matchesSafely(ExtinctionSequence sequence) {
        if (sequence.length() != data.size()) {
            return false;
        }
        for(int eachDeadCount: data.keySet()) {
            final List<Integer> expected = data.get(eachDeadCount);
            final List<Integer> actual = sequence.getSurvivorCounts(eachDeadCount).all(); 
            if (!actual.equals(expected)) {
                return false;
            }            
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        final StringBuilder builder = new StringBuilder();
        for(int eachDeadCount: data.keySet()) {
            builder.append(eachDeadCount).append(":");
            for(int eachSurvivorCount: data.get(eachDeadCount)) {
                builder.append(eachSurvivorCount).append(" ");
            }
            builder.append(System.lineSeparator());
        }
        description.appendText(builder.toString());
    }

     @Factory
    public static <T> org.hamcrest.Matcher<ExtinctionSequence> agreeWith(Map<Integer, List<Integer>> data) {
        return new AgreeWith(data);
    }

    
}
