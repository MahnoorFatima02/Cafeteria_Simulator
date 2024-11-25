package controller;

import model.Cafeteria;
import view.CafeteriaGUI;

public class CafeteriaController {

    private Cafeteria model;
    private CafeteriaGUI gui;

    // Constructor to initialize the controller
    public CafeteriaController(Cafeteria model, CafeteriaGUI gui) {
        this.model = model;
        this.gui = gui;
    }

    // Method to start the simulation
    public void startSimulation() {
        // Validate input parameters from GUI
        if (!validateInputParameters()) {
            gui.displayError("Invalid input parameters. Please check and try again.");
            return;
        }

        // Configure the model based on input parameters from GUI
        configureModel();

        // Starts the simulation in a new thread
        Thread simulationThread = new Thread(() -> {
            model.startSimulation();
            trackSimulationProgress();
        });
        simulationThread.start();
    }


    // Method to Pause simulation
    public void setPaused() {
        model.isPaused(true);
        gui.displayMessage("Simulation paused");
    }

    // Method to Restart simulation
    public void setRestarted() {
        model.isRestarted(true);
        model.isPaused(false);
        gui.displayMessage("Simulation restarted.");
    }



    // Method to validate user input from the GUI
    private boolean validateInputParameters() {
        // Example validation: Ensure arrival rate and duration are set
        double arrivalRate = gui.getArrivalRate();
        int duration = gui.getSimulationDuration();

        return arrivalRate > 0 && duration > 0;
    }

    // Method to configure the model based on user input
    private void configureModel() {
        model.setArrivalRate(gui.getArrivalRate());
        model.setFoodLineSpeed(gui.getFoodLineSpeed());
        model.setCashierSpeed(gui.getCashierSpeed());
        model.setSimulationDuration(gui.getSimulationDuration());
        model.isPaused(false);
        model.isRestarted(false);
        model.loadDataFile(gui.getDataFilePath()); // ??
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
        // Periodically fetch updates from the model
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