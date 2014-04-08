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
