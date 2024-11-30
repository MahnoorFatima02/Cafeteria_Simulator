package controller;

import simu.Cafeteria;
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

        // Start the simulation in the model
        model.startSimulation();

        // Listen for simulation updates (e.g., progress, metrics)
        trackSimulationProgress();
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
        model.setServiceSpeeds(gui.getServiceSpeeds());
        model.setCheckoutAvailability(gui.isSelfCheckoutAvailable(), gui.isCashierCheckoutAvailable());
        model.setSimulationDuration(gui.getSimulationDuration());
        model.loadDataFile(gui.getDataFilePath());
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
                model.getAverageTimePerServicePoint(),
                model.getLongestServicePointTime(),
                model.getPeakQueueLengths()
        );
    }
}