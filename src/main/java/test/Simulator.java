package test;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.MyEngine;

import java.util.Scanner;

/* Command-line type User Interface */

// Simulation time
// Delay time
// simulation speed
// parameter for strategy of cashier
// Comma separated file on excel at the end
// create graphics on JavaFX


// check pause and restart button

public class Simulator {
    public static void main(String[] args) {
        boolean startSimulation = true;
        boolean pauseSimulation = false;
        boolean stopSimulation = false;
        boolean resumeSimulation = false;

        Trace.setTraceLevel(Level.INFO);
        MyEngine engine = new MyEngine();
        engine.setAssignByQueueLength(true);
        engine.setSimulationTime(1000);
        engine.setDelayTime(0); // Example delay time
//		engine.adjustStudentArrival(false); // Increase student arrival rate by 10%
//		engine.adjustFoodLineServiceSpeed(false); // Decrease food line service speed by 10%
//		engine.adjustCashierServiceSpeed(true); // Increase cashier service speed by 10%
        engine.adjustStimulationSpeed(false);
//		engine.run();

        if (startSimulation) {
            engine.startSimulation();
        } else if (pauseSimulation) {
            engine.pauseSimulation();
        } else if (resumeSimulation) {
            engine.resumeSimulation();
        } else {
            engine.stopSimulation();
        }
    }
}
