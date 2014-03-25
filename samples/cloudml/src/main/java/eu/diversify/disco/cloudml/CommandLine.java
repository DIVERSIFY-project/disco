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

import eu.diversify.disco.population.Population;


public class CommandLine implements ControllerUI {
   
    private final CloudMLController controller;
    private final ModelListener sourceListener;
    private final ModelListener targetListener;
    
    public CommandLine(CloudMLController controller) {
        this.controller = controller;
        this.targetListener = new ModelListener("target");
        this.sourceListener = new ModelListener("source");
        controller.addListener(this);
    }
    
    
    @Override
    public ModelListener getSourceModelListener() {
        return this.sourceListener;
    }
    
    @Override
    public ModelListener getTargetModelListener() {
        return this.targetListener;
    }
    
    public void controlDiversity(String deployment, double reference) {
            controller.load(deployment);
            controller.setReference(reference);
            controller.control();
    }
    
    @Override
    public void onPopulationExtracted(Population description) {
        System.out.println("Extraction of: " + description.toString());
    }

    @Override
    public void onErrorWhileExtractingPopulation() {
        System.out.println("ERROR: Unable to extract the descriptive population!");
    }

    @Override
    public void onPopulationDiversified() {
        System.out.println("Population succesfully diversified!");
    }

    @Override
    public void onErrorWhileControllingDiversity() {
        System.out.println("ERROR: Unable to diversifying the population");
    }

    @Override
    public void onDiversityInjected(Population prescription) {
        System.out.println("Injection of: " + prescription);
    }

    @Override
    public void onErrorWhileInjectingDiversity() {
        System.out.println("ERROR: Unable to inject the diversity!");
    }
    
    
    public class ModelListener implements CloudMLModelListener {

        private String role;
        
        public ModelListener(String role) {
            this.role = role;
        }
                
        @Override
        public void onModelLoaded() {
            System.out.println(role + " model loaded!");
        }

        @Override
        public void onErrorWhileLoadingModel() {
            System.out.println("ERROR: Unable to load the " + role + " model!");
        }

        @Override
        public void onModelSaved() {
            System.out.println(role + " model saved!");
        }

        @Override
        public void onErrorWhileSavingModel() {
            System.out.println("ERROR: Unable to saved the " + role + " model!");
        }

        @Override
        public void onVisualisationUpdate(String pathToVisualisation) {
            System.out.println("View of " + role + " model: " + pathToVisualisation);
        }

        @Override
        public void onErrorWhileGeneratingVisualisation() {
            System.out.println("ERROR: Unable to build visualisation of the " + role + " model!");
        }
        
    }
        
    
}
