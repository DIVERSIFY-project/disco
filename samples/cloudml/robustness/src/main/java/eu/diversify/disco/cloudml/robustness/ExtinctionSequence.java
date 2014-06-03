package eu.diversify.disco.cloudml.robustness;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Capture a single extinction sequence
 */
public class ExtinctionSequence {

    public static ExtinctionSequence fromMap(Map<Integer, Integer> lifeLevels) {
        final List<Entry> entries = new ArrayList<Entry>(lifeLevels.size());
        for (Integer eachExtinctionLevel: lifeLevels.keySet()) {
            entries.add(new Entry(eachExtinctionLevel, lifeLevels.get(eachExtinctionLevel)));
        }
        return new ExtinctionSequence(entries);
    }

    public static ExtinctionSequence fromCsv(String csvText) {
        final List<Entry> entries = new ArrayList<Entry>();
        final String[] lines = csvText.split("\\s+\r?\n");
        for (String eachLine: lines) {
            final String[] cells = eachLine.split(",");
            if (cells[0].equals("killed\\alive")) {
                // nothing to do, this is the header
            } else {
                final int extinctionLevel = Integer.parseInt(cells[0].trim());
                final int lifeLevel = Integer.parseInt(cells[1].trim());
                entries.add(new Entry(extinctionLevel, lifeLevel));
            }
        }
        return new ExtinctionSequence(entries);
    }

    private final int maximumLifeLevel;
    private final List<Entry> entries;

    private ExtinctionSequence(Collection<Entry> entries) {
        validate(entries);
        this.entries = new ArrayList<Entry>(entries);
        this.maximumLifeLevel = maximumLifeLevel(entries);
        Collections.sort(this.entries);
    }

    private void validate(Collection<Entry> entries) {
        if (entries.size() < 2) {
            throw new IllegalArgumentException("An extinction sequence requires at least two data points");
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
            final String error = String.format("An extinction sequence requires that the maximum life and extinction levels agree (found %d alive & %d killed", maximumLifeLevel, maximumExtinctionLevel);
            throw new IllegalArgumentException(error);
        }
    }

    private int minimumLifeLevel(Collection<Entry> entries) {
        final List<Entry> asList = new ArrayList<Entry>(entries);
        int min = asList.get(0).getLifeLevel();
        for (int i = 1; i < entries.size(); i++) {
            final int lifeLevel = asList.get(i).getLifeLevel();
            if (lifeLevel < min) {
                min = lifeLevel;
            }
        }
        return min;
    }

    private int minimumExtinctionLevel(Collection<Entry> entries) {
        final List<Entry> asList = new ArrayList<Entry>(entries);
        int min = asList.get(0).getExtinctionLevel();
        for (int i = 1; i < entries.size(); i++) {
            final int extinctionLevel = asList.get(i).getExtinctionLevel();
            if (extinctionLevel < min) {
                min = extinctionLevel;
            }
        }
        return min;
    }

    private int maximumLifeLevel(Collection<Entry> entries) {
        final List<Entry> asList = new ArrayList<Entry>(entries);
        int max = asList.get(0).getLifeLevel();
        for (int i = 1; i < entries.size(); i++) {
            final int lifeLevel = asList.get(i).getLifeLevel();
            if (lifeLevel > max) {
                max = lifeLevel;
            }
        }
        return max;
    }

    private int maximumExtinctionLevel(Collection<Entry> entries) {
        final List<Entry> asList = new ArrayList<Entry>(entries);
        int max = asList.get(0).getExtinctionLevel();
        for (int i = 1; i < entries.size(); i++) {
            final int extinctionLevel = asList.get(i).getExtinctionLevel();
            if (extinctionLevel > max) {
                max = extinctionLevel;
            }
        }
        return max;
    }

    public int getAliveCount(int killed) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getExtinctionLevel() == killed) {
                return entries.get(i).getLifeLevel();
            }
        }
        return -1;
    }

    public int getUpperBound() {
        return maximumLifeLevel(entries);
    }

    public double getRobustness() {
        double result = 0D;
        for (int i = 1; i < entries.size(); i++) {
            final Entry previous = entries.get(i - 1);
            final Entry current = entries.get(i);
            result += previous.getLifeLevel() * (current.getExtinctionLevel() - previous.getExtinctionLevel());
        }
        return (result / Math.pow(maximumLifeLevel, 2)) * 100D;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < entries.size(); i++) {
            builder.append(String.format("%6d", entries.get(i).extinctionLevel));
        }
        builder.append("\n");
        for (int i = 0; i < entries.size(); i++) {
            builder.append(String.format("%6d", entries.get(i).lifeLevel));
        }
        builder.append("\n");
        return builder.toString();
    }

    public String toCsv() {
        final StringBuilder result = new StringBuilder();
        final String eol = System.lineSeparator();
        result.append("killed\\alive, 1").append(eol);
        for (Entry eachEntry: entries) {
            final String csvLine = String.format("%d, %d", eachEntry.getExtinctionLevel(), eachEntry.getLifeLevel());
            result.append(csvLine).append(eol);
        }
        return result.toString();
    }

    public void toCsvFile(String csvFileName) throws IOException {
        FileWriter file = new FileWriter(csvFileName);
        file.write(toCsv());
        file.close();
    }

    private static class Entry implements Comparable<Entry> {

        private final int extinctionLevel;
        private final int lifeLevel;

        public Entry(int extinctionLevel, int lifeLevel) {
            this.extinctionLevel = rejectIfInvalid(extinctionLevel);
            this.lifeLevel = rejectIfInvalid(lifeLevel);
        }

        private int rejectIfInvalid(int level) {
            if (level < 0) {
                final String error = String.format("Level must be positive in an extinction sequence (%d)", level);
                throw new IllegalArgumentException(error);
            }
            return level;
        }

        public int getExtinctionLevel() {
            return extinctionLevel;
        }

        public int getLifeLevel() {
            return lifeLevel;
        }

        @Override
        public int compareTo(Entry o) {
            return extinctionLevel - o.getExtinctionLevel();
        }

    }
}
