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

public class Individual {

    private final DecentralisedSetup setup;
    private final Position position;
    private int specie;

    public Individual(DecentralisedSetup setup) {
        this.setup = setup;
        this.position = setup.randomPosition();
        this.specie = setup.randomSpecie();
    }

    public Individual(DecentralisedSetup setup, Position position, int specie) {
        this.setup = setup;
        this.position = position;
        this.specie = specie;
    }

    public Position getPosition() {
        return position;
    }

    public int getSpecie() {
        return specie;
    }

    public Individual mutate(Group individuals, double reference) {
        int newSpecie = specie;
        if (setup.mutationShallOccurs()) {
            Group neighbours = individuals.around(this.position, setup.getNeighbourhoodRadius());
            newSpecie = neighbours.mostRelevantSpecieFor(setup, reference);
        }
        return new Individual(setup, position, newSpecie);
    }

    public double distanceTo(Position position) {
        return this.position.distanceTo(position);
    }
}
