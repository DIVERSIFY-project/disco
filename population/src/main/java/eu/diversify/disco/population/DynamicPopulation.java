/*
 */
package eu.diversify.disco.population;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class DynamicPopulation {

    private final HashMap<String, Integer> numberOfIndividualsPerSpecie;

    public DynamicPopulation() {
        this.numberOfIndividualsPerSpecie = new HashMap<String, Integer>();
    }

    public int getTotalNumberOfIndividuals() {
        int total = 0;
        for (int numberOfIndividuals : this.numberOfIndividualsPerSpecie.values()) {
            total += numberOfIndividuals;
        }
        return total;
    }

    public int getNumberOfSpecies() {
        return this.numberOfIndividualsPerSpecie.size();
    }

    public int getNumberOfIndividualsIn(String specie) {
        return this.numberOfIndividualsPerSpecie.get(specie);
    }

    public void addIndividualIn(String specie) {
        int count = 0;
        if (this.numberOfIndividualsPerSpecie.containsKey(specie)) {
            count = this.numberOfIndividualsPerSpecie.get(specie);
        }
        this.numberOfIndividualsPerSpecie.put(specie, count + 1);
    }

    public void addIndividualIn(Set<String> species) {
        for (String specie : species) {
            addIndividualIn(specie);
        }
    }

   public void removeIndividualFrom(String specie) {
        if (this.numberOfIndividualsPerSpecie.containsKey(specie)) {
            int count = this.numberOfIndividualsPerSpecie.get(specie);
            this.numberOfIndividualsPerSpecie.put(specie, count - 1);
        }
    }

   public void removeIndividualFrom(Set<String> species) {
        for (String specie : species) {
            removeIndividualFrom(specie);
        }
    }
}
