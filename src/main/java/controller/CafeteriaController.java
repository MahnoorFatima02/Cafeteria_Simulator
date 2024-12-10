package controller;

import simu.framework.Trace;
import simu.utility.SimulationVariables;
import view.CafeteriaGUI;
import simu.model.MyEngine;
import simu.model.SimulationAdjustments;

public class CafeteriaController {
    private CafeteriaGUI mainApp;
    private MyEngine engine;

    public CafeteriaController() {
        this.engine = new MyEngine();
    }
    /*
            ====== Main Functions =======
        */
    public void setMainApp(CafeteriaGUI mainApp) {
        this.mainApp = mainApp;
    }

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
        return engine.selfCashierDeparture;
    }

    public void setSelfCashierDeparture(boolean selfCashierDeparture) {
        engine.selfCashierDeparture = selfCashierDeparture;
    }

    public boolean isCashierDeparture2() {
        return engine.cashierDeparture2;
    }

    public void setCashierDeparture2(boolean cashierDeparture2) {
        engine.cashierDeparture2 = cashierDeparture2;
    }

    public boolean isCashierDeparture1() {
        return engine.cashierDeparture1;
    }

    public void setCashierDeparture1(boolean cashierDeparture1) {
        engine.cashierDeparture1 = cashierDeparture1;
    }

    public boolean isSelfCashierQueueDeparture() {
        return engine.selfCashierQueueDeparture;
    }

    public void setSelfCashierQueueDeparture(boolean selfCashierQueueDeparture) {
        engine.selfCashierQueueDeparture = selfCashierQueueDeparture;
    }

    public boolean isSelfCashierArrival() {
        return engine.selfCashierArrival;
    }

    public void setSelfCashierArrival(boolean selfCashierArrival) {
        engine.selfCashierArrival = selfCashierArrival;
    }

    public boolean isCashierQueueDeparture2() {
        return engine.cashierQueueDeparture2;
    }

    public void setCashierQueueDeparture2(boolean cashierQueueDeparture2) {
        engine.cashierQueueDeparture2 = cashierQueueDeparture2;
    }

    public boolean isCashierArrival2() {
        return engine.cashierArrival2;
    }

    public void setCashierArrival2(boolean cashierArrival2) {
        engine.cashierArrival2 = cashierArrival2;
    }

    public boolean isCashierQueueDeparture1() {
        return engine.cashierQueueDeparture1;
    }

    public void setCashierQueueDeparture1(boolean cashierQueueDeparture1) {
        engine.cashierQueueDeparture1 = cashierQueueDeparture1;
    }

    public boolean isCashierArrival1() {
        return engine.cashierArrival1;
    }

    public void setCashierArrival1(boolean cashierArrival1) {
        engine.cashierArrival1 = cashierArrival1;
    }

    public boolean isNonVeganDeparture2() {
        return engine.nonVeganDeparture2;
    }

    public void setNonVeganDeparture2(boolean nonVeganDeparture2) {
        engine.nonVeganDeparture2 = nonVeganDeparture2;
    }

    public boolean isSelfCashierQueueArrival() {
        return engine.selfCashierQueueArrival;
    }

    public void setSelfCashierQueueArrival(boolean selfCashierQueueArrival) {
        engine.selfCashierQueueArrival = selfCashierQueueArrival;
    }

    public boolean isNonVeganDeparture1() {
        return engine.nonVeganDeparture1;
    }

    public void setNonVeganDeparture1(boolean nonVeganDeparture1) {
        engine.nonVeganDeparture1 = nonVeganDeparture1;
    }

    public boolean isCashierQueueArrival2() {
        return engine.cashierQueueArrival2;
    }

    public void setCashierQueueArrival2(boolean cashierQueueArrival2) {
        engine.cashierQueueArrival2 = cashierQueueArrival2;
    }

    public boolean isCashierQueueArrival1() {
        return engine.cashierQueueArrival1;
    }

    public void setCashierQueueArrival1(boolean cashierQueueArrival1) {
        engine.cashierQueueArrival1 = cashierQueueArrival1;
    }

    public boolean isVeganDeparture() {
        return engine.veganDeparture;
    }

    public void setVeganDeparture(boolean veganDeparture) {
        engine.veganDeparture = veganDeparture;
    }

    public boolean isNonVeganFoodServe2() {
        return engine.nonVeganFoodServe2;
    }

    public void setNonVeganFoodServe2(boolean nonVeganFoodServe2) {
        engine.nonVeganFoodServe2 = nonVeganFoodServe2;
    }

    public boolean isNonVeganQueueDeparture2() {
        return engine.nonVeganQueueDeparture2;
    }

    public void setNonVeganQueueDeparture2(boolean nonVeganQueueDeparture2) {
        engine.nonVeganQueueDeparture2 = nonVeganQueueDeparture2;
    }

    public boolean isNonVeganFoodServe1() {
        return engine.nonVeganFoodServe1;
    }

    public void setNonVeganFoodServe1(boolean nonVeganFoodServe1) {
        engine.nonVeganFoodServe1 = nonVeganFoodServe1;
    }

    public boolean isNonVeganQueueDeparture1() {
        return engine.nonVeganQueueDeparture1;
    }

    public void setNonVeganQueueDeparture1(boolean nonVeganQueueDeparture1) {
        engine.nonVeganQueueDeparture1 = nonVeganQueueDeparture1;
    }

    public boolean isVeganFoodServe() {
        return engine.veganFoodServe;
    }

    public void setVeganFoodServe(boolean veganFoodServe) {
        engine.veganFoodServe = veganFoodServe;
    }

    public boolean isVeganQueueDeparture() {
        return engine.veganQueueDeparture;
    }

    public void setVeganQueueDeparture(boolean veganQueueDeparture) {
        engine.veganQueueDeparture = veganQueueDeparture;
    }

    public boolean isNonVeganQueueArrival2() {
        return engine.nonVeganQueueArrival2;
    }

    public void setNonVeganQueueArrival2(boolean nonVeganQueueArrival2) {
        engine.nonVeganQueueArrival2 = nonVeganQueueArrival2;
    }

    public boolean isNonVeganQueueArrival1() {
        return engine.nonVeganQueueArrival1;
    }

    public void setNonVeganQueueArrival1(boolean nonVeganQueueArrival1) {
        engine.nonVeganQueueArrival1 = nonVeganQueueArrival1;
    }

    public boolean isVeganQueueArrival() {
        return engine.veganQueueArrival;
    }

    public void setVeganQueueArrival(boolean veganQueueArrival) {
        engine.veganQueueArrival = veganQueueArrival;
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