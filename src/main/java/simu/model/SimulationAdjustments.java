package simu.model;

public class SimulationAdjustments {

    // Flags to indicate if adjustments are needed and their direction
    public Boolean adjustStudentArrivalFlag = null;
    public Boolean adjustFoodLineServiceSpeedFlag = null;
    public Boolean adjustCashierServiceSpeedFlag = null;
    public Boolean adjustStimulationSpeedFlag = null;


    private double adjustSpeed(Boolean flag) {
        if (flag == null) {
            return 1.0; // No adjustment
        }

        return flag ? SimulationConstants.SIMULATION_UPPER_SPEED : SimulationConstants.SIMULATION_LOWER_SPEED;
    }

    public double adjustStimulationSpeed() {
        var currentFlag = adjustStimulationSpeedFlag;
        adjustStimulationSpeedFlag = null;
       return  adjustSpeed(currentFlag);
    }

    public double adjustStudentArrival() {
        var currentFlag = adjustStudentArrivalFlag;
        adjustStudentArrivalFlag = null;
        return adjustSpeed(currentFlag);
    }

    public double adjustFoodLineServiceSpeed() {
        var currentFlag = adjustFoodLineServiceSpeedFlag;
        adjustFoodLineServiceSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }

    public double adjustCashierServiceSpeed() {
        var currentFlag = adjustCashierServiceSpeedFlag;
        adjustCashierServiceSpeedFlag = null;
        return  adjustSpeed(currentFlag);
    }



    // Setter methods
    public void setAdjustStudentArrivalFlag(Boolean adjustStudentArrivalFlag) {
        this.adjustStudentArrivalFlag = adjustStudentArrivalFlag;
    }

    public void setAdjustFoodLineServiceSpeedFlag(Boolean adjustFoodLineServiceSpeedFlag) {
        this.adjustFoodLineServiceSpeedFlag = adjustFoodLineServiceSpeedFlag;
    }

    public void setAdjustCashierServiceSpeedFlag(Boolean adjustCashierServiceSpeedFlag) {
        this.adjustCashierServiceSpeedFlag = adjustCashierServiceSpeedFlag;
    }

    public void setAdjustStimulationSpeedFlag(Boolean adjustStimulationSpeedFlag) {
        this.adjustStimulationSpeedFlag = adjustStimulationSpeedFlag;
    }

    }
