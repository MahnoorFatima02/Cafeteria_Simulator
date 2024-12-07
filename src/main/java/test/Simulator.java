//
//package test;
//
//import simu.framework.Trace;
//import simu.framework.Trace.Level;
//import simu.model.MyEngine;
//import simu.model.SimulationAdjustments;
//import simu.utility.SimulationVariables;
//
///* Command-line type User Interface */
//// Comma separated file on Excel at the end
//
//
//public class Simulator {
//    public static void main(String[] args) {
//
//        Trace.setTraceLevel(Level.INFO);
//        MyEngine engine = new MyEngine();
//
//        // set Assign queue length preference,
//        engine.setAssignByQueueLength(true);
//        // get the Assign value
//        engine.isAssignByQueueLength();
//
//        // setting simulation time
//        engine.setSimulationTime(1000);
//
//        // set delay
//        engine.setDelayTime(0);
//
//
//        // Set the flags for simulation adjustment
//        SimulationAdjustments.setAdjustStimulationSpeedFlag(false);
//        SimulationAdjustments.setAdjustStudentArrivalFlag(false);
//        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(false);
//        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(true);
//
//        // Get flags for simulation setting
//        SimulationAdjustments.getAdjustCashierServiceSpeedFlag();
//        SimulationAdjustments.getAdjustFoodLineServiceSpeedFlag();
//        SimulationAdjustments.getAdjustStimulationSpeedFlag();
//        SimulationAdjustments.getAdjustStudentArrivalFlag();
//
//        // Get current parameters
//        double simulationSpeed = SimulationVariables.getSimulationSpeed();
//        double foodServiceSpeed = SimulationVariables.getFoodServiceSpeed();
//        double cashierServiceSpeed = SimulationVariables.getCashierServiceSpeed();
//        double arrivalSpeed = SimulationVariables.getArrivalSpeed();
//
//        System.out.println("Current simulation speed: " + simulationSpeed);
//        System.out.println("Current food service speed: " + foodServiceSpeed);
//        System.out.println("Current cashier service speed: " + cashierServiceSpeed);
//        System.out.println("Current arrival speed: " + arrivalSpeed);
//
//        // Output Results
////        int totalStudentsServed = engine.totalCustomersServed;
////        double averageTimeSpent = engine.averageTimeSpent;
////        double averageNonVeganServiceTime = engine.avgNonVeganServiceTime;
////        double averageVeganServiceTime = engine.avgVeganServiceTime;
////        double averageCashierTime = engine.avgCashierServiceTime;
////        double averageSelfCheckoutTime = engine.avgSelfCheckoutServiceTime;
//
//
//        // Toggle simulation flags of start, resume, pause and stop using getter and setter methods
//        engine.setRunning(true);
//        engine.setPaused(false);
//        //engine.setStopped(false);
//
//        // Retrieve and print the customer IDs for simulation
//        System.out.println("Vegan Customer ID: " + engine.getVeganCustomerId());
//        System.out.println("Non-Vegan Customer ID: " + engine.getNonVeganCustomerId());
//        System.out.println("Cashier Customer ID: " + engine.getCashierCustomerId());
//        System.out.println("Self-Checkout Customer ID: " + engine.getSelfCheckoutCustomerId());
//
//
//        // Retrieve and print the queue sizes of people waiting at each queue for simulation
//        System.out.println("Vegan Queue Size: " + engine.getVeganQueueSize());
//        System.out.println("Non-Vegan Queue Sizes: " + engine.getNonVeganQueueSizes());
//        System.out.println("Cashier Queue Sizes: " + engine.getCashierQueueSizes());
//        System.out.println("Self-Checkout Queue Size: " + engine.getSelfCheckoutQueueSize());
//
//
//        if (engine.isRunning() && !engine.isPaused()) {
//            engine.startSimulation();
//        } else if (engine.isPaused()) {
//            engine.resumeSimulation();
//        } else {
//            engine.stopSimulation();
//        }
//    }
//}
