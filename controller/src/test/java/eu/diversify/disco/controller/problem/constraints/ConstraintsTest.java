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
package eu.diversify.disco.controller.problem.constraints;

import static eu.diversify.disco.population.PopulationBuilder.*;
import static eu.diversify.disco.controller.problem.ProblemBuilder.*;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import org.junit.runner.RunWith;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.actions.Action;
import eu.diversify.disco.population.actions.AddSpecie;
import eu.diversify.disco.population.actions.RemoveSpecie;
import eu.diversify.disco.population.actions.Script;
import eu.diversify.disco.population.actions.ShiftNumberOfIndividualsIn;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.runners.Parameterized;

/**
 * Test that the constraints are effective in excluding some candidates actions
 *
 */
@RunWith(Parameterized.class)
public class ConstraintsTest extends TestCase {

    public static final boolean LEGAL = true;
    public static final boolean ILLEGAL = false;
    private final String name;
    private final Problem problem;
    private final Action action;
    private final boolean asExpected;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> examples() {
        return Arrays.asList(new Object[][]{
            new Example("s++ / no constraint", withNoConstraint(), addSpecie(), LEGAL).toArray(),
            new Example("s-- / no constraint", withNoConstraint(), removeSpecie(), LEGAL).toArray(),
            new Example("n++ / no constraint", withNoConstraint(), shiftNumberOfIndividualsIn(), LEGAL).toArray(),
            new Example("si <-> sj / no constraint", withNoConstraint(), moveIndividual(), LEGAL).toArray(),
            new Example("s++ / s fixed", withFixedNumberOfSpecies(), addSpecie(), ILLEGAL).toArray(),
            new Example("s-- / s fixed", withFixedNumberOfSpecies(), removeSpecie(), ILLEGAL).toArray(),
            new Example("n++ / s fixed", withFixedNumberOfSpecies(), shiftNumberOfIndividualsIn(), LEGAL).toArray(),
            new Example("si <-> sj / s fixed", withFixedNumberOfSpecies(), moveIndividual(), LEGAL).toArray(),
            new Example("s++ / n fixed", withFixedNumberOfIndividuals(), addSpecie(), LEGAL).toArray(),
            new Example("s-- / n fixed", withFixedNumberOfIndividuals(), removeSpecie(), LEGAL).toArray(),
            new Example("n++ / n fixed", withFixedNumberOfIndividuals(), shiftNumberOfIndividualsIn(), ILLEGAL).toArray(),
            new Example("si <-> sj / n fixed", withFixedNumberOfIndividuals(), moveIndividual(), LEGAL).toArray(),
            new Example("s++ / n & s fixed", withBothConstraints(), addSpecie(), ILLEGAL).toArray(),
            new Example("s-- / n & s fixed", withBothConstraints(), removeSpecie(), ILLEGAL).toArray(),
            new Example("n++ / n & s fixed", withBothConstraints(), shiftNumberOfIndividualsIn(), ILLEGAL).toArray(),
            new Example("si <-> sj / n & s fixed", withBothConstraints(), moveIndividual(), LEGAL).toArray()});
    }

    public ConstraintsTest(String name, Problem problem, Action action, boolean legal) {
        this.name = name;
        this.problem = problem;
        this.action = action;
        this.asExpected = legal;
    }

    @Test
    public void testConstraintEffectiveness() {
        Solution initialSolution = problem.getInitialEvaluation();
        assertThat("is legal ", initialSolution.canBeRefinedWith(action), is(asExpected));
    }

    public static class Example {

        private final String name;
        private final Problem problem;
        private final Action action;
        private final boolean legal;

        public Example(String name, Problem problem, Action action, boolean isLegal) {
            this.name = name;
            this.problem = problem;
            this.action = action;
            this.legal = isLegal;
        }

        public String getName() {
            return name;
        }

        public Solution getInitialSolution() {
            return problem.getInitialEvaluation();
        }

        public Action getAction() {
            return action;
        }

        public boolean isLegal() {
            return legal;
        }

        public Object[] toArray() {
            return new Object[]{
                name, problem, action, new Boolean(legal)
            };
        }
    }

    private static ProblemBuilder sampleProblem() {
        return aProblem()
                .withDiversityMetric(new TrueDiversity().normalise())
                .withInitialPopulation(aPopulation()
                .withSpeciesNamed("s1", "s2", "s3", "s4")
                .withDistribution(3, 6, 0, 1)
                .build())
                .withReferenceDiversity(0.5);
    }

    private static Problem withNoConstraint() {
        Problem problem = sampleProblem().build();
        return problem;
    }

    private static Problem withFixedNumberOfSpecies() {
        Problem problem = sampleProblem()
                .withFixedNumberOfSpecies()
                .build();
        return problem;
    }

    private static Problem withFixedNumberOfIndividuals() {
        Problem problem = sampleProblem()
                .withFixedTotalNumberOfIndividuals()
                .build();
        return problem;
    }

    private static Problem withBothConstraints() {
        Problem problem = sampleProblem()
                .withFixedNumberOfSpecies()
                .withFixedTotalNumberOfIndividuals()
                .build();
        return problem;
    }

    private static Action addSpecie() {
        return new AddSpecie("s5");
    }

    private static Action removeSpecie() {
        return new RemoveSpecie("s4");
    }

    private static Action shiftNumberOfIndividualsIn() {
        return new ShiftNumberOfIndividualsIn("s3", +8);
    }

    private static Action moveIndividual() {
        return new Script(Arrays.asList(
                new Action[]{
            new ShiftNumberOfIndividualsIn("s1", -1),
            new ShiftNumberOfIndividualsIn("s2", +1)
        }));
    }
}