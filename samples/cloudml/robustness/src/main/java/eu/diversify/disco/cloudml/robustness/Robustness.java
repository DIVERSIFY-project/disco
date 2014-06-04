package eu.diversify.disco.cloudml.robustness;

import java.util.HashMap;
import java.util.Map;
import org.cloudml.core.Deployment;

/**
 * Calculate the robustness of the given deployment model using an extinction
 * sequence.
 */
public class Robustness {

    private final Simulator deployment;
    private ExtinctionSequence sequence;

    public Robustness(Deployment deployment) {
        this.deployment = new Simulator(deployment);
        this.sequence = null;
    }

    public double value() {
        return getExtinctionSequence().getRobustness();
    }
    
    public ExtinctionSequence getExtinctionSequence() {
        if (sequence == null) {
            computeRobustness();
        }
        return sequence;
    }

    private void computeRobustness() {
        final Map<Integer, Integer> lifeLevels = new HashMap<Integer, Integer>();

        deployment.markAllAsAlive();

        int lifeLevel = deployment.countAliveComponents();
        lifeLevels.put(0, lifeLevel);

        for (int deathLevel = 1; deathLevel <= deployment.countComponents(); deathLevel++) {
            deployment.killOneComponent();
            lifeLevel = deployment.countAliveComponents();
            lifeLevels.put(deathLevel, lifeLevel);
        }

        sequence = ExtinctionSequence.fromMap(lifeLevels);
    }

}
