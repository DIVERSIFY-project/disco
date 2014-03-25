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
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.controller.ConstantReferenceProvider;
import eu.diversify.disco.cloudml.controller.DiversityController;
import eu.diversify.disco.cloudml.transformations.ToCloudML;
import eu.diversify.disco.cloudml.transformations.ToPopulation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.core.DeploymentModel;

public class CloudMLController {

    private final ConstantReferenceProvider reference;
    private final CloudMLModel source;
    private final DiversityController<DeploymentModel> controller;
    private final CloudMLModel target;

    public CloudMLController() {
        reference = new ConstantReferenceProvider();
        source = new CloudMLModel();
        target = new CloudMLModel();
        controller = new DiversityController<DeploymentModel>(source, new ToPopulation(), reference, new ToCloudML(), target);
    }

    public void setReference(double reference) {
        this.reference.setReference(reference);
    }

    public void load(String deployment) {
        source.load(deployment);
        target.setLocation(buildOutputFileName(deployment));
    }

    private String buildOutputFileName(String deployment) {
        final String result;
        final Pattern pattern = Pattern.compile("([^\\.]+)(\\.\\w+)\\s*$");
        final Matcher matcher = pattern.matcher(deployment);
        if (matcher.matches()) {
            final String name = matcher.group(1);
            final String extension = matcher.group(2);
            result = name + "_adjusted" + extension;
        } else {
            throw new IllegalArgumentException("File names must have an extension!");
        }
        return result;
    }

    public void control() {
        controller.control();
    }

    public void addListener(ControllerUI listener) {
        this.source.addListener(listener.getSourceModelListener());
        this.controller.addListeners(listener);
        this.target.addListener(listener.getTargetModelListener());
    }
}
