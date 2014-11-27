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
package eu.diversify.disco.population;

import static eu.diversify.disco.population.Population.DEFAULT_SPECIE_NAME_FORMAT;
import static eu.diversify.disco.population.PopulationBuilder.*;

import static eu.diversify.disco.population.actions.ScriptBuilder.*;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.AddSpecie;
import eu.diversify.disco.population.actions.RemoveSpecie;
import eu.diversify.disco.population.actions.ScriptBuilder;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import eu.diversify.disco.population.constraints.Constraint;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A mutable data object representing a population
 *
 * FIXME: Change the implementation of the list of species name to ensure access
 * to species name in O(c)
 */
public class MutablePopulation implements Population {

    private static final int DEFAULT_INDEX = -1;
    private final ArrayList<Specie> species;
    private final ArrayList<Constraint> constraints;

    MutablePopulation() {
        this(new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Constraint>());
    }
    
    MutablePopulation(List<String> speciesNames, List<Integer> distribution) {
      this(speciesNames, distribution, new ArrayList<Constraint>());
    }

    MutablePopulation(List<String> speciesNames, List<Integer> distribution, Collection<Constraint> constraints) {
        checkConsistencyBetween(speciesNames, distribution);
        this.species = new ArrayList<Specie>();
        this.constraints = new ArrayList<Constraint>(constraints);
        for (int index = 0; index < speciesNames.size(); index++) {
            this.species.add(new Specie(this, speciesNames.get(index), distribution.get(index))); 
        }
    }

    private MutablePopulation(MutablePopulation model) {
        this(model.getSpeciesNames(), model.getDistribution(), model.constraints);
    }

    public MutablePopulation prepareUpdate() {
        return this;
    }

    @Override
    public boolean allows(Action action) {
        boolean supported = true;
        final Iterator<Constraint> eachConstraint = constraints.iterator();
        while(supported && eachConstraint.hasNext()) {
            supported &= eachConstraint.next().allows(action, this);
        }
        return supported;
    }

    // FIXME: to be refactored
    private List<Action> allPossibleActions(int scaleFactor) {
        final ArrayList<Action> possibleActions = new ArrayList<Action>();
        ScriptBuilder newSpecies = aScript();
        for (int i = 0; i < scaleFactor; i++) {
            newSpecies.addSpecie();
        }
        possibleActions.add(newSpecies.build());
        for (Specie specie : species) {
            if (getTotalHeadcount() > specie.getHeadcount()) {
                possibleActions.add(new RemoveSpecie(specie.getName()));
            }
            possibleActions.add(new ShiftNumberOfIndividualsIn(specie.getName(), +scaleFactor));
            if (getTotalHeadcount() > scaleFactor && specie.getHeadcount() >= scaleFactor) {
                possibleActions.add(new ShiftNumberOfIndividualsIn(specie.getName(), -scaleFactor));
            }
            for (Specie otherSpecie : species) {
                if (!specie.equals(otherSpecie)) {
                    if (specie.getHeadcount() >= scaleFactor) {
                        possibleActions.add(aScript()
                                .shift(specie.getName(), -scaleFactor)
                                .shift(otherSpecie.getName(), +scaleFactor)
                                .build());
                    }
                }
            }
        }
        return possibleActions;
    }

    @Override
    public List<Action> allLegalActions(int scaleFactor) {
        final ArrayList<Action> legalActions = new ArrayList<Action>();
        for (Action action : allPossibleActions(scaleFactor)) {
            if (allows(action)) {
                legalActions.add(action);
            }
        }
        return legalActions;
    }

    @Override
    public boolean isEmpty() {
        return getTotalHeadcount() == 0;
    }

    @Override
    public int getTotalHeadcount() {
        int total = 0;
        for (Specie specie : species) {
            total += specie.getHeadcount();
        }
        return total;
    }

    @Override
    public int getSpeciesCount() {
        return species.size();
    }

    @Override
    public Specie getSpecie(int index) {
        rejectInvalidIndex(index);
        return species.get(index - 1);
    } 
    
    @Override
    public Specie getSpecie(String name) {
        return getSpecie(getSpecieIndex(name));
    }

    @Override
    public int getSpecieIndex(String specieName) {
        int index = DEFAULT_INDEX;
        for (int i = 0; i < species.size(); i++) {
            if (species.get(i).isNamed(specieName)) {
                index = i + 1;
            }
        }
        return index > 0 ? index : DEFAULT_INDEX;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.species != null ? this.species.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object != null) {
            if (object instanceof Population) {
                final Population other = (Population) object;
                result = this.toMap().equals(other.toMap());
            }
        }
        return result;
    }

    @Override
    public Population addSpecie() {
        String name = nextSpecieName();
        return addSpecie(name);
    }

    private String nextSpecieName() {
        return String.format(DEFAULT_SPECIE_NAME_FORMAT, minimumUnusedId());
    }

    private int minimumUnusedId() {
        ArrayList<Integer> usedId = usedSpecieId();
        int minimumUnusedId = 1;
        while (usedId.contains(minimumUnusedId)) {
            minimumUnusedId++;
        }
        return minimumUnusedId;
    }

    private ArrayList<Integer> usedSpecieId() throws NumberFormatException {
        final ArrayList<Integer> usedId = new ArrayList<Integer>();
        for (String specieName : getSpeciesNames()) {
            Pattern pattern = Pattern.compile(DEFAULT_SPECIE_NAME_REGEX);
            Matcher matcher = pattern.matcher(specieName);
            if (matcher.matches()) {
                usedId.add(Integer.parseInt(matcher.group(1)));
            }
        }
        return usedId;
    }

    @Override
    public Population addSpecie(String specieName) {
        rejectInvalidNames(specieName);
        final MutablePopulation updated = prepareUpdate();
        updated.species.add(new Specie(this, specieName));
        return updated;
    }

    @Override
    public boolean hasAnySpecieNamed(String specieName) {
        return getSpecieIndex(specieName) > -1;
    }

    @Override
    public Population removeSpecie(int specieIndex) {
        final MutablePopulation updated = prepareUpdate();
        updated.species.remove(getSpecie(specieIndex));
        return updated;
    }

    @Override
    public Population removeSpecie(String specieName) {
        return removeSpecie(getSpecieIndex(specieName));
    }

    private void checkConsistencyBetween(List<String> speciesName, List<Integer> distribution) {
        if (speciesName.size() < distribution.size()) {
            throw new IllegalArgumentException("Missing species name");
        }
        if (speciesName.size() > distribution.size()) {
            throw new IllegalArgumentException("Missing individual counts");
        }
    }

    private void rejectInvalidNames(String specieName) {
        if (specieName == null) {
            throw new IllegalArgumentException("Specie name shall not be null.");
        }
        if (specieName.equals("")) {
            throw new IllegalArgumentException("The empty string '' is not a valid specie name.");
        }
        if (hasAnySpecieNamed(specieName)) {
            throw new IllegalArgumentException("Duplicated specie name '" + specieName + "'");
        }
    }

    private void rejectInvalidIndex(int specieIndex) {
        if (specieIndex < 1 || specieIndex > species.size()) {
            throw new IllegalArgumentException("No specie with index '" + specieIndex + "' (should be in [1, " + species.size() + "])" );
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for (final Specie specie : species) {
            builder.append(specie.getName());
            builder.append(": ");
            builder.append(specie.getHeadcount());
            if (species.indexOf(specie) < species.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(" ]");
        return builder.toString();
    }

    @Override
    public double[] toArrayOfFractions() {
        double[] results = new double[getSpeciesCount()];
        int index = 0;
        for (Specie specie : species) {
            results[index] = specie.getFraction();
            index += 1;
        }
        return results;
    }

    @Override
    public double getMeanHeadcount() {
        return ((double) getTotalHeadcount()) / getSpeciesCount();
    }

    @Override
    public PopulationBuilder deepCopy() {
        final PopulationBuilder builder = aPopulation()
                .withSpeciesNamed(getSpeciesNames())
                .withDistribution(getDistribution());
        for(Constraint constraint: constraints) {
            constraint.activateOn(builder);
        }
        return builder;
    }

    @Override
    public List<String> getSpeciesNames() {
        ArrayList<String> speciesName = new ArrayList<String>();
        for (Specie specie : species) {
            speciesName.add(specie.getName());
        }
        return Collections.unmodifiableList(speciesName);
    }

    @Override
    public List<Integer> getDistribution() {
        ArrayList<Integer> distribution = new ArrayList<Integer>();
        for (Specie specie : species) {
            distribution.add(specie.getHeadcount());
        }
        return Collections.unmodifiableList(distribution);
    }

    @Override
    public double getVariance() {
        int s = getSpeciesCount();
        double mu = getMeanHeadcount();
        double total = 0D;
        for (Specie specie : species) {
            total += Math.pow(specie.getHeadcount() - mu, 2);
        }
        return total / s;
    }

    @Override
    public boolean isUniformlyDistributed() {
        return getVariance() == 0;
    }

    @Override
    public List<Action> differenceWith(Population target) {
        final ArrayList<Action> actions = new ArrayList<Action>();
        final List<String> superfluousSpecies = new ArrayList<String>(getSpeciesNames());
        for (Specie foreign : target) {
            if (this.hasAnySpecieNamed(foreign.getName())) {
                int delta = foreign.getHeadcount() - this.getSpecie(foreign.getName()).getHeadcount();
                if (delta != 0) {
                    actions.add(new ShiftNumberOfIndividualsIn(foreign.getName(), delta));
                }
            }
            else {
                actions.add(new AddSpecie(foreign.getName()));
                actions.add(new ShiftNumberOfIndividualsIn(foreign.getName(), foreign.getHeadcount()));
            }
            superfluousSpecies.remove(foreign.getName());
        }
        for (String specie : superfluousSpecies) {
            actions.add(new RemoveSpecie(specie));
        }
        return actions;
    }

    @Override
    public List<String> sortSpeciesNamesAlphabetically() {
        final List<String> speciesNames = new ArrayList<String>(getSpeciesNames());
        Collections.sort(speciesNames);
        return speciesNames;
    }

    @Override
    public Map<String, Integer> toMap() {
        final HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (final Specie specie : species) {
            map.put(specie.getName(), specie.getHeadcount());
        }
        return map;
    }

    @Override
    public Iterator<Specie> iterator() {
        return species.iterator();
    }
}
