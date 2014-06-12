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
package eu.diversify.disco.cloudml.robustness;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static eu.diversify.disco.cloudml.robustness.Action.kill;

/**
 * A collection of extinction sequence on which some statistics are available
 */
public class SequenceGroup {

    private final Population population;
    private final List<Extinction> sequences;

    public SequenceGroup(Population population) {
        this.population = population;
        this.sequences = new ArrayList<Extinction>();
    }
    
    public void add(Extinction extinction) {
        this.sequences.add(extinction);
    }

    public int size() {
        return sequences.size();
    }

    public Distribution robustness() {
        final List<Double> data = new ArrayList<Double>(sequences.size());
        for (Extinction eachSequence: sequences) {
            data.add(eachSequence.robustness());
        }
        return new Distribution(data);
    }

    public Distribution length() {
        final List<Double> data = new ArrayList<Double>(sequences.size());
        for (Extinction eachSequence: sequences) {
            data.add((double) eachSequence.length());
        }
        return new Distribution(data);
    }

    public Map<String, Distribution> ranking() {
        Map<String, Distribution> result = new HashMap<String, Distribution>();
        for (String eachIndividual: population.getIndividualNames()) {
            result.put(eachIndividual, sensibilityOf(eachIndividual));
        }
        return result;
    }

    public Distribution sensibilityOf(String eachIndividual) {
        final List<Double> data = new ArrayList<Double>();
        for (Extinction eachSequence: sequences) {
            final int sensibility = eachSequence.impactOf(kill(eachIndividual));
            if (sensibility > 0) {
                data.add((double) sensibility);
            }
        }
        return new Distribution(data);
    }

    public String summary() {
        final StringBuilder builder = new StringBuilder();
        final String eol = System.lineSeparator();
        builder.append("Robustness: ").append(robustness()).append(eol);
        builder.append("Seq. length: ").append(length()).append(eol);
        builder.append("Sensitivity:").append(eol);
        final Map<String, Distribution> sensibility = ranking();
        for (String eachIndividual: sensibility.keySet()) {
            builder.append(" - ").append(eachIndividual).append(":").append(sensibility.get(eachIndividual)).append(eol);
        }
        return builder.toString();
    }

    public String toCsv() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("dead count");
        for (int i = 1; i <= sequences.size(); i++) {
            buffer.append(", ");
            buffer.append(String.format("action %d, survivor count %d", i, i));
        }
        buffer.append(System.lineSeparator());
        for (int deadcount = 0; deadcount <= population.headcount(); deadcount++) {
            buffer.append(deadcount);
            for (Extinction eachSequence: sequences) {
                buffer.append(", ");
                State state = eachSequence.stateAt(deadcount);
                if (state == null) {
                    buffer.append("none").append(", ").append(0);
                } else {
                    buffer.append(state.getTrigger()).append(", ").append(state.survivorCount());
                }
            }
            buffer.append(System.lineSeparator());
        }
        return buffer.toString();
    }

    public void toCsvFile(String csvFileName) throws IOException {
        FileWriter file = new FileWriter(csvFileName);
        file.write(toCsv());
        file.close();
    }

}
