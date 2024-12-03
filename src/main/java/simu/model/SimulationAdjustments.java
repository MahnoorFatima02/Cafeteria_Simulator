package simu.model;

public class SimulationAdjustments {

    // Flags that may change during the simulation to adjust various parameters dynamically.
    private static Boolean adjustStudentArrivalFlag = null;
    private static Boolean adjustFoodLineServiceSpeedFlag = null;
    private static Boolean adjustCashierServiceSpeedFlag = null;
    private static Boolean adjustStimulationSpeedFlag = null;


    private static double adjustSpeed(Boolean flag) {
        if (flag == null) {
            return 1.0; // No adjustment
        }

        return flag ? SimulationConstants.SIMULATION_UPPER_SPEED : SimulationConstants.SIMULATION_LOWER_SPEED;
    }

    public static double adjustStimulationSpeed() {
        var currentFlag = adjustStimulationSpeedFlag;
        adjustStimulationSpeedFlag = null;
       return  adjustSpeed(currentFlag);
    }

    public static double adjustStudentArrival() {
        var currentFlag = adjustStudentArrivalFlag;
        adjustStudentArrivalFlag = null;
        return adjustSpeed(currentFlag);
    }

    public static double adjustFoodLineServiceSpeed() {
        var currentFlag = adjustFoodLineServiceSpeedFlag;
        adjustFoodLineServiceSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }

    public static double adjustCashierServiceSpeed() {
        var currentFlag = adjustCashierServiceSpeedFlag;
        adjustCashierServiceSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }



    // Setter methods
    public static void setAdjustStudentArrivalFlag(Boolean adjustStudentArrival) {
        adjustStudentArrivalFlag = adjustStudentArrival;
    }

    public static void setAdjustFoodLineServiceSpeedFlag(Boolean adjustFoodLineServiceSpeed) {
        adjustFoodLineServiceSpeedFlag = adjustFoodLineServiceSpeed;
    }

    public static void setAdjustCashierServiceSpeedFlag(Boolean adjustCashierServiceSpeed) {
        adjustCashierServiceSpeedFlag = adjustCashierServiceSpeed;
    }

    public static void setAdjustStimulationSpeedFlag(Boolean adjustStimulationSpeed) {
        adjustStimulationSpeedFlag = adjustStimulationSpeed;
    }

    // make the getters

    public static Boolean getAdjustStudentArrivalFlag() {
        return adjustStudentArrivalFlag;
    }

    public static Boolean getAdjustFoodLineServiceSpeedFlag() {
        return adjustFoodLineServiceSpeedFlag;
    }

    public static Boolean getAdjustCashierServiceSpeedFlag() {
        return adjustCashierServiceSpeedFlag;
    }

    public static Boolean getAdjustStimulationSpeedFlag() {
        return adjustStimulationSpeedFlag;
    }


    }
