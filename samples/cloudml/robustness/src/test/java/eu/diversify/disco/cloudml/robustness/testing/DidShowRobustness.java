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
package eu.diversify.disco.cloudml.robustness.testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * Assert the robustness level displayed during the run
 */
public class DidShowRobustness extends TypeSafeMatcher<Run> {

    private final double robustness;

    public DidShowRobustness(double robustness) {
        this.robustness = robustness;
    }

    @Override
    protected boolean matchesSafely(Run run) {
        final Matcher matcher = Pattern.compile("Robustness:\\s*(\\d+\\.\\d+)\\s*%").matcher(run.getStandardOutput());
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) == robustness;
        }
        return false;
    }

    @Override
    public void describeTo(Description d) {
        d.appendText(String.format("Robustness: %.2f %%", robustness));
    }

    @Factory
    public static <T> org.hamcrest.Matcher<Run> didShowRobustness(double robustness) {
        return new DidShowRobustness(robustness);
    }

}
