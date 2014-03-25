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

import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Options {

    private static final String JSON_FILE_NAME = "[^\\.]*\\.json$";
    private static final String DOUBLE_LITERAL = "((\\+|-)?([0-9]+)(\\.[0-9]+)?)|((\\+|-)?\\.?[0-9]+)";
    public static final String ENABLE_GUI = "-gui";

    public static Options fromCommandLineArguments(String... arguments) {
        Options extracted = new Options();
        for (String argument : arguments) {
            if (isJsonFile(argument)) {
                extracted.addDeploymentModel(argument);
            }
            else if (isDouble(argument)) {
                extracted.setReference(Double.parseDouble(argument));
            }
            else if (isEnableGui(argument)) {
                extracted.setGuiEnabled(true);
            }
            else {
                throw new IllegalArgumentException("Unknown argument: " + argument);
            }
        }
        return extracted;
    }

    private static boolean isJsonFile(String argument) {
        return argument.matches(JSON_FILE_NAME);
    }

    private static boolean isDouble(String argument) {
        return argument.matches(DOUBLE_LITERAL);
    }

    private static boolean isEnableGui(String argument) {
        return argument.matches(ENABLE_GUI);
    }
    private boolean guiEnabled;
    private final List<String> deploymentModels;
    private double reference;

    public Options() {
        this.guiEnabled = false;
        this.deploymentModels = new ArrayList<String>();
        this.reference = 0.75;
    }

    public boolean isGuiEnabled() {
        return guiEnabled;
    }

    public void setGuiEnabled(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    public double getReference() {
        return reference;
    }

    public void setReference(double setPoint) {
        this.reference = setPoint;
    }

    public List<String> getDeploymentModels() {
        return Collections.unmodifiableList(deploymentModels);
    }

    public void addDeploymentModel(String pathToModel) {
        this.deploymentModels.add(pathToModel);
    }

    public void launchDiversityController() {
        final CloudMLController controller = new CloudMLController(new TrueDiversity().normalise());
        if (guiEnabled) {
            startGui(controller);
        }
        else {
            startCommandLine(controller);
        }
    }

    private void startGui(final CloudMLController controller) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final Gui gui = new Gui(controller);
                gui.setReference(reference);
                gui.setFileToLoad(deploymentModels.get(0));
                gui.setVisible(true);
            }
        });
    }

    private void startCommandLine(final CloudMLController controller) {
        final CommandLine commandLine = new CommandLine(controller);
        for (String deployment : getDeploymentModels()) {
            commandLine.controlDiversity(deployment, reference);
        }
    }
}
