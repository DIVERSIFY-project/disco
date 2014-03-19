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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.ToCloudML;
import eu.diversify.disco.cloudml.transformations.ToPopulation;
import eu.diversify.disco.cloudml.util.DotPrinter;
import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.Controller;
import eu.diversify.disco.controller.problem.Problem;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import eu.diversify.disco.controller.problem.Solution;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.diversity.DiversityMetric;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;

/**
 * Wrap the diversity controller and the transformation into a single object
 *
 * @author Franck Chauvel
 * @author Hui Song
 *
 * @since 0.1
 */
public class DiversityController {
    
    public static final String DOT_TO_PNG_COMMAND = "dot -Tpng %s -o %s";
    private ArrayList<ControllerListener> listeners;
    private String fileName;
    private DeploymentModel deployment;
    private Problem problem;

    /**
     * Create a new diversity controllers which ensure a given diversity level
     *
     * @param reference the diversity level to maintain
     */
    public DiversityController() {
        this.listeners = new ArrayList<ControllerListener>();
    }
    
    public void addListener(ControllerListener listener) {
        this.listeners.add(listener);
    }
    
    public Problem getCurrentProblem() {
        if (problem == null) {
            final String message = "Error: a CloudML model shall first be loaded!";
            throw new IllegalStateException(message);
        }
        return this.problem;
    }
    
    public void loadDeployment(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        prepareDefaultProblem(fileName);
        publishSolution(problem.getInitialEvaluation());
        updateDotFile();
        updatePngFile();
    }
    
    private void doControl(double reference) {
        try {
            final Controller controller = new AdaptiveHillClimber();
            prepareProblem(fileName, reference);
            final Solution solution = controller.applyTo(problem);
            publishSolution(solution);
            new ToCloudML().applyTo(deployment, solution.getPopulation());
        } catch (FileNotFoundException ex) {
            publishErrorWhileLoadingModel();
        }
    }
    
    public void setDiversity(double setPoint) throws FileNotFoundException {
        reloadDeployment();
        doControl(setPoint);
        updateDotFile();
        updatePngFile();
    }
    
    private void updateDotFile() throws FileNotFoundException {
        new DotPrinter().saveAs(deployment, getFileNameWithExtension(".dot"));
    }
    
    public void updatePngFile() {
        Runtime runtime = Runtime.getRuntime();
        try {
            final String pngFileName = getFileNameWithExtension(".png");
            final String dotFileName = getFileNameWithExtension(".dot");
            final String command = String.format(DOT_TO_PNG_COMMAND, dotFileName, pngFileName);
            Process process = runtime.exec(command);
            final int errorCode = process.waitFor();
            publishVisualisation();
            
        } catch (IOException ex) {
            publishErrorWhileGeneratingPngVisualisation(ex);
            
        } catch (InterruptedException ex) {
            publishErrorWhileGeneratingPngVisualisation(ex);
            
        }
    }
    
    public String getFileNameWithExtension(String extension) {
        return fileName.replace(".json", extension);
    }
    
    private void publishSolution(Solution solution) {
        for (ControllerListener listener : listeners) {
            listener.onSolution(solution);
        }
    }
    
    private void publishErrorWhileGeneratingPngVisualisation(Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void publishVisualisation() {
        for (ControllerListener listener : listeners) {
            listener.onVisualisation(this);
        }
    }
    
    private void reloadDeployment() {
        try {
            loadDeployment(fileName);
            
        } catch (FileNotFoundException ex) {
            publishErrorWhileLoadingModel();
        }
    }
    
    private void publishErrorWhileLoadingModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void prepareDefaultProblem(String fileName) throws FileNotFoundException {
        JsonCodec jsonCodec = new JsonCodec();
        deployment = (DeploymentModel) jsonCodec.load(new FileInputStream(fileName));
        final DiversityMetric metric = new TrueDiversity().normalise();
        final Population population = new ToPopulation().applyTo(deployment);
        double reference = metric.applyTo(population);
        
        problem = new ProblemBuilder()
                .withInitialPopulation(population)
                .withDiversityMetric(metric)
                .withReferenceDiversity(reference)
                .make();
    }
    
    private void prepareProblem(String fileName, double reference) throws FileNotFoundException {
        JsonCodec jsonCodec = new JsonCodec();
        deployment = (DeploymentModel) jsonCodec.load(new FileInputStream(fileName));
        final DiversityMetric metric = new TrueDiversity().normalise();
        final Population population = new ToPopulation().applyTo(deployment);
        problem = new ProblemBuilder()
                .withInitialPopulation(population)
                .withDiversityMetric(metric)
                .withReferenceDiversity(reference)
                .make();
    }
}
