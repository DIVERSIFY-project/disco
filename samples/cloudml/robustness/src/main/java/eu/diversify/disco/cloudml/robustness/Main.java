package eu.diversify.disco.cloudml.robustness;

import java.io.IOException;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;

/**
 * Entry point of the robustness calculator
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Copyright (c) 2014 - SINTEF ICT");
        
        Deployment deployment = new CodecsLibrary().load(args[0]);
        ExtinctionSequence sequence = new Robustness(deployment).getExtinctionSequence();
                
        System.out.println(sequence);
        sequence.toCsvFile("extinction_sequence.csv");

        System.out.printf("Robustness: %.2f %%\r\n", sequence.getRobustness());
    }

  
}
