package simu.model;

import simu.utility.ConstantsEnum;
import simu.utility.SimulationVariables;


/**
 * The {@code SimulationAdjustments} class provides methods to dynamically adjust various simulation parameters.
 * It includes flags that can be set to adjust the speed of different aspects of the simulation.
 */
public class SimulationAdjustments {

    // Flags that may change during the simulation to adjust various parameters dynamically.
    private static Boolean adjustStudentArrivalFlag = null;
    private static Boolean adjustFoodLineServiceSpeedFlag = null;
    private static Boolean adjustCashierServiceSpeedFlag = null;
    private static Boolean adjustStimulationSpeedFlag = null;


    /**
     * Adjusts the speed based on the provided flag.
     *
     * @param flag The flag indicating whether to adjust the speed
     * @return The adjusted speed value
     */
    private static double adjustSpeed(Boolean flag) {
        if (flag == null) {
            return 1.0; // No adjustment
        }

        return flag ? ConstantsEnum.SIMULATION_UPPER_SPEED.getValue() : ConstantsEnum.SIMULATION_LOWER_SPEED.getValue();
    }

    /**
     * Adjusts the stimulation speed based on the current flag.
     *
     * @return The adjusted stimulation speed
     */
    public static double adjustStimulationSpeed() {
        var currentFlag = adjustStimulationSpeedFlag;
        adjustStimulationSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }

    /**
     * Adjusts the student arrival speed based on the current flag.
     *
     * @return The adjusted student arrival speed
     */
    public static double adjustStudentArrival() {
        var currentFlag = adjustStudentArrivalFlag;
        adjustStudentArrivalFlag = null;
        return adjustSpeed(currentFlag);
    }

    /**
     * Adjusts the food line service speed based on the current flag.
     *
     * @return The adjusted food line service speed
     */
    public static double adjustFoodLineServiceSpeed() {
        var currentFlag = adjustFoodLineServiceSpeedFlag;
        adjustFoodLineServiceSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }

    /**
     * Adjusts the cashier service speed based on the current flag.
     *
     * @return The adjusted cashier service speed
     */
    public static double adjustCashierServiceSpeed() {
        var currentFlag = adjustCashierServiceSpeedFlag;
        adjustCashierServiceSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }



    // Setter methods

    /**
     * Sets the flag to adjust student arrival speed.
     *
     * @param adjustStudentArrival The flag to adjust student arrival speed
     */
    public static void setAdjustStudentArrivalFlag(Boolean adjustStudentArrival) {
        adjustStudentArrivalFlag = adjustStudentArrival;
    }


    /**
     * Sets the flag to adjust food line service speed.
     *
     * @param adjustFoodLineServiceSpeed The flag to adjust food line service speed
     */
    public static void setAdjustFoodLineServiceSpeedFlag(Boolean adjustFoodLineServiceSpeed) {
        adjustFoodLineServiceSpeedFlag = adjustFoodLineServiceSpeed;
    }


     /**
     * Sets the flag to adjust cashier service speed.
     *
     * @param adjustCashierServiceSpeed The flag to adjust cashier service speed
     */
    public static void setAdjustCashierServiceSpeedFlag(Boolean adjustCashierServiceSpeed) {
        adjustCashierServiceSpeedFlag = adjustCashierServiceSpeed;
    }

      /**
     * Sets the flag to adjust stimulation speed.
     *
     * @param adjustStimulationSpeed The flag to adjust stimulation speed
     */
    public static void setAdjustStimulationSpeedFlag(Boolean adjustStimulationSpeed) {
        adjustStimulationSpeedFlag = adjustStimulationSpeed;
    }

    // Getter methods

    /**
     * Gets the flag to adjust student arrival speed.
     *
     * @return The flag to adjust student arrival speed
     */
    public static Boolean getAdjustStudentArrivalFlag() {
        return adjustStudentArrivalFlag;
    }

      /**
     * Gets the flag to adjust food line service speed.
     *
     * @return The flag to adjust food line service speed
     */
    public static Boolean getAdjustFoodLineServiceSpeedFlag() {
        return adjustFoodLineServiceSpeedFlag;
    }

     /**
     * Gets the flag to adjust cashier service speed.
     *
     * @return The flag to adjust cashier service speed
     */
    public static Boolean getAdjustCashierServiceSpeedFlag() {
        return adjustCashierServiceSpeedFlag;
    }

      /**
     * Gets the flag to adjust stimulation speed.
     *
     * @return The flag to adjust stimulation speed
     */
    public static Boolean getAdjustStimulationSpeedFlag() {
        return adjustStimulationSpeedFlag;
    }

}