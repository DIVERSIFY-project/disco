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

import java.io.IOException;

/**
 * The interface of a run
 */
public abstract class Run {
    
      public static RunInThread withArguments(String fileName) throws IOException, InterruptedException {
        return new RunInThread(".", new String[]{"java", "-jar",
                                                 "robustness-final.jar",
                                                 fileName});
    }

    public static RunInThread withCommandLine(String... extraArguments) throws IOException, InterruptedException {
        final String[] all = new String[3 + extraArguments.length];
        all[0] = "java";
        all[1] = "-jar";
        all[2] = "robustness-final.jar";
        System.arraycopy(extraArguments, 0, all, 3, extraArguments.length);
        return new RunInThread(".", all);
    }

    public abstract String getStandardError();

    public abstract String getStandardOutput();
    
}
