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
