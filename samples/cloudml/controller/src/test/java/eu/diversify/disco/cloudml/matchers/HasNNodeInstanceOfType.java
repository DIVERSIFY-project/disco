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
import org.cloudml.core.Deployment;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HasNNodeInstanceOfType extends TypeSafeMatcher<Deployment> {

    private final int count;
    private final String nodeTypeName;

    public HasNNodeInstanceOfType(int count, String artefactTypeName) {
        super();
        this.count = count;
        this.nodeTypeName = artefactTypeName;
    }    
    
    
    @Override
    protected boolean matchesSafely(Deployment deployment) {
        VM type = deployment.getComponents().onlyVMs().firstNamed(nodeTypeName);
        List<VMInstance> instances = deployment.getComponentInstances().onlyVMs().ofType(type).toList();
        return instances.size() == count;
    }

    @Override
    public void describeTo(Description d) {
        d.appendText("Number of node instance of type ")
                .appendValue(nodeTypeName)
                .appendText("expected: ")
                .appendValue(count);
    }

}
