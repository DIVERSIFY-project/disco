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
package eu.diversify.disco.cloudml;

import eu.diversify.disco.cloudml.transformations.BidirectionalTransformation;
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

    private final DiversityMetric metric;
    private final Controller controller;
    private final BidirectionalTransformation transformation;
    private String fileName;
    private double reference;
    private DeploymentModel deployment;
    private ArrayList<ControllerListener> listeners;

    /**
     * Create a new diversity controllers which ensure a given diversity level
     *
     * @param reference the diversity level to maintain
     */
    public DiversityController(double reference) {
        this.reference = reference;
        this.metric = new TrueDiversity().normalise();
        this.controller = new AdaptiveHillClimber();
        this.transformation = new BidirectionalTransformation();
        this.listeners = new ArrayList<ControllerListener>();
    }
    
    public void addListener(ControllerListener listener) {
        this.listeners.add(listener);
    }

    // FIXME: to be tear down
    public Solution applyTo(DeploymentModel current) {
        Population population = transformation.toPopulation(current);
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(population)
                .withDiversityMetric(this.metric)
                .withReferenceDiversity(this.reference)
                .make();
        Solution solution = this.controller.applyTo(problem);
        publishSolution(solution);
        transformation.toCloudML(current, solution.getPopulation());
        return solution;
    }

    private void doControl() {
        Population population = transformation.toPopulation(deployment);
        Problem problem = new ProblemBuilder()
                .withInitialPopulation(population)
                .withDiversityMetric(this.metric)
                .withReferenceDiversity(this.reference)
                .make();
        Solution solution = this.controller.applyTo(problem);
        publishSolution(solution);
        transformation.toCloudML(deployment, solution.getPopulation());
    }

    public void loadDeployment(String fileName) throws FileNotFoundException {
        JsonCodec jsonCodec = new JsonCodec();
        this.fileName = fileName;
        deployment = (DeploymentModel) jsonCodec.load(new FileInputStream(fileName));
        updateDotFile();
    }

    public void setDiversity(double setPoint) throws FileNotFoundException {
        this.reference = setPoint;
        doControl();
        updateDotFile();
    }

    void saveDeploymentAs(String temporaryDotFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateDotFile() throws FileNotFoundException {
        new DotPrinter().saveAs(deployment, getFileNameWithExtension(".dot"));
        updatePngFile();
    }

    public void updatePngFile() {
        Runtime runtime = Runtime.getRuntime();
        try {
            final String pngFileName = getFileNameWithExtension(".png");
            final String dotFileName = getFileNameWithExtension(".dot");
            Process process = runtime.exec(
                    String.format(
                    "dot -Tpng %s -o %s", dotFileName, pngFileName));
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
}
