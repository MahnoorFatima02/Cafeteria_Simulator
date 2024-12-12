package controller;

import simu.framework.Trace;
import simu.utility.SimulationVariables;
import view.CafeteriaGUI;
import simu.model.MyEngine;
import simu.model.SimulationAdjustments;

/**
 * The {@code CafeteriaController} class is responsible for controlling the simulation of the cafeteria.
 * It interacts with the GUI and the simulation engine to manage the simulation flow and update the GUI.
 */
public class CafeteriaController {
    private CafeteriaGUI mainApp;
    private MyEngine engine;

    /**
     * Constructs a new {@code CafeteriaController} and initializes the simulation engine.
     */
    public CafeteriaController() {
        this.engine = new MyEngine();
    }

    /*
            ====== Main Functions =======
        */
    /**
     * Sets the main application GUI.
     *
     * @param mainApp The main application GUI
     */
    public void setMainApp(CafeteriaGUI mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Starts the simulation with the specified simulation time and delay time.
     *
     * @param simulationTime The simulation time
     * @param delayTime The delay time
     * @throws InterruptedException If the simulation thread is interrupted
     */
    public void startSimulation(String simulationTime, String delayTime) throws InterruptedException {
        engine.setSimulationTime(Double.parseDouble(simulationTime));
        engine.setDelayTime(Double.parseDouble(delayTime));
        Trace.setTraceLevel(Trace.Level.INFO);

        new Thread(() -> {
            engine.startSimulation();
        }).start();
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

    public void preferenceButtonAction() {
        engine.setAssignByQueueLength(false);
    }

    public void queueLengthButtonAction() {
        engine.setAssignByQueueLength(true);
    }

    public boolean isEngineRunning() {
        return engine.isRunning();
    }

    public boolean isEngineStopped() {
        return engine.isStopped();
    }

    public boolean isSimulate() {
        return engine.simulate();
    }
    /*
            ====== Main Functions =======
        */

    /*
            ====== Parameters Adjustments Functions =======
        */
    public void lessSimulationSpeedAction() {
        SimulationAdjustments.setAdjustStimulationSpeedFlag(false);
    }

    public void moreSimulationSpeedAction() {
        SimulationAdjustments.setAdjustStimulationSpeedFlag(true);
    }

    public void lessArrivalRateAction() {
        SimulationAdjustments.setAdjustStudentArrivalFlag(false);
    }

    public void moreArrivalRateAction() {
        SimulationAdjustments.setAdjustStudentArrivalFlag(true);
    }

    public void lessFoodLineSpeedAction() {
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(false);
    }

    public void moreFoodLineSpeedAction() {
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(true);
    }

    public void lessCashierSpeedAction() {
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(false);
    }

    public void moreCashierSpeedAction() {
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(true);
    }
    /*
            ====== Parameters Adjustments Functions =======
        */

    /*
            ====== Labels Updating Functions =======
        */
    public boolean isVeganFoodStationReserved() {
        return engine.veganFoodStation.isReserved();
    }

    public int getVeganFoodStationCurrentCustomerID() {
        return engine.veganFoodStation.getCurrentCustomerID();
    }

    public int getVeganFoodStationTotalCustomersRemoved() {
        return engine.veganFoodStation.getTotalCustomersRemoved();
    }

    public boolean isNonVeganFoodStation1Reserved() {
        return engine.nonVeganFoodStation[0].isReserved();
    }

    public int getNonVeganFoodStation1CurrentCustomerID() {
        return engine.nonVeganFoodStation[0].getCurrentCustomerID();
    }

    public int getNonVeganFoodStation1TotalCustomersRemoved() {
        return engine.nonVeganFoodStation[0].getTotalCustomersRemoved();
    }

    public boolean isNonVeganFoodStation2Reserved() {
        return engine.nonVeganFoodStation[1].isReserved();
    }

    public int getNonVeganFoodStation2CurrentCustomerID() {
        return engine.nonVeganFoodStation[1].getCurrentCustomerID();
    }

    public int getNonVeganFoodStation2TotalCustomersRemoved() {
        return engine.nonVeganFoodStation[1].getTotalCustomersRemoved();
    }

    public boolean isCashier1Reserved() {
        return engine.cashierServicePoints[0].isReserved();
    }

    public int getCashier1CurrentCustomerID() {
        return engine.cashierServicePoints[0].getCurrentCustomerID();
    }

    public int getCashier1TotalCustomersRemoved() {
        return engine.cashierServicePoints[0].getTotalCustomersRemoved();
    }

    public boolean isCashier2Reserved() {
        return engine.cashierServicePoints[1].isReserved();
    }

    public int getCashier2CurrentCustomerID() {
        return engine.cashierServicePoints[1].getCurrentCustomerID();
    }

    public int getCashier2TotalCustomersRemoved() {
        return engine.cashierServicePoints[1].getTotalCustomersRemoved();
    }

    public boolean isSelfCashierReserved() {
        return engine.selfCheckoutServicePoint.isReserved();
    }

    public int getSelfCashierCurrentCustomerID() {
        return engine.selfCheckoutServicePoint.getCurrentCustomerID();
    }

    public int getSelfCashierTotalCustomersRemoved() {
        return engine.selfCheckoutServicePoint.getTotalCustomersRemoved();
    }

    public int getVeganFoodStationQueueSize() {
        return engine.veganFoodStation.getQueueSize();
    }

    public int getNonVeganFoodStation1QueueSize() {
        return engine.nonVeganFoodStation[0].getQueueSize();
    }

    public int getNonVeganFoodStation2QueueSize() {
        return engine.nonVeganFoodStation[1].getQueueSize();
    }

    public int getNonVeganFoodStationQueueSize() {
        return engine.nonVeganFoodStation[0].getQueueSize() + engine.nonVeganFoodStation[1].getQueueSize();
    }

    public int getCashier1QueueSize() {
        return engine.cashierServicePoints[0].getQueueSize();
    }

    public int getCashier2QueueSize() {
        return engine.cashierServicePoints[1].getQueueSize();
    }

    public int getCashierServicePointQueueSize() {
        return engine.cashierServicePoints[0].getQueueSize() + engine.cashierServicePoints[1].getQueueSize();
    }

    public int getSelfCheckoutServicePointQueueSize() {
        return engine.selfCheckoutServicePoint.getQueueSize();
    }

    public boolean isCashier2Active() {
        return engine.cashierServicePoints[1].isActive();
    }
    /*
            ====== Labels Updating Functions =======
        */

    /*
                ====== Animation Updating Functions =======
            */
    public boolean isSelfCashierDeparture() {
        return SimulationVariables.selfCashierDeparture;
    }

    public void setSelfCashierDeparture(boolean selfCashierDeparture) {
        SimulationVariables.selfCashierDeparture = selfCashierDeparture;
    }

    public boolean isCashierDeparture2() {
        return SimulationVariables.cashierDeparture2;
    }

    public void setCashierDeparture2(boolean cashierDeparture2) {
        SimulationVariables.cashierDeparture2 = cashierDeparture2;
    }

    public boolean isCashierDeparture1() {
        return SimulationVariables.cashierDeparture1;
    }

    public void setCashierDeparture1(boolean cashierDeparture1) {
        SimulationVariables.cashierDeparture1 = cashierDeparture1;
    }

    public boolean isSelfCashierQueueDeparture() {
        return SimulationVariables.selfCashierQueueDeparture;
    }

    public void setSelfCashierQueueDeparture(boolean selfCashierQueueDeparture) {
        SimulationVariables.selfCashierQueueDeparture = selfCashierQueueDeparture;
    }

    public boolean isSelfCashierArrival() {
        return SimulationVariables.selfCashierArrival;
    }

    public void setSelfCashierArrival(boolean selfCashierArrival) {
        SimulationVariables.selfCashierArrival = selfCashierArrival;
    }

    public boolean isCashierQueueDeparture2() {
        return SimulationVariables.cashierQueueDeparture2;
    }

    public void setCashierQueueDeparture2(boolean cashierQueueDeparture2) {
        SimulationVariables.cashierQueueDeparture2 = cashierQueueDeparture2;
    }

    public boolean isCashierArrival2() {
        return SimulationVariables.cashierArrival2;
    }

    public void setCashierArrival2(boolean cashierArrival2) {
        SimulationVariables.cashierArrival2 = cashierArrival2;
    }

    public boolean isCashierQueueDeparture1() {
        return SimulationVariables.cashierQueueDeparture1;
    }

    public void setCashierQueueDeparture1(boolean cashierQueueDeparture1) {
        SimulationVariables.cashierQueueDeparture1 = cashierQueueDeparture1;
    }

    public boolean isCashierArrival1() {
        return SimulationVariables.cashierArrival1;
    }

    public void setCashierArrival1(boolean cashierArrival1) {
        SimulationVariables.cashierArrival1 = cashierArrival1;
    }

    public boolean isNonVeganDeparture2() {
        return SimulationVariables.nonVeganDeparture2;
    }

    public void setNonVeganDeparture2(boolean nonVeganDeparture2) {
        SimulationVariables.nonVeganDeparture2 = nonVeganDeparture2;
    }

    public boolean isSelfCashierQueueArrival() {
        return SimulationVariables.selfCashierQueueArrival;
    }

    public void setSelfCashierQueueArrival(boolean selfCashierQueueArrival) {
        SimulationVariables.selfCashierQueueArrival = selfCashierQueueArrival;
    }

    public boolean isNonVeganDeparture1() {
        return SimulationVariables.nonVeganDeparture1;
    }

    public void setNonVeganDeparture1(boolean nonVeganDeparture1) {
        SimulationVariables.nonVeganDeparture1 = nonVeganDeparture1;
    }

    public boolean isCashierQueueArrival2() {
        return SimulationVariables.cashierQueueArrival2;
    }

    public void setCashierQueueArrival2(boolean cashierQueueArrival2) {
        SimulationVariables.cashierQueueArrival2 = cashierQueueArrival2;
    }

    public boolean isCashierQueueArrival1() {
        return SimulationVariables.cashierQueueArrival1;
    }

    public void setCashierQueueArrival1(boolean cashierQueueArrival1) {
        SimulationVariables.cashierQueueArrival1 = cashierQueueArrival1;
    }

    public boolean isVeganDeparture() {
        return SimulationVariables.veganDeparture;
    }

    public void setVeganDeparture(boolean veganDeparture) {
        SimulationVariables.veganDeparture = veganDeparture;
    }

    public boolean isNonVeganFoodServe2() {
        return SimulationVariables.nonVeganFoodServe2;
    }

    public void setNonVeganFoodServe2(boolean nonVeganFoodServe2) {
        SimulationVariables.nonVeganFoodServe2 = nonVeganFoodServe2;
    }

    public boolean isNonVeganQueueDeparture2() {
        return SimulationVariables.nonVeganQueueDeparture2;
    }

    public void setNonVeganQueueDeparture2(boolean nonVeganQueueDeparture2) {
        SimulationVariables.nonVeganQueueDeparture2 = nonVeganQueueDeparture2;
    }

    public boolean isNonVeganFoodServe1() {
        return SimulationVariables.nonVeganFoodServe1;
    }

    public void setNonVeganFoodServe1(boolean nonVeganFoodServe1) {
        SimulationVariables.nonVeganFoodServe1 = nonVeganFoodServe1;
    }

    public boolean isNonVeganQueueDeparture1() {
        return SimulationVariables.nonVeganQueueDeparture1;
    }

    public void setNonVeganQueueDeparture1(boolean nonVeganQueueDeparture1) {
        SimulationVariables.nonVeganQueueDeparture1 = nonVeganQueueDeparture1;
    }

    public boolean isVeganFoodServe() {
        return SimulationVariables.veganFoodServe;
    }

    public void setVeganFoodServe(boolean veganFoodServe) {
        SimulationVariables.veganFoodServe = veganFoodServe;
    }

    public boolean isVeganQueueDeparture() {
        return SimulationVariables.veganQueueDeparture;
    }

    public void setVeganQueueDeparture(boolean veganQueueDeparture) {
        SimulationVariables.veganQueueDeparture = veganQueueDeparture;
    }

    public boolean isNonVeganQueueArrival2() {
        return SimulationVariables.nonVeganQueueArrival2;
    }

    public void setNonVeganQueueArrival2(boolean nonVeganQueueArrival2) {
        SimulationVariables.nonVeganQueueArrival2 = nonVeganQueueArrival2;
    }

    public boolean isNonVeganQueueArrival1() {
        return SimulationVariables.nonVeganQueueArrival1;
    }

    public void setNonVeganQueueArrival1(boolean nonVeganQueueArrival1) {
        SimulationVariables.nonVeganQueueArrival1 = nonVeganQueueArrival1;
    }

    public boolean isVeganQueueArrival() {
        return SimulationVariables.veganQueueArrival;
    }

    public void setVeganQueueArrival(boolean veganQueueArrival) {
        SimulationVariables.veganQueueArrival = veganQueueArrival;
    }
    /*
                ====== Animation Updating Functions =======
            */


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

    public int getTotalStudentsNotServed() {
        return SimulationVariables.TOTAL_CUSTOMERS_NOT_SERVED;
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

    public double getServiceEfficiency() {
        return SimulationVariables.SERVE_EFFICIENCY;
    }
}