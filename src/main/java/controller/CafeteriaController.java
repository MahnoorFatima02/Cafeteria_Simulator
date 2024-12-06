package controller;

import simu.framework.Trace;
import simu.utility.SimulationVariables;
import simu.model.MyEngine;
import simu.model.SimulationAdjustments;
import view.CafeteriaGUI;


public class CafeteriaController {
    private MyEngine engine;
    private CafeteriaGUI mainApp;


    public CafeteriaController() {
        this.engine = new MyEngine();
    }

    public void preferenceButtonAction() {
        engine.setAssignByQueueLength(false);
    }

    public void queueLengthButtonAction() {
        engine.setAssignByQueueLength(true);
    }

    public void setMainApp(CafeteriaGUI mainApp) {
        this.mainApp = mainApp;
    }

    public void pauseButtonAction() {
        engine.pauseSimulation();
    }


    public void resumeButtonAction() {
        engine.resumeSimulation();
    }


    public void stopButtonAction() {
        engine.stopSimulation();

    }

    public void startSimulation(String simulationTime, String delayTime) throws InterruptedException {
        engine.setSimulationTime(Double.parseDouble(simulationTime));
        engine.setDelayTime(Double.parseDouble(delayTime));
        Trace.setTraceLevel(Trace.Level.INFO);

        new Thread(() -> {
            engine.startSimulation();
        }).start();

        // Create a Timeline to update the GUI elements periodically
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), event -> {
            if (engine.isRunning() && !engine.isStopped()) {

            }
        }));
        timeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timeline.play();
    }


    public void pauseSimulation() {
        engine.pauseSimulation();
    }

    public void resumeSimulation() {
        engine.resumeSimulation();
    }

    public void stopSimulation() {
        engine.stopSimulation();
    }


    public void lessSimulationSpeed() {
        System.out.println("The lessSimulationSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustStimulationSpeedFlag(false);
    }

    public void moreSimulationSpeed() {
        System.out.println("The moreSimulationSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustStimulationSpeedFlag(true);
    }

    public void lessArrivalRate() {
        System.out.println("The lessArrivalRateAction button has been pressed");
        SimulationAdjustments.setAdjustStudentArrivalFlag(false);
    }

    public void moreArrivalRate() {
        System.out.println("The moreArrivalRateAction button has been pressed");
        SimulationAdjustments.setAdjustStudentArrivalFlag(true);
    }

    public void lessFoodLineSpeed() {
        System.out.println("The lessFoodLineSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(false);
    }

    public void moreFoodLineSpeed() {
        System.out.println("The moreFoodLineSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(true);
    }

    public void lessCashierSpeed() {
        System.out.println("The lessCashierSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(false);
    }

    public void moreCashierSpeed() {
        System.out.println("The moreCashierSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(true);
    }

    public double getSimulationSpeed() {
        return SimulationVariables.DELAY_TIME;
    }


    public double getArrivalRate() {
        return SimulationVariables.ARRIVAL_MEAN;
    }

    public double getFoodLineSpeed() {
        return SimulationVariables.MEAN_NON_VEGAN_SERVICE;
    }

    public double getCashierSpeed() {
        return SimulationVariables.MEAN_CASHIER;
    }

    public int getTotalStudentsServed() {
        return SimulationVariables.TOTAL_CUSTOMERS_SERVED;
    }

    public double getAverageTimeSpent() {
        return SimulationVariables.AVERAGE_TIME_SPENT;
    }

    public double getNormalFoodLineTimeSpent() {
        return SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME;
    }

    public double getVeganFoodLineTimeSpent() {
        return SimulationVariables.AVG_VEGAN_SERVICE_TIME;
    }

    public double getStaffedCashierTimeSpent() {
        return SimulationVariables.AVG_CASHIER_SERVICE_TIME;
    }


    public double getSelfServiceCashierTimeSpent() {
        return SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME;
    }

}


