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
package eu.diversify.disco.population.diversity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metric factory, which builds diversity metric from a string
 */
public class MetricFactory {

    public static DiversityMetric create(String text) {
        return new Parser().parse(text).make();
    }

    private static interface Factory {

        public DiversityMetric make();
    }

    public DiversityMetric instantiate(String text) {
        return new Parser().parse(text).make();
    }
    public static final String MISSING_NAME = "No Name";
    private final HashMap<String, Factory> factories;
    private String name;
    private final HashMap<String, Double> parameters;

    /**
     * Create a new description of a metric
     */
    public MetricFactory() {
        factories = new HashMap<String, Factory>();
        addfactoryForTrueDiversity();
        addFactoryForShannonIndex();
        addFactoryForGiniSimpsonIndex();
        addFactoryForStandardDeviation();
        this.name = MISSING_NAME;
        this.parameters = new HashMap<String, Double>();
    }

    public DiversityMetric make() {
        if (factories.containsKey(escapedName())) {
            return factories.get(escapedName()).make();
        }
        final String message = String.format("Unknown diversity metric '%s'", escapedName());
        throw new IllegalArgumentException(message);
    }

    public String escapedName() {
        return name.trim().replaceAll("\\s+", " ").toUpperCase();
    }

    /**
     * Update the name of the metric of interest
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Update the values bind to the parameters
     *
     * @param parameters the new values to set for the parameters
     */
    public void setParameters(Map<String, Double> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    /**
     * Add a new parameter to this description
     *
     * @param name the name of the parameter
     * @param value the value associated with the parameter
     */
    public void setParameter(String name, Double value) {
        this.parameters.put(name, value);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetricFactory other = (MetricFactory) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.parameters != other.parameters && (this.parameters == null || !this.parameters.equals(other.parameters))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append(" (");
        if (!this.parameters.isEmpty()) {
            int size = this.parameters.size();
            int counter = 0;
            for (String key : this.parameters.keySet()) {
                builder.append(key);
                builder.append(" = ");
                builder.append(this.parameters.get(key));
                if (counter < size - 1) {
                    builder.append(", ");
                }
                counter++;
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private void addfactoryForTrueDiversity() {
        factories.put("TRUE DIVERSITY", new Factory() {
            @Override
            public DiversityMetric make() {
                final Double theta = parameters.containsKey("theta") ? parameters.get("theta") : TrueDiversity.DEFAULT_THETA;
                return new TrueDiversity(theta);
            }
        });
    }

    private void addFactoryForShannonIndex() {
        factories.put("SHANNON INDEX", new Factory() {
            @Override
            public DiversityMetric make() {
                return new ShannonIndex();
            }
        });
    }

    private void addFactoryForGiniSimpsonIndex() {
        factories.put("GINI SIMPSON INDEX", new Factory() {
            @Override
            public DiversityMetric make() {
                return new GiniSimpsonIndex();
            }
        });
    }

    private void addFactoryForStandardDeviation() {
        factories.put("STANDARD DEVIATION", new Factory() {
            @Override
            public DiversityMetric make() {
                return new StandardDeviation();
            }
        });

    }

    /**
     * A simple parser which builds a description object from a textual
     * description
     */
    static class Parser {

        /**
         * Parse the text of description
         *
         * @param text the text of the description
         * @return a description object
         */
        public MetricFactory parse(String text) {
            MetricFactory result = new MetricFactory();

            Pattern pattern = Pattern.compile("((.+)\\s*\\((.+)\\))|([^\\(\\)]+)");
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                if (matcher.group(4) != null) {
                    result.setName(matcher.group(4).trim());
                }
                else {
                    result.setName(matcher.group(2).trim());
                    result.setParameters(parseParameters(matcher.group(3).trim()));
                }
            }
            else {
                throw new IllegalArgumentException("Illformed description");
            }
            return result;
        }

        /**
         * Parse a list of parameter as key value pairs
         *
         * @param text the list of parameters values as text
         * @return a map from key to values
         */
        private Map<String, Double> parseParameters(String text) {
            HashMap<String, Double> result = new HashMap<String, Double>();
            Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*(\\d+(\\.\\d+)?)");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                result.put(matcher.group(1), Double.parseDouble(matcher.group(2)));
            }
            return result;
        }
    }
}