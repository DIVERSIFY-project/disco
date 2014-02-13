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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metric factory, which builds diversity metric from a string
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class MetricFactory {

    private final HashMap<String, String> metrics;

    public MetricFactory() {
        this.metrics = new HashMap<String, String>();
        this.metrics.put("GINI SIMPSON INDEX", "eu.diversify.disco.population.diversity.GiniSimpsonIndex");
        this.metrics.put("SHANNON INDEX", "eu.diversify.disco.population.diversity.ShannonIndex");
        this.metrics.put("TRUE DIVERSITY", "eu.diversify.disco.population.diversity.TrueDiversity");

    }

    /**
     * Instantiate a given description from a textual description
     *
     * @param text the textual description of the metric to instantiate
     * @return the related diversity metric
     */
    public DiversityMetric instantiate(String text) {
        DiversityMetric result = null;
        Parser parser = new Parser();
        Description description = parser.parse(text);
        final String escaped = description.getName().trim().replaceAll("\\s+", " ").toUpperCase();
        final String className = this.metrics.get(escaped);
        if (className == null) {
            throw new IllegalArgumentException("Unknown metric name '" + description.getName() + "'");
        }

        try {
            Constructor<?>[] constructors = Class.forName(className).getConstructors();

            // Search for the constructor with the good number of parameters
            int index = 0;
            Constructor<?> selected = null;
            while (index < constructors.length && selected == null) {
                if (description.getParameters().size() == constructors[index].getParameterTypes().length) {
                    selected = constructors[index];
                }
                index++;
            }

            if (selected == null) {
                throw new IllegalArgumentException("Unable to instantiate the '" + description.getName() + "' with " + description.getParameters().size() + " parameters ");
            }

            Object[] actuals = new Object[selected.getParameterTypes().length];
            index = 0;
            for (String key : description.getParameters().keySet()) {
                actuals[index] = description.getParameters().get(key);
            }
            result = (DiversityMetric) selected.newInstance(actuals);

        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);

        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);

        } catch (InstantiationException ex) {
            throw new IllegalArgumentException(ex);

        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }

        return result;
    }

    /**
     * Capture the description of a given metric
     */
    static class Description {

        private String name;
        private final HashMap<String, Double> parameters;

        /**
         * Create a new description of a metric
         */
        public Description() {
            this.name = "No Name";
            this.parameters = new HashMap<String, Double>();
        }

        /**
         * @return the name of the metric of interest
         */
        public String getName() {
            return this.name;
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
         * @return the map of parameters values
         */
        public Map<String, Double> getParameters() {
            return Collections.unmodifiableMap(this.parameters);
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
            final Description other = (Description) obj;
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
    }

    /**
     * A simple parser which builds a description object from a textual
     * description
     *
     * @author Franck Chauvel
     * @since 0.1
     */
    static class Parser {

        /**
         * Parse the text of description
         *
         * @param text the text of the description
         * @return a description object
         */
        public Description parse(String text) {
            Description result = new Description();

            Pattern pattern = Pattern.compile("((.+)\\s*\\((.+)\\))|([^\\(\\)]+)");
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                if (matcher.group(4) != null) {
                    result.setName(matcher.group(4).trim());
                } else {
                    result.setName(matcher.group(2).trim());
                    result.setParameters(parseParameters(matcher.group(3).trim()));
                }
            } else {
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
