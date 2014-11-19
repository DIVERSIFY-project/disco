package eu.diversify.disco.population.constraints;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationBuilder;
import eu.diversify.disco.population.actions.Action;

/**
 * Set up a maximal number of individual in a given specie.
 */
public class AtMost implements Constraint {

    private final String specieName;
    private final int maximalHeadCount;

    public AtMost(String specieName, int maximalHeadCount) {
        requireValid(specieName);
        requireValid(maximalHeadCount);

        this.specieName = specieName;
        this.maximalHeadCount = maximalHeadCount;
    }

    private void requireValid(int maximalHeadCount) throws IllegalArgumentException {
        if (maximalHeadCount <= 0) {
            final String error = String.format("A maximal head count must be a positive non-null integer value, but '%d' was found", maximalHeadCount);
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
     * @return the name of specie subject to this constraint
     */
    public String getSpecieName() {
        return specieName;
    }

    /**
     * @return the maximal headcount associated with this constraint
     */
    public int getMaximalHeadCount() {
        return maximalHeadCount;
    }

    @Override
    public PopulationBuilder activateOn(PopulationBuilder builder) {
        return builder.withAtMost(maximalHeadCount, specieName);
    }

    @Override
    public boolean allows(Action action, Population target) {
        if (target == null) {
            final String error = String.format("Unable to evaluate action '%s' on 'null'", action.toString());
            throw new IllegalArgumentException(error);
        }
        if (target.hasAnySpecieNamed(specieName)) {
            final int impact = action.impactOnSpecie(specieName, target);
            return (target.getSpecie(specieName).getHeadcount() + impact) <= maximalHeadCount;
        }
        return true;
    }

}
