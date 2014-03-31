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

package eu.diversify.disco.cloudml.matchers;

import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HasNArtefactInstanceOfType extends TypeSafeMatcher<DeploymentModel> {

    private final int count;
    private final String artefactTypeName;

    public HasNArtefactInstanceOfType(int count, String artefactTypeName) {
        super();
        this.count = count;
        this.artefactTypeName = artefactTypeName;
    }    
    
    
    @Override
    protected boolean matchesSafely(DeploymentModel deployment) {
        Artefact type = deployment.getArtefactTypes().named(artefactTypeName);
        List<ArtefactInstance> instances = deployment.getArtefactInstances().ofType(type).toList();
        return instances.size() == count;
    }

    @Override
    public void describeTo(Description d) {
        d.appendText("Number of instance of artefact type ")
                .appendValue(artefactTypeName)
                .appendText("expected: ")
                .appendValue(count);
    }

    
    
}
