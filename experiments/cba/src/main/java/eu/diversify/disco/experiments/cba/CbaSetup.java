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
package eu.diversify.disco.experiments.cba;

import eu.diversify.disco.experiments.commons.Experiment;
import eu.diversify.disco.experiments.commons.Setup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Define the parameters of the Cost-Benefits Analysis experiment.
 */
public class CbaSetup implements Setup {

    private final List<String> deploymentModels;
    private final ArrayList<Double> diversityLevels;
    private String controlStrategy;
    private String diversityMetric;
    private int sampleCount;

    public CbaSetup() {
        this.deploymentModels = new ArrayList<String>();
        this.diversityLevels = new ArrayList<Double>();
    }

    @Override
    public Experiment buildExperiment() {
       return new CbaExperiment(this); 
    }

    public List<String> getDeploymentModels() {
        return Collections.unmodifiableList(this.deploymentModels);
    }

    public void setDeploymentModels(List<String> deploymentModels) {
        this.deploymentModels.clear();
        this.deploymentModels.addAll(deploymentModels);
    }

    public String getControlStrategy() {
        return controlStrategy;
    }

    public void setControlStrategy(String controlStrategy) {
        this.controlStrategy = controlStrategy;
    }

    public String getDiversityMetric() {
        return diversityMetric;
    }

    public void setDiversityMetric(String diversityMetric) {
        this.diversityMetric = diversityMetric;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public void setDiversityLevels(List<Double> levels) {
        this.diversityLevels.clear();
        for (Double level : levels) {
            diversityLevels.add(level);
        }
    }

    public List<Double> getDiversityLevels() {
        return Collections.unmodifiableList(this.diversityLevels);
    }
}
