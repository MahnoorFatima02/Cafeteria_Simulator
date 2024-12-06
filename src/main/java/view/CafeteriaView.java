package view;

public interface CafeteriaView {
    void setButtonsDisabled(boolean disabled);
//    void updateSimulationSpeed(String speed);
//    void updateArrivalRate(String rate);
//    void updateFoodLineSpeed(String speed);
//    void updateCashierSpeed(String speed);
//    void updateTotalStudentsServed(String total);
//    void updateAverageTimeSpent(String time);
//    void updateNormalFoodLineTimeSpent(String time);
//    void updateVeganFoodLineTimeSpent(String time);
//    void updateStaffedCashierTimeSpent(String time);
//    void updateSelfServiceCashierTimeSpent(String time);
    void showMessage(String message);
    String getSimulationTime();
    String getDelayTime();
    boolean isPreferenceButtonDisabled();
    boolean isQueueLengthButtonDisabled();
    void loadScene(String fxmlFile) throws Exception;
//    void stMessageBoxText(String text);
}