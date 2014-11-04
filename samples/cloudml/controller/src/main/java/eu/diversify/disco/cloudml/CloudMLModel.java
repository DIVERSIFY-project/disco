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

import eu.diversify.disco.samples.commons.ModelReader;
import eu.diversify.disco.samples.commons.ModelWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.DotCodec;
import org.cloudml.core.Deployment;

public class CloudMLModel implements ModelReader<Deployment>, ModelWriter<Deployment> {

    private static final String DOT_TO_PNG_COMMAND = "dot -Tpng %s -o %s";
    private String location;
    private Deployment model;
    private final ArrayList<CloudMLModelListener> listeners;

    public CloudMLModel() {
        this.listeners = new ArrayList<CloudMLModelListener>();
    }

    public void addListener(CloudMLModelListener sourceListener) {
        this.listeners.add(sourceListener);
    }

    @Override
    public Deployment read() {
        if (location == null) {
            throw new IllegalStateException("No model has been loaded yet!");
        }
        load(location);
        return model;
    }

    @Override
    public void write(Deployment model) {
        JsonCodec jsonCodec = new JsonCodec();
        try {
            jsonCodec.save(model, new FileOutputStream(getLocation()));

        } catch (FileNotFoundException ex) {
            for (CloudMLModelListener listener : listeners) {
                listener.onErrorWhileSavingModel();
            }
        }
        updateVisualisation(model);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public String getLocationOfDotVisualisation() {
        return getLocationWithExtension(".dot");
    }

    public String getLocationOfPngVisualisation() {
        return getLocationWithExtension(".png");
    }

    public void load() {
        if (!hasLocation()) {
            throw new IllegalStateException("No location given yet!");
        }
        JsonCodec jsonCodec = new JsonCodec();
        try {
            model = (Deployment) jsonCodec.load(new FileInputStream(getLocation()));
            write(model);

        } catch (FileNotFoundException ex) {
            for (CloudMLModelListener listener : listeners) {
                listener.onErrorWhileLoadingModel();
            }
        }
    }

    public void load(String pathToDeployment) {
        setLocation(pathToDeployment);
        load();
    }

    private String getLocationWithExtension(String extension) {
        return location.replace(".json", extension);
    }

    private void updateVisualisation(Deployment model) {
        try {
            new DotCodec().save(model, new FileOutputStream(getLocationOfDotVisualisation()));

        } catch (FileNotFoundException ex) {
            for (CloudMLModelListener listener : listeners) {
                listener.onErrorWhileGeneratingVisualisation();
            }
        }

        Runtime runtime = Runtime.getRuntime();
        try {
            final String pngFileName = getLocationOfPngVisualisation();
            final String dotFileName = getLocationOfDotVisualisation();
            final String command = String.format(DOT_TO_PNG_COMMAND, dotFileName, pngFileName);
            Process process = runtime.exec(command);
            final int errorCode = process.waitFor();
            for (CloudMLModelListener listener : listeners) {
                listener.onVisualisationUpdate(pngFileName);
            }

        } catch (IOException ex) {
            for (CloudMLModelListener listener : listeners) {
                listener.onErrorWhileGeneratingVisualisation();
            }
        } catch (InterruptedException ex) {
            for (CloudMLModelListener listener : listeners) {
                listener.onErrorWhileGeneratingVisualisation();
            }
        }
    }
}
