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

import org.cloudml.core.*;

/**
 * Simulate a given extinction sequence
 */
public class Simulator {

    private final Deployment deployment;

    public Simulator(Deployment deployment) {
        this.deployment = deployment;
        markAllAsAlive();
    }

    public int countAliveComponents() {
        int count = 0;
        for (Component c: deployment.getComponents()) {
            if (isAlive(c) && canStillBeStarted(c)) {
                count++;
            }
        }
        return count;
    }

    public boolean canStillBeStarted(Component c) {
        return canBeProvisioned(c) && allMandatoryDependenciesCanBeResolved(c);
    }

    public boolean canBeProvisioned(Component c) {
        if (isDead(c)) {
            return false;
        }
        if (c.isExternal()) {
            return true;
        }

        for (Component other: deployment.getComponents()) {
            if (isAlive(other) && other.canHost(c.asInternal())) {
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
                if (isAlive(eachRelationship.getServerComponent())) {
                    return true;
                }
            }
        }
        return false;
    }

  
    public boolean isAlive(Component c) {
        return !isDead(c);
    }
    
    public boolean isDead(Component c) {
        return c.hasProperty("Killed", "true");
    }

    public void killOneComponent() {
        int killed = 0;
        for (Component c: deployment.getComponents()) {
            if (isAlive(c)) {
                killed++;
                markAsDead(c);
                break;
            }
        }

        while (killed != 0) {
            killed = 0;
            for (Component c: deployment.getComponents()) {
                if (isAlive(c) && !canStillBeStarted(c)) {
                    killed++;
                    markAsDead(c);
                }
            }
        }
    }

    public void markAsDead(Component c) {
        c.getProperties().get("Killed").setValue("true");
    }

    public void markAllAsAlive() {
        for (Component c: deployment.getComponents()) {
            markAsAlive(c);
        }
    }

    private void markAsAlive(Component component) {
        if (component.hasProperty("Killed")) {
            component.getProperties().get("Killed").setValue("false");
        } else {
            component.getProperties().add(new Property("Killed", "false"));
        }
    }

    public int countComponents() {
        return deployment.getComponents().size();
    }

}
