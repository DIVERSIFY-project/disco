/*
 */
package eu.diversify.disco.cloudml.robustness.testing;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * Check whether exception were reported on either 'stdout' or 'stderr' during
 * the run.
 */
public class DidNotRaiseAnyException extends TypeSafeMatcher<Run> {

    private static final List<String> errorKeywords
            = Arrays.asList(new String[]{
                "ERROR", "Error", "error", "Exception", "exception"
            });

    @Override
    protected boolean matchesSafely(Run run) {
        boolean match = true;
        final Iterator<String> iterator = errorKeywords.iterator();
        while (iterator.hasNext() && match) {
            final String eachKeyword = iterator.next();
            match &= !run.getStandardOutput().contains(eachKeyword)
                    && !run.getStandardError().contains(eachKeyword);
        }
        return match;
    }

    @Override
    public void describeTo(Description d) {
        d.appendText("unexpected error were reported");
    }

    @Factory
    public static <T> org.hamcrest.Matcher<Run> didNotReportAnyError() {
        return new DidNotRaiseAnyException();
    }

}
