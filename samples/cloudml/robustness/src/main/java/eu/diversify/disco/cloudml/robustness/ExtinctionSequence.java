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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.cloudml.robustness;

import java.io.*;
import java.util.*;

/**
 * Capture a single extinction sequence
 */
public class ExtinctionSequence {

    public static ExtinctionSequence fromMap(Map<Integer, List<Integer>> survivorCounts) {
        final List<SurvivorCounts> entries = new ArrayList<SurvivorCounts>(survivorCounts.size());
        for (Integer eachExtinctionLevel: survivorCounts.keySet()) {
            entries.add(new SurvivorCounts(eachExtinctionLevel, survivorCounts.get(eachExtinctionLevel)));
        }
        return new ExtinctionSequence(entries);
    }

    public static ExtinctionSequence fromCsv(String csvText) {
        final List<SurvivorCounts> entries = new ArrayList<SurvivorCounts>();
        final String[] lines = csvText.split("\\r?\\n");
        for (String eachLine: lines) {
            final List<Integer> survivorCounts = new ArrayList<Integer>();
            final String[] cells = eachLine.split(",");
            if (cells[0].equals("killed\\alive")) {
                // nothing to do, this is the header
            } else {
                final int extinctionLevel = Integer.parseInt(cells[0].trim());
                for (int i = 1; i < cells.length; i++) {
                    survivorCounts.add(Integer.parseInt(cells[1].trim()));
                }
                entries.add(new SurvivorCounts(extinctionLevel, survivorCounts));
            }
        }
        return new ExtinctionSequence(entries);
    }

    public static ExtinctionSequence fromCsvFile(String csvFile) throws FileNotFoundException, IOException {
        final BufferedReader br = new BufferedReader(new FileReader(csvFile));
        final StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append(System.lineSeparator());
                line = br.readLine();
            }
            return fromCsv(sb.toString());

        } finally {
            br.close();

        }
    }

    private final int maximumLifeLevel;
    private final List<SurvivorCounts> entries;

    private ExtinctionSequence(Collection<SurvivorCounts> entries) {
        validate(entries);
        this.entries = new ArrayList<SurvivorCounts>(entries);
        this.maximumLifeLevel = maximumLifeLevel(entries);
        Collections.sort(this.entries);
    }

    private void validate(Collection<SurvivorCounts> entries) {
        if (entries.size() < 2) {
            throw new IllegalArgumentException("An extinction sequence requires at least two data points" + entries);
        }
        if (minimumLifeLevel(entries) != 0) {
            throw new IllegalArgumentException("Life level of an extinction sequence must be in [0, +inf]");
        }
        if (minimumExtinctionLevel(entries) != 0) {
            throw new IllegalArgumentException("An extinction sequence must contains a zero extinction level");
        }
        final int maximumLifeLevel = maximumLifeLevel(entries);
        final int maximumExtinctionLevel = maximumExtinctionLevel(entries);
        if (maximumLifeLevel != maximumExtinctionLevel) {
            final String error = String.format("An extinction sequence requires that the maximum life and extinction levels agree (found %d alive & %d killed)\r\n%s", maximumLifeLevel, maximumExtinctionLevel, entries);
            throw new IllegalArgumentException(error);
        }
    }

    private int minimumLifeLevel(Collection<SurvivorCounts> entries) {
        final List<SurvivorCounts> asList = new ArrayList<SurvivorCounts>(entries);
        int min = asList.get(0).getOneSurvivorCount();
        for (int i = 1; i < entries.size(); i++) {
            final int lifeLevel = asList.get(i).getOneSurvivorCount();
            if (lifeLevel < min) {
                min = lifeLevel;
            }
        }
        return min;
    }

    private int minimumExtinctionLevel(Collection<SurvivorCounts> entries) {
        final List<SurvivorCounts> asList = new ArrayList<SurvivorCounts>(entries);
        int min = asList.get(0).getDeadCount();
        for (int i = 1; i < entries.size(); i++) {
            final int extinctionLevel = asList.get(i).getDeadCount();
            if (extinctionLevel < min) {
                min = extinctionLevel;
            }
        }
        return min;
    }

    private int maximumLifeLevel(Collection<SurvivorCounts> entries) {
        final List<SurvivorCounts> asList = new ArrayList<SurvivorCounts>(entries);
        int max = asList.get(0).getOneSurvivorCount();
        for (int i = 1; i < entries.size(); i++) {
            final int lifeLevel = asList.get(i).getOneSurvivorCount();
            if (lifeLevel > max) {
                max = lifeLevel;
            }
        }
        return max;
    }

    private int maximumExtinctionLevel(Collection<SurvivorCounts> entries) {
        final List<SurvivorCounts> asList = new ArrayList<SurvivorCounts>(entries);
        int max = asList.get(0).getDeadCount();
        for (int i = 1; i < entries.size(); i++) {
            final int extinctionLevel = asList.get(i).getDeadCount();
            if (extinctionLevel > max) {
                max = extinctionLevel;
            }
        }
        return max;
    }

    public int length() {
        return entries.size();
    }

    public int getLifeLevel(int killed) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getDeadCount() == killed) {
                return entries.get(i).getOneSurvivorCount();
            }
        }
        return -1;
    }

    public SurvivorCounts getSurvivorCounts(int killed) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getDeadCount() == killed) {
                return entries.get(i);
            }
        }
        return null;
    }

    public int getUpperBound() {
        return maximumLifeLevel(entries);
    }

    public double getRobustness() {
        double result = 0D;
        for (int i = 1; i < entries.size(); i++) {
            final SurvivorCounts previous = entries.get(i - 1);
            final SurvivorCounts current = entries.get(i);
            result += previous.mean() * (current.getDeadCount() - previous.getDeadCount());
        }
        return (result / Math.pow(maximumLifeLevel, 2)) * 100D;
    }

    public String summary() {
        final StringBuilder builder = new StringBuilder();
        final String eol = System.lineSeparator();
        builder.append("Extinction sequence:").append(eol);
        builder.append(String.format("%10s %10s %10s %10s %10s", "dead count", "min", "average", "max", "std. dev.")).append(eol);
        for(SurvivorCounts each: entries) {
            builder.append(String.format("%10d %10d %10.2f %10d %10.2f", each.getDeadCount(), each.minimum(), each.mean(), each.maximum(), each.standardDeviation()));
            builder.append(eol);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return toCsv();
    }

    public String toCsv() {
        final StringBuilder result = new StringBuilder();
        final String eol = System.lineSeparator();
        result.append("killed\\alive");
        for (int i = 1; i <= entries.get(0).all().size(); i++) {
            result.append(String.format(",%d", i));
        }
        result.append(eol);
        for (SurvivorCounts eachEntry: entries) {
            result.append(String.format("%d", eachEntry.getDeadCount()));
            for (int eachSurvivorCount: eachEntry.all()) {
                result.append(String.format(",%d", eachSurvivorCount));
            }
            result.append(eol);
        }
        return result.toString();
    }

    public void toCsvFile(String csvFileName) throws IOException {
        FileWriter file = new FileWriter(csvFileName);
        file.write(toCsv());
        file.close();

    }

}
