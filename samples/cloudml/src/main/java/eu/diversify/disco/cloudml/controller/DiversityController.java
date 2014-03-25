package eu.diversify.disco.cloudml.controller;

import eu.diversify.disco.controller.AdaptiveHillClimber;
import eu.diversify.disco.controller.Facade;
import eu.diversify.disco.controller.Reference;
import eu.diversify.disco.controller.problem.ProblemBuilder;
import eu.diversify.disco.population.Population;
import eu.diversify.disco.population.PopulationReader;
import eu.diversify.disco.population.PopulationWriter;
import eu.diversify.disco.population.diversity.TrueDiversity;
import java.util.ArrayList;
import java.util.Arrays;

public class DiversityController<T> {

    private final ModelReader<T> source;
    private final DiversityExtraction<T> extraction;
    private final Facade facade;
    private final DiversityInjection<T> injection;
    private final ModelWriter<T> target;
    private final ArrayList<DiversityControllerListener> listeners;

    public DiversityController(ModelReader<T> source, DiversityExtraction<T> extraction, Reference reference, DiversityInjection<T> injection, ModelWriter<T> target) {
        abortIfInvalid(source, extraction, injection, target);
        this.source = source;
        this.extraction = extraction;
        final ProblemBuilder problemBuilder = new ProblemBuilder().withDiversityMetric(new TrueDiversity().normalise());
        this.facade = new Facade(problemBuilder, reference, new Extractor(), new AdaptiveHillClimber(), new Injector());
        this.injection = injection;
        this.target = target;
        this.listeners = new ArrayList<DiversityControllerListener>();
    }

    public void addListeners(DiversityControllerListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    public void control() {
        this.facade.control();
    }

    private void abortIfInvalid(ModelReader<T> source, DiversityExtraction<T> extraction, DiversityInjection<T> injection, ModelWriter<T> target) {
        abortIfNull(source, "model reader");
        abortIfNull(extraction, "diversity extraction");
        abortIfNull(injection, "diversity injection");
        abortIfNull(target, "model writer");
    }

    private void abortIfNull(Object object, String role) {
        if (object == null) {
            final String message = String.format("'null' value cannot be used as %s", role);
            throw new IllegalArgumentException(message);
        }
    }

    private class Extractor implements PopulationReader {

        @Override
        public final Population read() {
            final Population description = extraction.applyTo(source.read());
            for (DiversityControllerListener listener: listeners) {
                listener.onPopulationExtracted(description);
            }
            return description;
        }
    }

    private class Injector implements PopulationWriter {

        @Override
        public final void write(Population population) {
            final T model = injection.applyTo(population, source.read());
            for(DiversityControllerListener listener: listeners) {
                listener.onDiversityInjected(population);
            }
            target.write(model);
        }
    }
    
}
