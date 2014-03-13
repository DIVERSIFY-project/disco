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

import eu.diversify.disco.cloudml.util.ToolBox;
import java.util.List;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


public class HasNBindingOfType  extends TypeSafeMatcher<DeploymentModel> {

    private final int count;
    private final String bindingTypeName;

    public HasNBindingOfType(int count, String artefactTypeName) {
        super();
        this.count = count;
        this.bindingTypeName = artefactTypeName;
    }    
    
    
    @Override
    protected boolean matchesSafely(DeploymentModel deployment) {
        Binding type = deployment.findBindingByName(bindingTypeName);
        List<BindingInstance> instances = ToolBox.findBindingInstancesByType(deployment, type);
        return instances.size() == count;
    }

    @Override
    public void describeTo(Description d) {
        d.appendText("Number of binding instance of type ")
                .appendValue(bindingTypeName)
                .appendText("expected: ")
                .appendValue(count);
    }

}