package test;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.MyEngine;
import simu.model.SimulationAdjustments;
import simu.model.SimulationConstants;

import java.util.Scanner;

/* Command-line type User Interface */

// Simulation time
// Delay time
// simulation speed
// parameter for strategy of cashier
// Comma separated file on Excel at the end
// create graphics on JavaFX
// getters for simulation speed
// getter for arrival rate
        // getter for speed


// check pause and restart button

public class Simulator {
    public static void main(String[] args) {

        Trace.setTraceLevel(Level.INFO);
        MyEngine engine = new MyEngine();

        // set Assign queue length preference,
        engine.setAssignByQueueLength(true);
        // get the Assign value
        engine.isAssignByQueueLength();

        // setting simulation time
        engine.setSimulationTime(1000);

        // set delay
        engine.setDelayTime(0);


        // Set the flags for simulation adjustment
        SimulationAdjustments.setAdjustStimulationSpeedFlag(false);
        SimulationAdjustments.setAdjustStudentArrivalFlag(false);
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(false);
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(true);

        // Get flags for simulation setting
        SimulationAdjustments.getAdjustCashierServiceSpeedFlag();
        SimulationAdjustments.getAdjustFoodLineServiceSpeedFlag();
        SimulationAdjustments.getAdjustStimulationSpeedFlag();
        SimulationAdjustments.getAdjustStudentArrivalFlag();

        // Get current parameters
        double simulationSpeed = SimulationConstants.getSimulationSpeed();
        double foodServiceSpeed = SimulationConstants.getFoodServiceSpeed();
        double cashierServiceSpeed = SimulationConstants.getCashierServiceSpeed();
        double arrivalSpeed = SimulationConstants.getArrivalSpeed();

        System.out.println("Current simulation speed: " + simulationSpeed);
        System.out.println("Current food service speed: " + foodServiceSpeed);
        System.out.println("Current cashier service speed: " + cashierServiceSpeed);
        System.out.println("Current arrival speed: " + arrivalSpeed);

        // Output Results
        int totalStudentsServed = engine.totalCustomersServed;
        double averageTimeSpent = engine.averageTimeSpent;
        double averageNonVeganServiceTime = engine.avgNonVeganServiceTime;
        double averageVeganServiceTime = engine.avgVeganServiceTime;
        double averageCashierTime = engine.avgCashierServiceTime;
        double averageSelfCheckoutTime = engine.avgSelfCheckoutServiceTime;

        
        // Toggle simulation flags of start, resume, pause and stop using getter and setter methods
        engine.setRunning(true);
        engine.setPaused(false);
        engine.setStopped(false);


        if (engine.isRunning() && !engine.isPaused()) {
            engine.startSimulation();
        } else if (engine.isPaused()) {
            engine.resumeSimulation();
        } else {
            engine.stopSimulation();
        }
    }
}
