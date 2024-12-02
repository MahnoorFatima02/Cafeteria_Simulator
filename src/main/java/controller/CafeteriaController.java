package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import simu.framework.Trace;
import test.Simulator;
import view.CafeteriaGUI;
import simu.model.MyEngine;
import java.util.List;
import java.util.Arrays;


public class CafeteriaController {
    private CafeteriaGUI mainApp;
    private MyEngine engine;

    @FXML
    private Button lessSimulationSpeed1, moreSimulationSpeed1, lessArrivalRate1, moreArrivalRate1, lessFoodLineSpeed1, moreFoodLineSpeed1, lessCashierSpeed1, moreCashierSpeed1, startButton1, pauseButton1, resumeButton1, preferenceButton1, queueLengthButton1;
    @FXML
    private TextArea simulationTime1, delayTime1;
    @FXML
    private Label messageBox;
    @FXML
    private Label simulationSpeed1;
    @FXML
    private Label arrivalRate1;
    @FXML
    private Label foodLineSpeed1;
    @FXML
    private Label cashierSpeed1;

    public void setMainApp(CafeteriaGUI mainApp) {
        this.mainApp = mainApp;
    }

    public CafeteriaController() {
        this.engine = new MyEngine();
    }

    private void setButtonsDisabled(boolean disabled) {
        lessSimulationSpeed1.setDisable(disabled);
        moreSimulationSpeed1.setDisable(disabled);
        lessArrivalRate1.setDisable(disabled);
        moreArrivalRate1.setDisable(disabled);
        lessFoodLineSpeed1.setDisable(disabled);
        moreFoodLineSpeed1.setDisable(disabled);
        lessCashierSpeed1.setDisable(disabled);
        moreCashierSpeed1.setDisable(disabled);
        startButton1.setDisable(disabled);
        pauseButton1.setDisable(disabled);
        resumeButton1.setDisable(disabled);
        preferenceButton1.setDisable(disabled);
        queueLengthButton1.setDisable(disabled);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (!isInteger(simulationTime1.getText())) {
            messageBox.setText("Simulation Time must be an integer.");
            valid = false;
        } else if (!isInteger(delayTime1.getText())) {
            messageBox.setText("Delay Time must be an integer.");
            valid = false;
        } else if (Integer.parseInt(simulationTime1.getText()) < 1 || Integer.parseInt(delayTime1.getText()) < 1) {
            messageBox.setText("Simulation Time and Delay Time must be greater than 0.");
            valid = false;
        } else if (Integer.parseInt(simulationTime1.getText()) < Integer.parseInt(delayTime1.getText())) {
            messageBox.setText("Simulation Time must be greater than Delay Time.");
            valid = false;
        } else if (!preferenceButton1.isDisable() && !queueLengthButton1.isDisable()) {
            messageBox.setText("Please select Choosing Type.");
            valid = false;
        }
        return valid;
    }

    @FXML
    private void preferenceButtonAction() {
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(false);
        resumeButton1.setDisable(true);
        checkStartConditions();
    }

    @FXML
    private void queueLengthButtonAction() {
        queueLengthButton1.setDisable(true);
        preferenceButton1.setDisable(false);
        resumeButton1.setDisable(true);
        checkStartConditions();
    }

    private void checkStartConditions() {
        if (validateInputs()) {
            startButton1.setDisable(false);
            messageBox.setText("Press START to begin the simulation.");
        }
    }

    @FXML
    private void startButtonAction() {
        if (validateInputs()) {
            setButtonsDisabled(false);
            startButton1.setDisable(true);
            preferenceButton1.setDisable(true);
            queueLengthButton1.setDisable(true);
            pauseButton1.setDisable(false);
            resumeButton1.setDisable(true);
            messageBox.setText("Simulation started. Use PAUSE, RESUME, and RESTART as needed.");


            engine.setAssignByQueueLength(true);
            engine.setSimulationTime(Double.parseDouble(simulationTime1.getText()));
            engine.setDelayTime(Double.parseDouble(delayTime1.getText()));
            Trace.setTraceLevel(Trace.Level.INFO);
            setPrintoutsToView();

            new Thread(() -> {
                engine.startSimulation();

                Platform.runLater(() -> {
                    messageBox.setText("Simulation finished.");
                    startButton1.setDisable(false);
                    pauseButton1.setDisable(true);
                    resumeButton1.setDisable(true);
                });
            }).start();
        }

        }


    @FXML
    private void pauseButtonAction() {
        pauseButton1.setDisable(true);
        resumeButton1.setDisable(false);
        preferenceButton1.setDisable(false);
        queueLengthButton1.setDisable(false);
        messageBox.setText("Simulation paused. Press RESUME to continue.");
        engine.pauseSimulation();
    }

    @FXML
    private void resumeButtonAction() {
        resumeButton1.setDisable(true);
        pauseButton1.setDisable(false);
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(true);
        messageBox.setText("Simulation resumed.");
        engine.resumeSimulation();
    }



    public void initialStartButtonAction() {
        System.out.println("The initialStartButton has been pressed");
        try {
            mainApp.loadScene("/simulationpage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lessSimulationSpeedAction() {
        System.out.println("The lessSimulationSpeedAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void moreSimulationSpeedAction() {
        System.out.println("The moreSimulationSpeedAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void lessArrivalRateAction() {
        System.out.println("The lessArrivalRateAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void moreArrivalRateAction() {
        System.out.println("The moreArrivalRateAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void lessFoodLineSpeedAction() {
        System.out.println("The lessFoodLineSpeedAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void moreFoodLineSpeedAction() {
        System.out.println("The moreFoodLineSpeedAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void lessCashierSpeedAction() {
        System.out.println("The lessCashierSpeedAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }

    public void moreCashierSpeedAction() {
        System.out.println("The moreCashierSpeedAction button has been pressed");
        getViewPrintouts();
        setPrintoutsToView();
    }


    public List<Double> getViewPrintouts() {
       // return Arrays.asList(
                // these need to be made in the model myEngine, now test used
               // engine.getSimulationSpeed(),
               // engine.getArrivalRate(),
               // engine.getFoodLineSpeed(),
               // engine.getCashierSpeed()
        //);
        // test
        return Arrays.asList(0.0, 1.1, 2.2, 3.3);
    }

    public void setPrintoutsToView() {
        List<Double> printouts = getViewPrintouts();
        simulationSpeed1.setText(String.valueOf(printouts.get(0)));
        arrivalRate1.setText(String.valueOf(printouts.get(1)));
        foodLineSpeed1.setText(String.valueOf(printouts.get(2)));
        cashierSpeed1.setText(String.valueOf(printouts.get(3)));
    }


}