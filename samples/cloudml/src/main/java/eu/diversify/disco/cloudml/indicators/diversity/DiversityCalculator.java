/*
 */

package eu.diversify.disco.cloudml.indicators.diversity;

import eu.diversify.disco.cloudml.CloudML;
import eu.diversify.disco.cloudml.indicators.DeploymentIndicator;
import eu.diversify.disco.cloudml.transformations.Transformation;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import org.cloudml.core.DeploymentModel;

/**
 * General Interface of Diversity calculators
 * 
 * @author Franck Chauvel
 * @since 0.1
 */
public class DiversityCalculator extends DeploymentIndicator {

    private final DiversityMetric metric;
    private final Transformation transformation;
    
    public DiversityCalculator(DiversityMetric metric) {
        this.metric = metric;
        this.transformation = new Transformation();
    }

    @Override
    protected double doEvaluation(DeploymentModel deployment) {
        CloudML cloudml = new CloudML();
        cloudml.setRoot(deployment);
        Population population = transformation.forward(cloudml);
        return metric.normalised(population);
    }
    
}
