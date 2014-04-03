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
package eu.diversify.disco.experiments.testing;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class TesterBuilderTest extends TestCase {

    @Test
    public void testBuilderTester() {
        Tester tester = new Tester("exp1", "result.csv", "foo.pdf", "bar.pdf");

        assertThat("CSV count", tester.getExpectedCsvFiles().size(), is(equalTo(1)));
        assertThat("PDF count", tester.getExpectedPdfFiles().size(), is(equalTo(2)));
        assertThat("CSV files", tester.getExpectedCsvFiles(), contains("result.csv"));
        assertThat("PDF files", tester.getExpectedPdfFiles(), containsInAnyOrder("foo.pdf", "bar.pdf"));

    }
}
