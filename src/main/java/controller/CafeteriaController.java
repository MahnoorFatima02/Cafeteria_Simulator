package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import model.Cafeteria;
import view.CafeteriaGUI;

public class CafeteriaController {

    private Cafeteria model;
    private CafeteriaGUI gui;
    @FXML
    private Button startButton;
    @FXML
    private TextArea simulationTime;
    @FXML
    private TextArea delayTime;


    public CafeteriaController(Cafeteria model, CafeteriaGUI gui) {
        this.model = model;
        this.gui = gui;
    }


    // Method to start the simulation
    @FXML
    public void startButton() {
        String simulationTimeValue = simulationTime.getText();
        String delayTimeValue = delayTime.getText();


        // validate input
        if (!validateInput(simulationTimeValue, delayTimeValue)) {
            showAlert("Invalid input, please enter positive numbers for Simulation Time and Delay Time");
            return;
        }

        double simTime = Double.parseDouble(simulationTimeValue);
        double delay = Double.parseDouble(delayTimeValue);

        model.setSimulationTime(simTime);
        model.setDelayTime(delay);
        model.setIsPaused(false);
        model.setIsResume(false);
        model.setIsRestarted(false);
        startButton.setDisable(true);


        Thread simulationThread = new Thread(() -> {
            try {
                model.startSimulation();
                trackSimulationProgress(); // problems with UI ?
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("An error occurred while running the simulation."));
            } finally {
                Platform.runLater(() -> startButton.setDisable(false));
            }
        });

        simulationThread.setDaemon(true);
        simulationThread.start();
    }


    // Method to validate user input from the GUI
    private boolean validateInput(String simulationTimeValue, String delayTimeValue) {
        if (simulationTimeValue.isEmpty() || delayTimeValue.isEmpty()) {
            return false;
        }

        try {
            double simTime = Double.parseDouble(simulationTimeValue);
            double delay = Double.parseDouble(delayTimeValue);
            if (simTime <= 0 || delay <= 0) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

@FXML
public void pauseButton() {
    if (!model.isPaused()) {

        model.setIsPaused(true);
        showAlert.updateStatus("Simulation Paused");
        pauseButton.setDisable(true);
        resumeButton.setDisable(false);
    }
}




        /*
    // Method to Restart simulation
    public void setRestarted() {
        model.isRestarted(true);
        model.isPaused(false);
        gui.displayMessage("Simulation restarted.");
    }






    // Method to speed up or slow down the simulation
    public void changeParameter(String parameter, boolean increase) {
        double factor = increase ? 1.1 : 0.9;

        switch (parameter) {
            case "simulationSpeed":
                model.setSimulationSpeed(model.getSimulationSpeed() * factor);
                gui.updateSimulationParameters(model.getSimulationSpeed());
                break;

            case "arrivalRate":
                model.setArrivalRate(model.getArrivalRate() * factor);
                gui.updateArrivalRate(model.getArrivalRate());
                break;

            case "foodLineSpeed":
                model.setFoodLineSpeed(model.getFoodLineSpeed() * factor);
                gui.updateFoodLine(model.getFoodLineSpeed());
                break;

            case "cashierSpeed":
                model.setCashierSpeed(model.getCashierSpeed() * factor);
                gui.updateCashier(model.getCashierSpeed());
                break;

            default:
                throw new IllegalArgumentException("Unknown parameter: " + parameter);
        }
    }


    // Method to track progress of the simulation
    private void trackSimulationProgress() {

        while (!model.isSimulationComplete()) {
            gui.updateProgress(model.getCurrentProgress());

            // Display intermediate queue lengths or congestion levels
            gui.updateQueueLengths(model.getQueueLengths());
        }

        // Display final results once simulation is complete
        displaySimulationResults();
    }


    // Method to stop the simulation
    public void stopSimulation() {
        model.stopSimulation();
        gui.displayMessage("Simulation stopped.");
    }


    // Method to display final results
    private void displaySimulationResults() {
        gui.displayResults(
                model.getTotalStudentsServed(),
                model.getAverageTotalTimePerStudent(),
                model.getAverageTimeNormalFoodLine(),
                model.getAverageTimeVeganFoodLine(),
                model.getAverageTimeStaffedCashier(),
                model.getAverageTimeSelfServiceCashier()
        );
    }
}
*/
