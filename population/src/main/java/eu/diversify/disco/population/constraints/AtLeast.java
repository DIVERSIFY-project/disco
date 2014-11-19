package eu.diversify.disco.population.constraints;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import eu.diversify.disco.population.actions.Action;

/**
 * Set up a minimal head count in a given specie
 */
public class AtLeast implements Constraint {

    private final String specieName;
    private final int minimalHeadCount;

    public AtLeast(String specieName, int minimalHeadCount) {
        requireValid(specieName);
        requireValid(minimalHeadCount);
        
        this.specieName = specieName;
        this.minimalHeadCount = minimalHeadCount;
    }

    private void requireValid(int minimalHeadCount) throws IllegalArgumentException {
        if (minimalHeadCount < 0) {
            final String error = String.format("A minimal head count must be a positive value, but '%d' was found", minimalHeadCount);
            throw new IllegalArgumentException(error);
        }
    }

    private void requireValid(String specieName) throws IllegalArgumentException {
        if (specieName == null) {
            throw new IllegalArgumentException("'null' is not a valid species name");
        }
        if (specieName.isEmpty()) {
            throw new IllegalArgumentException("The empty string ('') is not a valid species name");
        }
    }

    /**
     * @return the name of the specie whose headcount is constrained
     */
    public String getSpecieName() {
        return specieName;
    }

    /**
     * @return the minimum head count set by this constraint
     */
    public int getMinimalHeadCount() {
        return minimalHeadCount;
    }

    @Override
    public PopulationBuilder activateOn(PopulationBuilder builder) {
        return builder.withAtLeast(minimalHeadCount, specieName);
    }

    @Override
    public boolean allows(Action action, Population target) {
        return action.ensureAtLeast(minimalHeadCount, specieName, target);
    }
    
    
    
}
