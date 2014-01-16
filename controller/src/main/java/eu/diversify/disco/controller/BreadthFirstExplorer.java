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
package eu.diversify.disco.controller;

import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.Specie;
import eu.diversify.disco.population.diversity.DiversityMetric;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of a breadth-first search strategy.
 *
 * It maintains a set of populations whose neighbours have already by evaluated
 * and a set of populations whose neighbours are unknown, so called the
 * "frontier". At each iteration, we push back the frontier by evaluating all
 * possible neighbours, potentially augmenting the set of explored population.
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class BreadthFirstExplorer extends Controller {

    private final HashSet<Evaluation> explored;
    private final HashSet<Evaluation> frontier;
    private Evaluation best = null;

    public BreadthFirstExplorer(DiversityMetric metric) {
        super(metric);
        this.explored = new HashSet<Evaluation>();
        this.frontier = new HashSet<Evaluation>();
    }

    @Override
    public Evaluation applyTo(Population population, double reference) {
        Case problem = new Case(population, reference, this.metric);

        reset(problem.evaluate(population));

        while (!frontier.isEmpty()) {
            pushback();
        }

        return best;
    }

    public void pushback() {
        final HashSet<Evaluation> newFrontier = new HashSet<Evaluation>();
        for (Evaluation fp : frontier) {
            for (Update update : getLegalUpdates(fp.getPopulation())) {
                final Evaluation next = fp.refineWith(update);
                if (next.getError() < fp.getError()) {
                    if (!this.explored.contains(next)
                            && !this.frontier.contains(next)) {
                        if (next.getError() < best.getError()) {
                            best = next;
                        }
                        newFrontier.add(next);
                    }
                }
            }
            this.explored.add(fp);
        }
        frontier.clear();
        frontier.addAll(newFrontier);
    }

    /**
     * Reset the explorer with an empty set of explored populations, a frontier
     * the given seed as best evaluation found so far and as unique frontier to
     * explore.
     *
     * @param seed the evaluation to start from
     */
    void reset(Evaluation seed) {
        this.best = seed;
        this.explored.clear();
        this.frontier.clear();
        frontier.add(best);
    }

    /**
     * @return the set of population evaluated so far
     */
    HashSet<Evaluation> getExplored() {
        return this.explored;
    }


    /**
     * @return the frontier populations, whose neighbours have not yet been
     * evaluated
     */
    HashSet<Evaluation> getFrontier() {
        return this.frontier;
    }

    @Override
    protected List<Update> getLegalUpdates(Population population) {
        final ArrayList<Update> updates = new ArrayList<Update>();
        for (Specie s1 : population.getSpecies()) {
            if (s1.getIndividualCount() > 0) {
                for (Specie s2 : population.getSpecies()) {
                    if (!s1.getName().equals(s2.getName())) {
                        final Update u = new Update();
                        u.setUpdate(s1.getName(), -1);
                        u.setUpdate(s2.getName(), +1);
                        updates.add(u);
                    }
                }
            }
        }
        return updates;
    }
}
