/*
 */
package eu.diversify.disco.cloudml.robustness.testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * Check whether the usage information are displayed
 */
public class DidShowUsage extends TypeSafeMatcher<Run> {

    @Override
    protected boolean matchesSafely(Run run) {
        final Matcher matcher = Pattern.compile("Usage:").matcher(run.getStandardOutput());
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Usage");
    }

    @Factory
    public static <T> org.hamcrest.Matcher<Run> didShowUsage() {
        return new DidShowUsage();
    }

}
