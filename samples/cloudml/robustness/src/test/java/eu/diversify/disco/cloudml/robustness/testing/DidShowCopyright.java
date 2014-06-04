/*
 */
package eu.diversify.disco.cloudml.robustness.testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * Check that a copyright notice was displayed on STDOUT
 */
public class DidShowCopyright extends TypeSafeMatcher<Run> {

    private final int year;
    private final String owner;

    public DidShowCopyright(int year, String owner) {
        super();
        this.year = year;
        this.owner = owner;
    }

    @Override
    protected boolean matchesSafely(Run run) {
        Matcher matcher = Pattern.compile("Copyright\\s+\\([cC]\\)\\s+(\\d+)\\s*-([^\\n]+)").matcher(run.getStandardOutput());
        if (matcher.find()) {
            final int shownYear = Integer.parseInt(matcher.group(1));
            final String shownOwner = matcher.group(2);
            return shownYear == year
                    && shownOwner.trim().equals(owner);
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("Copyright (C) %d - %s", year, owner));
    }

    @Factory
    public static <T> org.hamcrest.Matcher<Run> didShowCopyright(int year, String owner) {
        return new DidShowCopyright(year, owner);
    }

}
