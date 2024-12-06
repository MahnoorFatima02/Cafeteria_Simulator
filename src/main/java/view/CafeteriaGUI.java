package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import controller.CafeteriaController;
import javafx.util.Duration;
import simu.model.SimulationAdjustments;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static org.hibernate.type.descriptor.jdbc.JdbcType.isInteger;


public class CafeteriaGUI extends Application implements CafeteriaView {
    private CafeteriaController cafeController;
    private Stage primaryStage;
    private Timeline timeline;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.cafeController = new CafeteriaController();
        cafeController.setMainApp(this); // If applicable
        loadScene("/mainpage.fxml");
    }

    public void loadScene(String fxmlFile) throws Exception {
        System.out.println("IN LOAD SCENE....");
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
//        fxmlLoader.setController(this); // Set this class as the controller
//        Parent root = fxmlLoader.load(); // Load the FXML file
////        cafeController = fxmlLoader.getController();
////        cafeController = new CafeteriaController();
////        System.out.println("********");
////        System.out.println(cafeController);
////        cafeController.setView(this);
//        primaryStage.setTitle("Cafeteria Simulator");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();


//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
//        Parent root = fxmlLoader.load();
//        cafeController = fxmlLoader.getController();
//        cafeController.setMainApp(this);
//        primaryStage.setTitle("Cafeteria Simulator");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setController(this);
//        fxmlLoader.setController(this); // Set this class as the controller
        Parent root = fxmlLoader.load(); // Load the FXML file
//        cafeController = new CafeteriaController();
//        cafeController.setMainApp(this);
        primaryStage.setTitle("Cafeteria Simulator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



    @FXML
    public void initialize() {
        System.out.println("FXML initialized successfully.");
    }


    @FXML
    private void initialStartButtonAction() {
        System.out.println("The initialStartButton has been pressed");
        try {
            loadScene("/simulationpage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public Button lessSimulationSpeed1, moreSimulationSpeed1, lessArrivalRate1, moreArrivalRate1, lessFoodLineSpeed1, moreFoodLineSpeed1, lessCashierSpeed1, moreCashierSpeed1, startButton1, pauseButton1, resumeButton1, preferenceButton1, queueLengthButton1, stopButton1;
    @FXML
    private TextField simulationTime1, delayTime1;
    @FXML
    private Label messageBox, simulationSpeed1, arrivalRate1, foodLineSpeed1, cashierSpeed1, totalStudentsServed, averageTimeSpent, normalFoodLineTimeSpent, veganFoodLineTimeSpent, staffedCashierTimeSpent, selfServiceCashierTimeSpent;


    @FXML
    private void preferenceButtonAction() {
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(false);
        resumeButton1.setDisable(true);
        cafeController.preferenceButtonAction();
        checkStartConditions();

    }

    @FXML
    private void queueLengthButtonAction() {
        queueLengthButton1.setDisable(true);
        preferenceButton1.setDisable(false);
        resumeButton1.setDisable(true);
        cafeController.queueLengthButtonAction();
        checkStartConditions();
    }

    private void checkStartConditions() {
        if (validateInputs()) {
            startButton1.setDisable(false);
            messageBox.setText("Press START to begin the simulation.");
        }
    }

    @FXML
    private void startButtonAction() throws InterruptedException {
        if (validateInputs()) {
            setButtonsDisabled(false);
            startButton1.setDisable(true);
            preferenceButton1.setDisable(true);
            queueLengthButton1.setDisable(true);
            pauseButton1.setDisable(false);
            stopButton1.setDisable(false);
            resumeButton1.setDisable(true);
            messageBox.setText("Simulation started. Use PAUSE, RESUME, and RESTART as needed.");
            cafeController.startSimulation(getSimulationTime(), getDelayTime());
            startUpdatingView();
        }
    }


    private void startUpdatingView() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateView()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopUpdatingView() {
        if (timeline != null) {
            timeline.stop();
        }
    }


    private void updateView() {

//        updateSimulationSpeed(String.format("%.2f", cafeController.getSimulationSpeed()));
//        updateArrivalRate(String.format("%.2f", cafeController.getArrivalRate()));
//        updateFoodLineSpeed(String.format("%.2f", cafeController.getFoodLineSpeed()));
//        updateCashierSpeed(String.format("%.2f", cafeController.getCashierSpeed()));
//        updateTotalStudentsServed(String.format("%d", cafeController.getTotalStudentsServed()));
//        updateAverageTimeSpent(String.format("%.2f", cafeController.getAverageTimeSpent()));
//        updateNormalFoodLineTimeSpent(String.format("%.2f", cafeController.getNormalFoodLineTimeSpent()));
//        updateVeganFoodLineTimeSpent(String.format("%.2f", cafeController.getVeganFoodLineTimeSpent()));
//        updateStaffedCashierTimeSpent(String.format("%.2f", cafeController.getStaffedCashierTimeSpent()));
//        updateSelfServiceCashierTimeSpent(String.format("%.2f", cafeController.getSelfServiceCashierTimeSpent()));

        simulationSpeed1.setText(String.format("%.2f", cafeController.getSimulationSpeed()));
        arrivalRate1.setText(String.format("%.2f", cafeController.getArrivalRate()));
        foodLineSpeed1.setText(String.format("%.2f", cafeController.getFoodLineSpeed()));
        cashierSpeed1.setText(String.format("%.2f",  cafeController.getCashierSpeed()));
        totalStudentsServed.setText(String.format("%d", cafeController.getTotalStudentsServed()));
        averageTimeSpent.setText(String.format("%.2f", cafeController.getAverageTimeSpent()));
        normalFoodLineTimeSpent.setText(String.format("%.2f",cafeController.getNormalFoodLineTimeSpent()));
        veganFoodLineTimeSpent.setText(String.format("%.2f", cafeController.getVeganFoodLineTimeSpent()));
        staffedCashierTimeSpent.setText(String.format("%.2f", cafeController.getStaffedCashierTimeSpent()));
        selfServiceCashierTimeSpent.setText(String.format("%.2f", cafeController.getSelfServiceCashierTimeSpent()));
    }


    @FXML
    private void pauseButtonAction() {
        pauseButton1.setDisable(true);
        resumeButton1.setDisable(false);
        stopButton1.setDisable(false);
        messageBox.setText("Simulation paused. Press RESUME to continue.");
        cafeController.pauseButtonAction();
        startUpdatingView();
    }

    @FXML
    private void resumeButtonAction() {
        resumeButton1.setDisable(true);
        pauseButton1.setDisable(false);
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(true);
        stopButton1.setDisable(false);
        messageBox.setText("Simulation resumed.");
        cafeController.resumeButtonAction();
    }

    @FXML
    private void stopButtonAction() {
        setButtonsDisabled(true);
        preferenceButton1.setDisable(false);
        queueLengthButton1.setDisable(false);
        messageBox.setText("Simulation stopped. Press START to start a new simulation.");
        cafeController.stopButtonAction();
        stopButtonAction();

    }
    @FXML
    public void startSimulation() throws InterruptedException {
        if (validateInputs()) {
            setButtonsDisabled(false);
            showMessage("Simulation started. Use PAUSE, RESUME, and RESTART as needed.");
            cafeController.startSimulation(getSimulationTime(), getDelayTime());
        }
    }

    @FXML
    public void pauseSimulation() {
        setButtonsDisabled(true);
        showMessage("Simulation paused. Press RESUME to continue.");
        cafeController.pauseSimulation();
    }

    @FXML
    public void resumeSimulation() {
        setButtonsDisabled(true);
        showMessage("Simulation resumed.");
        cafeController.resumeSimulation();
    }

    @FXML
    public void stopSimulation() {
        setButtonsDisabled(true);
        showMessage("Simulation stopped. Press START to start a new simulation.");
        cafeController.stopSimulation();
    }

    @FXML
    public void lessSimulationSpeedAction() {
        System.out.println("The lessSimulationSpeedAction button has been pressed");
        cafeController.lessSimulationSpeed();
    }

    @FXML
    public void moreSimulationSpeedAction() {
        System.out.println("The moreSimulationSpeedAction button has been pressed");
        cafeController.moreSimulationSpeed();
    }

    @FXML
    public void lessArrivalRateAction()  {
        System.out.println("The lessArrivalRateAction button has been pressed");
        cafeController.lessArrivalRate();
    }

    @FXML
    public void moreArrivalRateAction() {
        System.out.println("The moreArrivalRateAction button has been pressed");
        cafeController.moreArrivalRate();
    }

    @FXML
    public void lessFoodLineSpeedAction() {
        System.out.println("The lessFoodLineSpeedAction button has been pressed");
        cafeController.lessFoodLineSpeed();
    }

    @FXML
    public void moreFoodLineSpeedAction() {
        System.out.println("The moreFoodLineSpeedAction button has been pressed");
        cafeController.moreFoodLineSpeed();
    }

    @FXML
    public void lessCashierSpeedAction() {
        System.out.println("The lessCashierSpeedAction button has been pressed");
        cafeController.lessCashierSpeed();
    }

    @FXML
    public void moreCashierSpeedAction() {
        System.out.println("The moreCashierSpeedAction button has been pressed");
        cafeController.moreCashierSpeed();
    }


    @Override
    public void setButtonsDisabled(boolean disabled) {
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
        stopButton1.setDisable(disabled);
        preferenceButton1.setDisable(disabled);
        queueLengthButton1.setDisable(disabled);
    }


//    @Override
//    public void updateSimulationSpeed(String speed) {
//        simulationSpeed1.setText(speed);
//    }

//    @Override
//    public void updateArrivalRate(String rate) {
//        arrivalRate1.setText(rate);
//    }

//    @Override
//    public void updateFoodLineSpeed(String speed) {
//        foodLineSpeed1.setText(speed);
//    }
//
//    @Override
//    public void updateCashierSpeed(String speed) {
//        cashierSpeed1.setText(speed);
//    }
//
//    @Override
//    public void updateTotalStudentsServed(String total) {
//        totalStudentsServed.setText(total);
//    }

//    @Override
//    public void updateAverageTimeSpent(String time) {
//        averageTimeSpent.setText(time);
//    }

//    @Override
//    public void updateNormalFoodLineTimeSpent(String time) {
//        normalFoodLineTimeSpent.setText(time);
//    }
//
//    @Override
//    public void updateVeganFoodLineTimeSpent(String time) {
//        veganFoodLineTimeSpent.setText(time);
//    }

//    @Override
//    public void updateStaffedCashierTimeSpent(String time) {
//        staffedCashierTimeSpent.setText(time);
//    }
//
//    @Override
//    public void updateSelfServiceCashierTimeSpent(String time) {
//        selfServiceCashierTimeSpent.setText(time);
//    }

    @Override
    public void showMessage(String message) {
        messageBox.setText(message);
    }

    @Override
    public String getSimulationTime() {
        return simulationTime1.getText();
    }

    @Override
    public String getDelayTime() {
        return delayTime1.getText();
    }

    @Override
    public boolean isPreferenceButtonDisabled() {
        return preferenceButton1.isDisable();
    }

    @Override
    public boolean isQueueLengthButtonDisabled() {
        return queueLengthButton1.isDisable();
    }

    public boolean validateInputs() {
        boolean valid = true;
        if (!isInteger((getSimulationTime())) || !isInteger(getDelayTime())) {
            showMessage("Simulation Time and Delay Time must be an integer.");
            valid = false;
        } else if (Integer.parseInt(getSimulationTime()) < 1 || Integer.parseInt(getDelayTime()) < 1) {
            showMessage("Simulation Time and Delay Time must be greater than 0.");
            valid = false;
        } else if (!isPreferenceButtonDisabled() && !isQueueLengthButtonDisabled()) {
            showMessage("Please select Choosing Type.");
            valid = false;
        }
        System.out.println("Dealy Timr" + getDelayTime());
        System.out.println("Simulation " + getSimulationTime());
        return valid;
    }
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}