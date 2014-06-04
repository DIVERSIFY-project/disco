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
package eu.diversify.disco.cloudml.robustness;

import junit.framework.TestCase;
import org.cloudml.core.Deployment;

import static org.cloudml.core.builders.Commons.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of the robustness calculator
 */
@RunWith(JUnit4.class)
public class RobustnessTest extends TestCase {

    @Test
    public void robustnessOfASingleVMShouldBe100Percent() {
        final Deployment deployment = CloudML.aSingleVM().build();

        final Robustness sut = new Robustness(deployment);
        final double robustness = sut.getExtinctionSequence().getRobustness();

        assertThat(robustness, is(equalTo(100.0)));
    }

    @Test
    public void robustnessOfTwoIndependentVMsShouldBe75Percent() {
        final Deployment deployment = CloudML.twoIndependentVMs().build();

        final Robustness sut = new Robustness(deployment);
        final double robustness = sut.getExtinctionSequence().getRobustness();

        assertThat(robustness, is(equalTo(75.0)));
    }

    @Test
    public void robustnessOfAnAppDeployedOnItsVMShouldBe75Percent() {
        final Deployment deployment = CloudML.anAppOnAVm().build();

        final ExtinctionSequence sequence = new Robustness(deployment).getExtinctionSequence();
        final double robustness = sequence.getRobustness();

        assertThat(sequence.toString(), robustness, is(either(equalTo(75.0)).or(equalTo(50.0))));
    }

}