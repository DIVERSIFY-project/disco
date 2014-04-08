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
/*
 */
package eu.diversify.disco.experiments.controllers.decentralised;

import eu.diversify.disco.population.ConcretePopulation;
import static eu.diversify.disco.population.PopulationBuilder.*;
import eu.diversify.disco.population.Population;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group implements Iterable<Individual> {

    private final ArrayList<Individual> individuals;

    public Group() {
        this.individuals = new ArrayList<Individual>();
    }

    public void add(Individual newComer) {
        this.individuals.add(newComer);
    }

    public Individual get(int index) {
        return individuals.get(index);
    }

    @Override
    public Iterator<Individual> iterator() {
        return this.individuals.iterator();
    }

    public void replaceWith(Group other) {
        this.individuals.clear();
        this.individuals.addAll(other.individuals);
    }

    public Group around(Position position, double radius) {
        Group selected = new Group();
        for (Individual i : individuals) {
            if (i.distanceTo(position) <= radius) {
                selected.add(i);
            }
        }
        return selected;
    }

    public int mostRelevantSpecieFor(DecentralisedSetup setup, double reference) {
        final Population population = asPopulation();
        Attempt best = new NoAttemptYet();
        for (String specieName : population.getSpeciesNames()) {
            Attempt attempt = new Attempt(setup, reference, population, specieName);
            best = attempt.onlyIfBetterThan(best);
        }
        return best.getSpecieCode();
    }

    public Population asPopulation() {
        final Population population = aPopulation().build();
        for (final Individual i : individuals) {
            final String specie = makeSpecieName(i);
            if (!population.hasAnySpecieNamed(specie)) {
                population.addSpecie(specie);
            }
            population.shiftNumberOfIndividualsIn(specie, +1);
        }
        return aPopulation().immutable().clonedFrom(population).build();
    }

    private String makeSpecieName(final Individual i) {
        return String.format(ConcretePopulation.DEFAULT_SPECIE_NAME_FORMAT, i.getSpecie());
    }

    private static class Attempt {

        private final String specieName;
        private final double error;

        public Attempt(DecentralisedSetup setup, double reference, Population population, String specieName) {
            this.specieName = specieName;
            Population updated = population.shiftNumberOfIndividualsIn(specieName, +1);
            error = Math.pow(reference - setup.diversityOf(updated), 2);
        }

        public Attempt onlyIfBetterThan(Attempt best) {
            if (isBetterThan(best)) {
                return this;
            }
            else {
                return best;
            }
        }

        private boolean isBetterThan(Attempt best) {
            return error < best.error;
        }

        private int getSpecieCode() {
            Pattern pattern = Pattern.compile(ConcretePopulation.DEFAULT_SPECIE_NAME_REGEX);
            Matcher matcher = pattern.matcher(specieName);
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
            throw new IllegalStateException("Illeformed specie name '" + specieName + "'!");
        }
    }

    private static class NoAttemptYet extends Attempt {

        public NoAttemptYet() {
            super(new DecentralisedSetup(), 0.5, aPopulation().withSpeciesNamed("s1").build(), "s1");
        }

        @Override
        public Attempt onlyIfBetterThan(Attempt best) {
            return best;
        }
    }
}
