package view;

public interface CafeteriaView {
    void setButtonsDisabled(boolean disabled);

    void showMessage(String message);

    String getSimulationTime();

    String getDelayTime();

    boolean isPreferenceButtonDisabled();

    boolean isQueueLengthButtonDisabled();

    void loadScene(String fxmlFile) throws Exception;
}