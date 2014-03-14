/*
 */

package eu.diversify.disco.cloudml;

import eu.diversify.disco.controller.problem.Solution;
import java.io.FileNotFoundException;


public class CommandLine implements ControllerListener {
    
    final DiversityController controller;
    
    public CommandLine(DiversityController controller) {
        this.controller = controller;
        controller.addListener(this);
    }
    
    public void controlDiversity(String deployment, double reference) {
            System.out.println("Loading CloudML model from: '" + deployment + "'");
            loadDeployment(deployment);
            adjustDiversity(reference);
    }
    
    @Override
    public void onSolution(Solution solution) {
        System.out.println(solution);
    }
 
    @Override
    public void onVisualisation(DiversityController controller) {
        System.out.println("Results stored in '" + controller.getFileNameWithExtension(".dot"));
        System.out.println("Results stored in '" + controller.getFileNameWithExtension(".png"));
    }

    private void loadDeployment(String deployment) {
        try {
            controller.loadDeployment(deployment);
            
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to load the given deployment model");
            System.out.println("--> " + ex.getMessage());                
        }
    }

    private void adjustDiversity(double reference) {
        try {
            controller.setDiversity(reference);

        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Unable to write visualisations of the resulting model");
            System.out.println("--> " + ex.getMessage());
        }
    }

    
    
}
