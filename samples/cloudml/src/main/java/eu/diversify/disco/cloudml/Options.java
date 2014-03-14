/*
 */
package eu.diversify.disco.cloudml;

import java.io.FileNotFoundException;
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
        final DiversityController controller = new DiversityController();
        if (guiEnabled) {
            startGui(this, controller);

        }
        else {
            startCommandLine(this, controller);
        }
    }

    private void startGui(final Options options, final DiversityController controller) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final Gui gui = new Gui(controller);
                gui.setVisible(true);
            }
        });
    }

    private void startCommandLine(final Options options, final DiversityController controller) {
        final CommandLine commandLine = new CommandLine(controller);
        for (String deployment : options.getDeploymentModels()) {
            commandLine.controlDiversity(deployment, options.getReference());
        }
    }
}
