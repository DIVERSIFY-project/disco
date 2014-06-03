/*
 */
package eu.diversify.disco.cloudml.robustness;

import org.cloudml.core.*;

/**
 * Simulate a given extinction sequence
 */
public class Simulator {

    private final Deployment deployment;

    public Simulator(Deployment deployment) {
        this.deployment = deployment;
    }

    public int countAliveComponents() {
        int count = 0;
        for (Component c: deployment.getComponents()) {
            if (isNotKilled(c) && canStillBeStarted(c)) {
                count++;
            }
        }
        return count;
    }

    public boolean canStillBeStarted(Component c) {
        return canBeProvisioned(c) && allMandatoryDependenciesCanBeResolved(c);
    }

    private boolean canBeProvisioned(Component c) {
        if (c.isExternal()) {
            return true;
        }

        for (Component other: deployment.getComponents()) {
            if (isNotKilled(other) && c.canHost(c.asInternal())) {
                return true;
            }
        }
        return false;
    }

    private  boolean allMandatoryDependenciesCanBeResolved(Component c) {
        if (c.isExternal()) {
            return true;
        }
        InternalComponent internal = c.asInternal();
        boolean all = true;
        for (RequiredPort eachDependency: internal.getRequiredPorts()) {
            all &= canBeResolved(eachDependency);
            if (!all) {
                return all;
            }
        }
        return all;
    }

    private boolean canBeResolved(RequiredPort port) {
        for (Relationship eachRelationship: deployment.getRelationships()) {
            if (eachRelationship.getRequiredEnd() == port) {
                if (isNotKilled(eachRelationship.getServerComponent())) {
                    return true;
                }
            }
        }
        return false;
    }

  
    
    public boolean isNotKilled(Component c) {
        return !c.hasProperty("Killed", "true");
    }

    public void killOneComponent() {
        int killed = 0;
        for (Component c: deployment.getComponents()) {
            if (isNotKilled(c)) {
                killed++;
                kill(c);
            }
        }

        while (killed != 0) {
            killed = 0;
            for (Component c: deployment.getComponents()) {
                if (isNotKilled(c) && !canStillBeStarted(c)) {
                    killed++;
                    kill(c);
                }
            }
        }

    }

    public void kill(Component c) {
        c.getProperties().add(new Property("Killed", "true"));
    }

}
