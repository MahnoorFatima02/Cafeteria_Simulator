package controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import simu.framework.Trace;
import simu.utility.SimulationVariables;
import view.CafeteriaGUI;
import simu.model.MyEngine;
import simu.model.SimulationAdjustments;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.chart.PieChart;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import simu.framework.Clock;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;

public class CafeteriaController {
    private CafeteriaGUI mainApp;
    private MyEngine engine;
    private Timeline timeline;

    @FXML
    private Button lessSimulationSpeed1, moreSimulationSpeed1, lessArrivalRate1, moreArrivalRate1, lessFoodLineSpeed1, moreFoodLineSpeed1, lessCashierSpeed1, moreCashierSpeed1, startButton1, pauseButton1, resumeButton1, preferenceButton1, queueLengthButton1, stopButton1;
    @FXML
    private TextField simulationTime1, delayTime1;
    @FXML
    private Label messageBox, simulationSpeed1, arrivalRate1, foodLineSpeed1, cashierSpeed1, totalStudentsServed, averageTimeSpent, normalFoodLineTimeSpent, veganFoodLineTimeSpent, staffedCashierTimeSpent, selfServiceCashierTimeSpent, queue1, queue2, queue3, queue4, veganStationServing, veganStationServed, nonVeganStation1Serving, nonVeganStation1Served, nonVeganStation2Serving, nonVeganStation2Served, cashier1Serving, cashier1Served, cashier2Serving, cashier2Served, selfCashierServing, selfCashierServed, totalStudentsNotServed, serveEfficiency;
    @FXML
    private Circle ball1, ball2, ball2extra, ball3, ball4, ball5, ball6, ball6extra, ball7, ball8, ball8extra, ball9, ball10, ball10extra, ball11, ball12, ball13, ball14, ball15, ball16, ball17, cashier2Active;
    @FXML
    private ImageView image1, image2, image3, image4, image5, image6;
    @FXML
    private PieChart servedPieChart;
    @FXML
    private LineChart<Number, Number> averagetimeLineChart;
    @FXML
    private NumberAxis horizontalAxisLineChart;
    @FXML
    private NumberAxis verticalAxisLineChart, timeAxis;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private BarChart<String, Number> foodlineBarChart;

    private XYChart.Series<Number, Number> averageTimeSeries;
    private XYChart.Series<String, Number> foodLineSeries;
    private TranslateTransition transition1, transition2, transition2extra, transition3, transition4, transition5, transition6, transition6extra, transition7, transition8, transition8extra, transition9, transition10, transition10extra, transition11, transition12, transition13, transition14, transition15, transition16, transition17;
    private RotateTransition rotateTransition1, rotateTransition2, rotateTransition3, rotateTransition4, rotateTransition5, rotateTransition6;

    public void setMainApp(CafeteriaGUI mainApp) {
        this.mainApp = mainApp;
    }

    public CafeteriaController() {
        this.engine = new MyEngine();
    }

    /*
        ====== OnActions =======
    */
    @FXML
    private void preferenceButtonAction() {
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(false);
        resumeButton1.setDisable(true);
        engine.setAssignByQueueLength(false);
        checkStartConditions();
    }

    @FXML
    private void queueLengthButtonAction() {
        queueLengthButton1.setDisable(true);
        preferenceButton1.setDisable(false);
        resumeButton1.setDisable(true);
        engine.setAssignByQueueLength(true);
        checkStartConditions();
    }

    @FXML
    private void startButtonAction() throws InterruptedException {
        if (validateInputs()) {
            initialSetup();

            engine.setSimulationTime(Double.parseDouble(simulationTime1.getText()));
            engine.setDelayTime(Double.parseDouble(delayTime1.getText()));
            Trace.setTraceLevel(Trace.Level.INFO);

            new Thread(() -> {
                engine.startSimulation();
            }).start();

            // Create a Timeline to update the GUI elements periodically
            timeline = new Timeline(new KeyFrame(Duration.millis(SimulationVariables.DELAY_TIME*1000), event -> {
                if (engine.isRunning() && !engine.isStopped()) {
                    ballControllers();
                    updateLabels();
                    setTexts();
                    updatePieChart();
                    updateLineChart();
                    updateBarChart();
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    @FXML
    private void pauseButtonAction() {
        stopRotateImage();
        pauseButton1.setDisable(true);
        resumeButton1.setDisable(false);
        stopButton1.setDisable(false);
        messageBox.setText("Simulation paused. Press RESUME to continue.");
        engine.pauseSimulation();
    }

    @FXML
    private void resumeButtonAction() {
        startRotateImage();
        resumeButton1.setDisable(true);
        pauseButton1.setDisable(false);
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(true);
        stopButton1.setDisable(false);
        messageBox.setText("Simulation resumed.");
        engine.resumeSimulation();
    }
    @FXML
    private void stopButtonAction() {
        stopRotateImage();
        setButtonsDisabled(true);
        preferenceButton1.setDisable(false);
        queueLengthButton1.setDisable(false);
        messageBox.setText("Simulation stopped. Press START to start a new simulation.");
        engine.stopSimulation();
        if (timeline != null) {
            timeline.stop();
        }
        resetElements();
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
        SimulationAdjustments.setAdjustStimulationSpeedFlag(false);

    }

    public void moreSimulationSpeedAction() {
        System.out.println("The moreSimulationSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustStimulationSpeedFlag(true);
    }

    public void lessArrivalRateAction() {
        System.out.println("The lessArrivalRateAction button has been pressed");
        SimulationAdjustments.setAdjustStudentArrivalFlag(false);
    }

    public void moreArrivalRateAction() {
        System.out.println("The moreArrivalRateAction button has been pressed");
        SimulationAdjustments.setAdjustStudentArrivalFlag(true);
    }

    public void lessFoodLineSpeedAction() {
        System.out.println("The lessFoodLineSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(false);
    }

    public void moreFoodLineSpeedAction() {
        System.out.println("The moreFoodLineSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustFoodLineServiceSpeedFlag(true);
    }

    public void lessCashierSpeedAction() {
        System.out.println("The lessCashierSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(false);
    }

    public void moreCashierSpeedAction() {
        System.out.println("The moreCashierSpeedAction button has been pressed");
        SimulationAdjustments.setAdjustCashierServiceSpeedFlag(true);
    }
    /*
        ====== OnActions =======
    */

    /*
        ====== Update Labesls =======
    */
    public void updateVeganStationServing() {
        if (engine.veganFoodStation.isReserved()) {
            veganStationServing.setText(String.format("%d", engine.veganFoodStation.getCurrentCustomerID()));
        } else {
            veganStationServing.setText("None");
        }
    }

    public void updateVeganStationServed() {
        veganStationServed.setText(String.format("%d", engine.veganFoodStation.getTotalCustomersRemoved()));
    }

    public void updateNonVeganStation1Serving() {
        if (engine.nonVeganFoodStation[0].isReserved()) {
            nonVeganStation1Serving.setText(String.format("%d", engine.nonVeganFoodStation[0].getCurrentCustomerID()));
        } else {
            nonVeganStation1Serving.setText("None");
        }
    }

    public void updateNonVeganStation1Served() {
        nonVeganStation1Served.setText(String.format("%d", engine.nonVeganFoodStation[0].getTotalCustomersRemoved()));
    }

    public void updateNonVeganStation2Serving() {
        if (engine.nonVeganFoodStation[1].isReserved()) {
            nonVeganStation2Serving.setText(String.format("%d", engine.nonVeganFoodStation[1].getCurrentCustomerID()));
        } else {
            nonVeganStation2Serving.setText("None");
        }
    }

    public void updateNonVeganStation2Served() {
        nonVeganStation2Served.setText(String.format("%d", engine.nonVeganFoodStation[1].getTotalCustomersRemoved()));
    }

    public void updateCashier1Serving() {
        if (engine.cashierServicePoints[0].isReserved()) {
            cashier1Serving.setText(String.format("%d", engine.cashierServicePoints[0].getCurrentCustomerID()));
        } else {
            cashier1Serving.setText("None");
        }
    }

    public void updateCashier1Served() {
        cashier1Served.setText(String.format("%d", engine.cashierServicePoints[0].getTotalCustomersRemoved()));
    }

    public void updateCashier2Serving() {
        if (engine.cashierServicePoints[1].isReserved()) {
            cashier2Serving.setText(String.format("%d", engine.cashierServicePoints[1].getCurrentCustomerID()));
        } else {
            cashier2Serving.setText("None");
        }
    }

    public void updateCashier2Served() {
        cashier2Served.setText(String.format("%d", engine.cashierServicePoints[1].getTotalCustomersRemoved()));
    }

    public void updateSelfCashierServing() {
        if (engine.selfCheckoutServicePoint.isReserved()) {
            selfCashierServing.setText(String.format("%d", engine.selfCheckoutServicePoint.getCurrentCustomerID()));
        } else {
            selfCashierServing.setText("None");
        }
    }

    public void updateSelfCashierServed() {
        selfCashierServed.setText(String.format("%d", engine.selfCheckoutServicePoint.getTotalCustomersRemoved()));
    }
    /*
        ====== Update Labesls =======
    */

    /*
        ====== Chart Components =======
    */
    private void setupPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Total Students Served", SimulationVariables.TOTAL_CUSTOMERS_SERVED),
                new PieChart.Data("Students Not Served", SimulationVariables.TOTAL_CUSTOMERS_NOT_SERVED)
        );

        servedPieChart.setData(pieChartData);
        servedPieChart.setLabelsVisible(false);
        servedPieChart.setLegendVisible(false);

        // Set colors for the pie chart slices
        for (PieChart.Data data : servedPieChart.getData()) {
            if (data.getName().equals("Total Students Served")) {
                data.getNode().setStyle("-fx-pie-color: #ff5000;");
            } else if (data.getName().equals("Students Not Served")) {
                data.getNode().setStyle("-fx-pie-color: #ffa07b;");
            }
        }
    }
    public void updatePieChart() {
        for (PieChart.Data data : servedPieChart.getData()) {
            if (data.getName().equals("Total Students Served")) {
                data.setPieValue(SimulationVariables.TOTAL_CUSTOMERS_SERVED);
            } else if (data.getName().equals("Students Not Served")) {
                data.setPieValue(SimulationVariables.TOTAL_CUSTOMERS_NOT_SERVED);
            }
        }
    }

    private void setupLineChart() {
        horizontalAxisLineChart.setLabel(null);
        verticalAxisLineChart.setLabel(null);
        horizontalAxisLineChart.setAutoRanging(false);
        verticalAxisLineChart.setAutoRanging(false);
        horizontalAxisLineChart.setLowerBound(0);
        verticalAxisLineChart.setLowerBound(0);
        averagetimeLineChart.setLegendVisible(false);
        averagetimeLineChart.setHorizontalGridLinesVisible(false);
        averagetimeLineChart.setVerticalGridLinesVisible(false);
        averagetimeLineChart.setStyle("-fx-background-color: transparent;");
        averagetimeLineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #ffdbcd;");

        // Set tick label formatter to show only integer values
        horizontalAxisLineChart.setTickLabelFormatter(new NumberAxis.DefaultFormatter(horizontalAxisLineChart) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }
        });

        verticalAxisLineChart.setTickLabelFormatter(new NumberAxis.DefaultFormatter(verticalAxisLineChart) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }
        });

        averageTimeSeries = new XYChart.Series<>();
        averagetimeLineChart.getData().add(averageTimeSeries);
    }

    public void updateLineChart() {
        int currentTime = (int) Clock.getInstance().getClock();
        int averageTimeSpent = (int) SimulationVariables.AVERAGE_TIME_SPENT;

        averageTimeSeries.getData().add(new XYChart.Data<>(currentTime, averageTimeSpent));

        // Calculate the upper bounds for the axes
        int horizontalUpperBound = (int) Math.ceil(currentTime / 10.0) * 10;
        int verticalUpperBound = (int) Math.ceil(averageTimeSpent / 10.0) * 10;

        // Set the upper bounds and minor tick counts
        horizontalAxisLineChart.setUpperBound(horizontalUpperBound);
        verticalAxisLineChart.setUpperBound(verticalUpperBound);

        horizontalAxisLineChart.setTickUnit(horizontalUpperBound / 5.0);
        verticalAxisLineChart.setTickUnit(verticalUpperBound / 4.0);
    }

    private void setupBarChart() {
        categoryAxis.setLabel(null);
        timeAxis.setLabel(null);
        timeAxis.setAutoRanging(false);
        timeAxis.setLowerBound(0);
        foodlineBarChart.setLegendVisible(false);
        foodlineBarChart.setHorizontalGridLinesVisible(false);
        foodlineBarChart.setVerticalGridLinesVisible(false);
        foodlineBarChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #ffdbcd;");
        foodlineBarChart.setAnimated(false);

        // Set tick label formatter to show only integer values
        timeAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(timeAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }
        });

        // Set categories for the horizontal axis
        categoryAxis.setCategories(FXCollections.observableArrayList(
                "Normal Food Line", "Vegan Food Line", "Staffed Cashier", "Self-service Cashier"
        ));

        foodLineSeries = new XYChart.Series<>();
        foodlineBarChart.getData().add(foodLineSeries);
    }

    public void updateBarChart() {
        foodLineSeries.getData().clear();

        XYChart.Data<String, Number> normalFoodLine = new XYChart.Data<>("Normal Food Line", SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME);
        XYChart.Data<String, Number> veganFoodLine = new XYChart.Data<>("Vegan Food Line", SimulationVariables.AVG_VEGAN_SERVICE_TIME);
        XYChart.Data<String, Number> staffedCashier = new XYChart.Data<>("Staffed Cashier", SimulationVariables.AVG_CASHIER_SERVICE_TIME);
        XYChart.Data<String, Number> selfServiceCashier = new XYChart.Data<>("Self-service Cashier", SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME);

        // Set different colors for each bar
        normalFoodLine.nodeProperty().addListener((observable, oldValue, newValue) -> newValue.setStyle("-fx-bar-fill: #ff5000;"));
        veganFoodLine.nodeProperty().addListener((observable, oldValue, newValue) -> newValue.setStyle("-fx-bar-fill: #00bf63;"));
        staffedCashier.nodeProperty().addListener((observable, oldValue, newValue) -> newValue.setStyle("-fx-bar-fill: #0097b2;"));
        selfServiceCashier.nodeProperty().addListener((observable, oldValue, newValue) -> newValue.setStyle("-fx-bar-fill: #5271ff;"));

        foodLineSeries.getData().addAll(normalFoodLine, veganFoodLine, staffedCashier, selfServiceCashier);

        // Calculate the upper bound for the vertical axis
        double maxValue = Math.max(Math.max(SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME, SimulationVariables.AVG_VEGAN_SERVICE_TIME),
                Math.max(SimulationVariables.AVG_CASHIER_SERVICE_TIME, SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME));
        int verticalUpperBound = (int) Math.ceil(maxValue / 10.0) * 10;

        timeAxis.setUpperBound(verticalUpperBound);
        timeAxis.setTickUnit(verticalUpperBound / 4.0);
    }
    /*
        ====== Chart Components =======
    */

    /*
        ====== Utilities =======
    */
    public void initialSetup() {
        setupWiggleAnimation();
        startRotateImage();
        rotateTransition5.stop();
        image5.setOpacity(0.4);
        cashier2Active.setFill(Color.RED);
        setupBalls();
        setButtonsDisabled(false);
        startButton1.setDisable(true);
        preferenceButton1.setDisable(true);
        queueLengthButton1.setDisable(true);
        pauseButton1.setDisable(false);
        stopButton1.setDisable(false);
        resumeButton1.setDisable(true);
        messageBox.setText("Simulation started. Use PAUSE, RESUME, and RESTART as needed.");
        setupPieChart();
        setupLineChart();
        setupBarChart();
    }

    public void startRotateImage(){
        rotateTransition1.play();
        rotateTransition2.play();
        rotateTransition3.play();
        rotateTransition4.play();
        rotateTransition5.play();
        rotateTransition6.play();
    }

    public void stopRotateImage() {
        rotateTransition1.stop();
        rotateTransition2.stop();
        rotateTransition3.stop();
        rotateTransition4.stop();
        rotateTransition5.stop();
        rotateTransition6.stop();
    }

    public void resetElements(){
        queue1.setText("0");
        queue2.setText("0");
        queue3.setText("0");
        queue4.setText("0");
        veganStationServed.setText("0");
        veganStationServing.setText("0");
        nonVeganStation1Served.setText("0");
        nonVeganStation1Serving.setText("0");
        nonVeganStation2Served.setText("0");
        nonVeganStation2Serving.setText("0");
        cashier1Served.setText("0");
        cashier1Serving.setText("0");
        cashier2Served.setText("0");
        cashier2Serving.setText("0");
        selfCashierServing.setText("0");
        selfCashierServed.setText("0");
    }

    public void updateLabels(){
        updateVeganStationServed();
        updateVeganStationServing();
        updateNonVeganStation1Served();
        updateNonVeganStation1Serving();
        updateNonVeganStation2Served();
        updateNonVeganStation2Serving();
        updateCashier1Served();
        updateCashier1Serving();
        updateCashier2Served();
        updateCashier2Serving();
        updateSelfCashierServing();
        updateSelfCashierServed();
    }

    public void ballControllers(){
        controlBall1();
        controlBall2();
        controlBall2extra();
        controlBall3();
        controlBall4();
        controlBall5();
        controlBall6();
        controlBall6extra();
        controlBall7();
        controlBall8();
        controlBall8extra();
        controlBall9();
        controlBall10();
        controlBall10extra();
        controlBall11();
        controlBall12();
        controlBall13();
        controlBall14();
        controlBall15();
        controlBall16();
        controlBall17();
        checkCashier2Active();
    }

    public void checkCashier2Active(){
        if (engine.cashierServicePoints[1].isActive()) {
            cashier2Active.setFill(Color.GREEN);
            rotateTransition5.play();
            image5.setOpacity(1.0);
        } else {
            cashier2Active.setFill(Color.RED);
            rotateTransition5.stop();
            image5.setOpacity(0.4);
        }
    }

    public void setTexts(){
        simulationSpeed1.setText(String.format("%.2f", SimulationVariables.DELAY_TIME));
        arrivalRate1.setText(String.format("%.2f", SimulationVariables.ARRIVAL_MEAN));
        foodLineSpeed1.setText(String.format("%.2f", SimulationVariables.MEAN_NON_VEGAN_SERVICE));
        cashierSpeed1.setText(String.format("%.2f", SimulationVariables.MEAN_CASHIER));
        totalStudentsServed.setText(String.format("%d", SimulationVariables.TOTAL_CUSTOMERS_SERVED));
        totalStudentsNotServed.setText(String.format("%d", SimulationVariables.TOTAL_CUSTOMERS_NOT_SERVED));
        serveEfficiency.setText(String.format("%.2f", SimulationVariables.SERVE_EFFICIENCY));
        averageTimeSpent.setText(String.format("%.2f", SimulationVariables.AVERAGE_TIME_SPENT));
        normalFoodLineTimeSpent.setText(String.format("%.2f", SimulationVariables.AVG_NON_VEGAN_SERVICE_TIME));
        veganFoodLineTimeSpent.setText(String.format("%.2f", SimulationVariables.AVG_VEGAN_SERVICE_TIME));
        staffedCashierTimeSpent.setText(String.format("%.2f", SimulationVariables.AVG_CASHIER_SERVICE_TIME));
        selfServiceCashierTimeSpent.setText(String.format("%.2f", SimulationVariables.AVG_SELF_CHECKOUT_SERVICE_TIME));
        queue1.setText(String.format("%d", engine.veganFoodStation.getQueueSize()));
        queue2.setText(String.format("%d", engine.nonVeganFoodStation[0].getQueueSize()+ engine.nonVeganFoodStation[1].getQueueSize()));
        queue3.setText(String.format("%d", engine.cashierServicePoints[0].getQueueSize()+ engine.cashierServicePoints[1].getQueueSize()));
        queue4.setText(String.format("%d", engine.selfCheckoutServicePoint.getQueueSize()));
    }

    public void setupBalls(){
        setupBall1();
        setupBall2();
        setupBall2extra();
        setupBall3();
        setupBall4();
        setupBall5();
        setupBall6();
        setupBall6extra();
        setupBall7();
        setupBall8();
        setupBall8extra();
        setupBall9();
        setupBall10();
        setupBall10extra();
        setupBall11();
        setupBall12();
        setupBall13();
        setupBall14();
        setupBall15();
        setupBall16();
        setupBall17();
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
        stopButton1.setDisable(disabled);
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
        if (!isInteger(simulationTime1.getText()) || !isInteger(delayTime1.getText())) {
            messageBox.setText("Simulation Time and Delay Time must be an integer.");
            valid = false;
        } else if (Integer.parseInt(simulationTime1.getText()) < 1 || Integer.parseInt(delayTime1.getText()) < 1) {
            messageBox.setText("Simulation Time and Delay Time must be greater than 0.");
            valid = false;
        } else if (!preferenceButton1.isDisable() && !queueLengthButton1.isDisable()) {
            messageBox.setText("Please select Choosing Type.");
            valid = false;
        }
        return valid;
    }

    private void checkStartConditions() {
        if (validateInputs()) {
            startButton1.setDisable(false);
            messageBox.setText("Press START to begin the simulation.");
        }
    }
    /*
        ====== Utilities =======
    */

    /*
       ====== Setup Balls =======
   */
    public void setupBall1(){
        transition1 = new TranslateTransition();
        transition1.setNode(ball1);
        transition1.setToX(114);
        transition1.setToY(72);
        transition1.setCycleCount(1);
    }
    public void setupBall2(){
        transition2 = new TranslateTransition();
        transition2.setNode(ball2);
        transition2.setToX(-116);
        transition2.setToY(72);
        transition2.setCycleCount(1);
    }

    public void setupBall2extra(){
        transition2extra = new TranslateTransition();
        transition2extra.setNode(ball2extra);
        transition2extra.setToX(-116);
        transition2extra.setToY(72);
        transition2extra.setCycleCount(1);
    }

    public void setupBall3(){
        transition3 = new TranslateTransition();
        transition3.setNode(ball3);
        transition3.setToX(85);
        transition3.setToY(193);
        transition3.setCycleCount(1);
    }

    public void setupBall4(){
        transition4 = new TranslateTransition();
        transition4.setNode(ball4);
        transition4.setToX(91);
        transition4.setToY(193);
        transition4.setCycleCount(1);
    }

    public void setupBall5(){
        transition5 = new TranslateTransition();
        transition5.setNode(ball5);
        transition5.setToX(-135);
        transition5.setToY(193);
        transition5.setCycleCount(1);
    }

    public void setupBall6() {
        transition6 = new TranslateTransition();
        transition6.setNode(ball6);
        transition6.setToX(-87);
        transition6.setToY(197);
        transition6.setCycleCount(1);
    }

    public void setupBall6extra() {
        transition6extra = new TranslateTransition();
        transition6extra.setNode(ball6extra);
        transition6extra.setToX(-87);
        transition6extra.setToY(197);
        transition6extra.setCycleCount(1);
    }

    public void setupBall7() {
        transition7 = new TranslateTransition();
        transition7.setNode(ball7);
        transition7.setToX(-318);
        transition7.setToY(197);
        transition7.setCycleCount(1);
    }

    public void setupBall8() {
        transition8 = new TranslateTransition();
        transition8.setNode(ball8);
        transition8.setToX(138);
        transition8.setToY(198);
        transition8.setCycleCount(1);
    }

    public void setupBall8extra() {
        transition8extra = new TranslateTransition();
        transition8extra.setNode(ball8extra);
        transition8extra.setToX(138);
        transition8extra.setToY(198);
        transition8extra.setCycleCount(1);
    }

    public void setupBall9() {
        transition9 = new TranslateTransition();
        transition9.setNode(ball9);
        transition9.setToX(-92);
        transition9.setToY(199);
        transition9.setCycleCount(1);
    }

    public void setupBall10(){
        transition10 = new TranslateTransition();
        transition10.setNode(ball10);
        transition10.setToX(365);
        transition10.setToY(195);
        transition10.setCycleCount(1);
    }

    public void setupBall10extra() {
        transition10extra = new TranslateTransition();
        transition10extra.setNode(ball10extra);
        transition10extra.setToX(365);
        transition10extra.setToY(195);
        transition10extra.setCycleCount(1);
    }

    public void setupBall11() {
        transition11 = new TranslateTransition();
        transition11.setNode(ball11);
        transition11.setToX(135);
        transition11.setToY(196);
        transition11.setCycleCount(1);
    }

    public void setupBall12(){
        transition12 = new TranslateTransition();
        transition12.setNode(ball12);
        transition12.setToX(84);
        transition12.setToY(187);
        transition12.setCycleCount(1);
    }


    public void setupBall13() {
        transition13 = new TranslateTransition();
        transition13.setNode(ball13);
        transition13.setToX(-140);
        transition13.setToY(189);
        transition13.setCycleCount(1);
    }


    public void setupBall14() {
        transition14 = new TranslateTransition();
        transition14.setNode(ball14);
        transition14.setToX(-135);
        transition14.setToY(186);
        transition14.setCycleCount(1);
    }


    public void setupBall15() {
        transition15 = new TranslateTransition();
        transition15.setNode(ball15);
        transition15.setToX(-94);
        transition15.setToY(184);
        transition15.setCycleCount(1);
    }


    public void setupBall16() {
        transition16 = new TranslateTransition();
        transition16.setNode(ball16);
        transition16.setToX(131);
        transition16.setToY(183);
        transition16.setCycleCount(1);
    }


    public void setupBall17() {
        transition17 = new TranslateTransition();
        transition17.setNode(ball17);
        transition17.setToX(353);
        transition17.setToY(183);
        transition17.setCycleCount(1);
    }

    /*
        ====== Setup Balls =======
    */

    /*
        ====== Setup Ball Controlllers & Wiggle Animations =======
    */
    public void controlBall17() {
        if (!engine.selfCashierDeparture) {
            ball17.setOpacity(0.0);
            transition17.stop();
            ball17.setTranslateX(0);
            ball17.setTranslateY(0);
            ball17.setLayoutX(68);
            ball17.setLayoutY(754);
        } else if (engine.selfCashierDeparture) {
            ball17.setOpacity(1.0);
            transition17.stop();
            ball17.setTranslateX(0);
            ball17.setTranslateY(0);
            transition17.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition17.play();
            engine.selfCashierDeparture = false;
        }
    }

    public void controlBall16() {
        if (!engine.cashierDeparture2) {
            ball16.setOpacity(0.0);
            transition16.stop();
            ball16.setTranslateX(0);
            ball16.setTranslateY(0);
            ball16.setLayoutX(290);
            ball16.setLayoutY(754);
        } else if (engine.cashierDeparture2) {
            ball16.setOpacity(1.0);
            transition16.stop();
            ball16.setTranslateX(0);
            ball16.setTranslateY(0);
            transition16.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition16.play();
            engine.cashierDeparture2 = false;
        }
    }

    public void controlBall15(){
        if (!engine.cashierDeparture1) {
            ball15.setOpacity(0.0);
            transition15.stop();
            ball15.setTranslateX(0);
            ball15.setTranslateY(0);
            ball15.setLayoutX(515);
            ball15.setLayoutY(753);
        } else if (engine.cashierDeparture1) {
            ball15.setOpacity(1.0);
            transition15.stop();
            ball15.setTranslateX(0);
            ball15.setTranslateY(0);
            transition15.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition15.play();
            engine.cashierDeparture1 = false;
        }
    }

    public void controlBall14() {
        if (!engine.selfCashierQueueDeparture || !engine.selfCashierArrival) {
            ball14.setOpacity(0.0);
            transition14.stop();
            ball14.setTranslateX(0);
            ball14.setTranslateY(0);
            ball14.setLayoutX(203);
            ball14.setLayoutY(566);
        } else if (engine.selfCashierQueueDeparture && engine.selfCashierArrival && engine.selfCheckoutServicePoint.getQueueSize() > 1) {
            ball14.setOpacity(1.0);
            transition14.stop();
            ball14.setTranslateX(0);
            ball14.setTranslateY(0);
            transition14.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition14.play();
            engine.selfCashierQueueDeparture = false;
            engine.selfCashierArrival = false;
        } else if (engine.selfCashierQueueDeparture && engine.selfCashierArrival && engine.selfCheckoutServicePoint.getQueueSize() <= 1) {
            ball14.setOpacity(1.0);
            transition14.stop();
            ball14.setTranslateX(0);
            ball14.setTranslateY(0);
            transition14.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            PauseTransition pause8 = new PauseTransition(Duration.millis(SimulationVariables.DELAY_TIME *500)); // Adjust the delay as needed
            pause8.setOnFinished(event -> {
                transition14.play();
            });
            pause8.play();
            engine.selfCashierQueueDeparture = false;
            engine.selfCashierArrival = false;
        }
    }

    public void controlBall13() {
        if (!engine.cashierQueueDeparture2 || !engine.cashierArrival2) {
            ball13.setOpacity(0.0);
            transition13.stop();
            ball13.setTranslateX(0);
            ball13.setTranslateY(0);
            ball13.setLayoutX(431);
            ball13.setLayoutY(565);
        } else if (engine.cashierQueueDeparture2 && engine.cashierArrival2 && engine.cashierServicePoints[1].getQueueSize() > 1) {
            ball13.setOpacity(1.0);
            transition13.stop();
            ball13.setTranslateX(0);
            ball13.setTranslateY(0);
            transition13.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition13.play();
            engine.cashierQueueDeparture2 = false;
            engine.cashierArrival2 = false;
        } else if (engine.cashierQueueDeparture2 && engine.cashierArrival2 && engine.cashierServicePoints[1].getQueueSize() <= 1) {
            ball13.setOpacity(1.0);
            transition13.stop();
            ball13.setTranslateX(0);
            ball13.setTranslateY(0);
            transition13.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            PauseTransition pause7 = new PauseTransition(Duration.millis(SimulationVariables.DELAY_TIME *500)); // Adjust the delay as needed
            pause7.setOnFinished(event -> {
                transition13.play();
            });
            pause7.play();
            engine.cashierQueueDeparture2 = false;
            engine.cashierArrival2 = false;
        }
    }

    public void controlBall12() {
        if (!engine.cashierQueueDeparture1 || !engine.cashierArrival1) {
            ball12.setOpacity(0.0);
            transition12.stop();
            ball12.setTranslateX(0);
            ball12.setTranslateY(0);
            ball12.setLayoutX(431);
            ball12.setLayoutY(565);
        } else if (engine.cashierQueueDeparture1 && engine.cashierArrival1 && engine.cashierServicePoints[0].getQueueSize() > 1) {
            ball12.setOpacity(1.0);
            transition12.stop();
            ball12.setTranslateX(0);
            ball12.setTranslateY(0);
            transition12.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition12.play();
            engine.cashierQueueDeparture1 = false;
            engine.cashierArrival1 = false;
        } else if (engine.cashierQueueDeparture1 && engine.cashierArrival1 && engine.cashierServicePoints[0].getQueueSize() <= 1) {
            ball12.setOpacity(1.0);
            transition12.stop();
            ball12.setTranslateX(0);
            ball12.setTranslateY(0);
            transition12.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            PauseTransition pause6 = new PauseTransition(Duration.millis(SimulationVariables.DELAY_TIME *500)); // Adjust the delay as needed
            pause6.setOnFinished(event -> {
                transition12.play();
            });
            pause6.play();
            engine.cashierQueueDeparture1 = false;
            engine.cashierArrival1 = false;
        }
    }

    public void controlBall11() {
        if (!engine.nonVeganDeparture2 || !engine.selfCashierQueueArrival) {
            ball11.setOpacity(0.0);
            transition11.stop();
            ball11.setTranslateX(0);
            ball11.setTranslateY(0);
            ball11.setLayoutX(68);
            ball11.setLayoutY(370);
        } else if (engine.nonVeganDeparture2 && engine.selfCashierQueueArrival) {
            ball11.setOpacity(1.0);
            transition11.stop();
            ball11.setTranslateX(0);
            ball11.setTranslateY(0);
            transition11.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition11.play();
            engine.nonVeganDeparture2 = false;
            engine.selfCashierQueueArrival = false;
        }
    }

    public void controlBall10extra() {
        if (!engine.nonVeganDeparture2 || !engine.cashierQueueArrival2) {
            ball10extra.setOpacity(0.0);
            transition10extra.stop();
            ball10extra.setTranslateX(0);
            ball10extra.setTranslateY(0);
            ball10extra.setLayoutX(68);
            ball10extra.setLayoutY(370);
        } else if (engine.nonVeganDeparture2 && engine.cashierQueueArrival2) {
            ball10extra.setOpacity(1.0);
            transition10extra.stop();
            ball10extra.setTranslateX(0);
            ball10extra.setTranslateY(0);
            transition10extra.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition10extra.play();
            engine.nonVeganDeparture2 = false;
            engine.cashierQueueArrival2 = false;
        }
    }

    public void controlBall10(){
        if (!engine.nonVeganDeparture2 || !engine.cashierQueueArrival1) {
            ball10.setOpacity(0.0);
            transition10.stop();
            ball10.setTranslateX(0);
            ball10.setTranslateY(0);
            ball10.setLayoutX(68);
            ball10.setLayoutY(370);
        } else if (engine.nonVeganDeparture2 && engine.cashierQueueArrival1) {
            ball10.setOpacity(1.0);
            transition10.stop();
            ball10.setTranslateX(0);
            ball10.setTranslateY(0);
            transition10.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition10.play();
            engine.nonVeganDeparture2 = false;
            engine.cashierQueueArrival1 = false;
        }
    }

    public void controlBall9(){
        if (!engine.nonVeganDeparture1 || !engine.selfCashierQueueArrival) {
            ball9.setOpacity(0.0);
            transition9.stop();
            ball9.setTranslateX(0);
            ball9.setTranslateY(0);
            ball9.setLayoutX(295);
            ball9.setLayoutY(367);
        } else if (engine.nonVeganDeparture1 && engine.selfCashierQueueArrival) {
            ball9.setOpacity(1.0);
            transition9.stop();
            ball9.setTranslateX(0);
            ball9.setTranslateY(0);
            transition9.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition9.play();
            engine.nonVeganDeparture1 = false;
            engine.selfCashierQueueArrival = false;
        }
    }

    public void controlBall8extra() {
        if (!engine.nonVeganDeparture1 || !engine.cashierQueueArrival2) {
            ball8extra.setOpacity(0.0);
            transition8extra.stop();
            ball8extra.setTranslateX(0);
            ball8extra.setTranslateY(0);
            ball8extra.setLayoutX(295);
            ball8extra.setLayoutY(367);
        } else if (engine.nonVeganDeparture1 && engine.cashierQueueArrival2) {
            ball8extra.setOpacity(1.0);
            transition8extra.stop();
            ball8extra.setTranslateX(0);
            ball8extra.setTranslateY(0);
            transition8extra.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition8extra.play();
            engine.nonVeganDeparture1 = false;
            engine.cashierQueueArrival2 = false;
        }
    }

    public void controlBall8(){
        if (!engine.nonVeganDeparture1 || !engine.cashierQueueArrival1) {
            ball8.setOpacity(0.0);
            transition8.stop();
            ball8.setTranslateX(0);
            ball8.setTranslateY(0);
            ball8.setLayoutX(295);
            ball8.setLayoutY(367);
        } else if (engine.nonVeganDeparture1 && engine.cashierQueueArrival1) {
            ball8.setOpacity(1.0);
            transition8.stop();
            ball8.setTranslateX(0);
            ball8.setTranslateY(0);
            transition8.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition8.play();
            engine.nonVeganDeparture1 = false;
            engine.cashierQueueArrival1 = false;
        }
    }
    public void controlBall7(){
        if (!engine.veganDeparture || !engine.selfCashierQueueArrival) {
            ball7.setOpacity(0.0);
            transition7.stop();
            ball7.setTranslateX(0);
            ball7.setTranslateY(0);
            ball7.setLayoutX(520);
            ball7.setLayoutY(368);
        } else if (engine.veganDeparture && engine.selfCashierQueueArrival) {
            ball7.setOpacity(1.0);
            transition7.stop();
            ball7.setTranslateX(0);
            ball7.setTranslateY(0);
            transition7.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition7.play();
            engine.veganDeparture = false;
            engine.selfCashierQueueArrival = false;
        }
    }

    public void controlBall6extra() {
        if (!engine.veganDeparture || !engine.cashierQueueArrival2) {
            ball6extra.setOpacity(0.0);
            transition6extra.stop();
            ball6extra.setTranslateX(0);
            ball6extra.setTranslateY(0);
            ball6extra.setLayoutX(520);
            ball6extra.setLayoutY(368);
        } else if (engine.veganDeparture && engine.cashierQueueArrival2) {
            ball6extra.setOpacity(1.0);
            transition6extra.stop();
            ball6extra.setTranslateX(0);
            ball6extra.setTranslateY(0);
            transition6extra.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition6extra.play();
            engine.veganDeparture = false;
            engine.cashierQueueArrival2 = false;
        }
    }

    public void controlBall6(){
        if (!engine.veganDeparture || !engine.cashierQueueArrival1) {
            ball6.setOpacity(0.0);
            transition6.stop();
            ball6.setTranslateX(0);
            ball6.setTranslateY(0);
            ball6.setLayoutX(520);
            ball6.setLayoutY(368);
        } else if (engine.veganDeparture && engine.cashierQueueArrival1) {
            ball6.setOpacity(1.0);
            transition6.stop();
            ball6.setTranslateX(0);
            ball6.setTranslateY(0);
            transition6.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition6.play();
            engine.veganDeparture = false;
            engine.cashierQueueArrival1 = false;
        }
    }

    public void controlBall5() {
        if (!engine.nonVeganFoodServe2 || !engine.nonVeganQueueDeparture2) {
            ball5.setOpacity(0.0);
            transition5.stop();
            ball5.setTranslateX(0);
            ball5.setTranslateY(0);
            ball5.setLayoutX(203);
            ball5.setLayoutY(174);
        } else if (engine.nonVeganFoodServe2 && engine.nonVeganQueueDeparture2 && engine.nonVeganFoodStation[1].getQueueSize() > 1) {
            ball5.setOpacity(1.0);
            transition5.stop();
            ball5.setTranslateX(0);
            ball5.setTranslateY(0);
            transition5.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition5.play();
            engine.nonVeganFoodServe2 = false;
            engine.nonVeganQueueDeparture2 = false;
        } else if (engine.nonVeganFoodServe2 && engine.nonVeganQueueDeparture2 && engine.nonVeganFoodStation[1].getQueueSize() <= 1) {
            transition5.stop();
            ball5.setOpacity(1.0);
            ball5.setTranslateX(0);
            ball5.setTranslateY(0);
            transition5.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            PauseTransition pause5 = new PauseTransition(Duration.millis(SimulationVariables.DELAY_TIME * 500)); // Adjust the delay as needed
            pause5.setOnFinished(event -> {
                transition5.play();
            });
            pause5.play();
            engine.nonVeganFoodServe2 = false;
            engine.nonVeganQueueDeparture2 = false;
        }
    }

    public void controlBall4() {
        if (!engine.nonVeganFoodServe1 || !engine.nonVeganQueueDeparture1) {
            ball4.setOpacity(0.0);
            transition4.stop();
            ball4.setTranslateX(0);
            ball4.setTranslateY(0);
            ball4.setLayoutX(203);
            ball4.setLayoutY(174);
        } else if (engine.nonVeganFoodServe1 && engine.nonVeganQueueDeparture1 && engine.nonVeganFoodStation[0].getQueueSize() > 1) {
            ball4.setOpacity(1.0);
            transition4.stop();
            ball4.setTranslateX(0);
            ball4.setTranslateY(0);
            transition4.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition4.play();
            engine.nonVeganFoodServe1 = false;
            engine.nonVeganQueueDeparture1 = false;
        } else if (engine.nonVeganFoodServe1 && engine.nonVeganQueueDeparture1 && engine.nonVeganFoodStation[0].getQueueSize() <= 1) {
            transition4.stop();
            ball4.setOpacity(1.0);
            ball4.setTranslateX(0);
            ball4.setTranslateY(0);
            transition4.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            PauseTransition pause4 = new PauseTransition(Duration.millis(SimulationVariables.DELAY_TIME * 500)); // Adjust the delay as needed
            pause4.setOnFinished(event -> {
                transition4.play();
            });
            pause4.play();
            engine.nonVeganFoodServe1 = false;
            engine.nonVeganQueueDeparture1 = false;
        }
    }

    public void controlBall3() {
        if (!engine.veganFoodServe || !engine.veganQueueDeparture) {
            ball3.setOpacity(0.0);
            transition3.stop();
            ball3.setTranslateX(0);
            ball3.setTranslateY(0);
            ball3.setLayoutX(433);
            ball3.setLayoutY(174);
        } else if (engine.veganFoodServe && engine.veganQueueDeparture && engine.veganFoodStation.getQueueSize() > 1) {
            ball3.setOpacity(1.0);
            transition3.stop();
            ball3.setTranslateX(0);
            ball3.setTranslateY(0);
            transition3.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition3.play();
            engine.veganFoodServe = false;
            engine.veganQueueDeparture = false;
        } else if (engine.veganFoodServe && engine.veganQueueDeparture && engine.veganFoodStation.getQueueSize() <= 1) {
            transition3.stop();
            ball3.setOpacity(1.0);
            ball3.setTranslateX(0);
            ball3.setTranslateY(0);
            transition3.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            PauseTransition pause3 = new PauseTransition(Duration.millis(SimulationVariables.DELAY_TIME *500)); // Adjust the delay as needed
            pause3.setOnFinished(event -> {
                transition3.play();
            });
            pause3.play();
            engine.veganFoodServe = false;
            engine.veganQueueDeparture = false;
        }
    }

    public void controlBall2extra(){
        if (!engine.nonVeganQueueArrival2) {
            ball2extra.setOpacity(0.0);
            transition2extra.stop();
            ball2extra.setTranslateX(0);
            ball2extra.setTranslateY(0);
            ball2extra.setLayoutX(319);
            ball2extra.setLayoutY(102);
        } else {
            ball2extra.setOpacity(1.0);
            transition2extra.stop();
            ball2extra.setTranslateX(0);
            ball2extra.setTranslateY(0);
            transition2extra.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition2extra.play();
            engine.nonVeganQueueArrival2 = false;
        }

    }

    public void controlBall2(){
        if (!engine.nonVeganQueueArrival1) {
            ball2.setOpacity(0.0);
            transition2.stop();
            ball2.setTranslateX(0);
            ball2.setTranslateY(0);
            ball2.setLayoutX(319);
            ball2.setLayoutY(102);
        } else {
            ball2.setOpacity(1.0);
            transition2.stop();
            ball2.setTranslateX(0);
            ball2.setTranslateY(0);
            transition2.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition2.play();
            engine.nonVeganQueueArrival1 = false;
        }
    }
    public void controlBall1(){
        if (!engine.veganQueueArrival) {
            ball1.setOpacity(0.0);
            transition1.stop();
            ball1.setTranslateX(0);
            ball1.setTranslateY(0);
            ball1.setLayoutX(319);
            ball1.setLayoutY(102);
        }
        else {
            ball1.setOpacity(1.0);
            transition1.stop();
            ball1.setTranslateX(0);
            ball1.setTranslateY(0);
            transition1.setDuration(Duration.millis(SimulationVariables.DELAY_TIME * 500));
            transition1.play();
            engine.veganQueueArrival = false;
        }
    }

    private void setupWiggleAnimation() {
        rotateTransition1 = createRotateTransition(image1);
        rotateTransition2 = createRotateTransition(image2);
        rotateTransition3 = createRotateTransition(image3);
        rotateTransition4 = createRotateTransition(image4);
        rotateTransition5 = createRotateTransition(image5);
        rotateTransition6 = createRotateTransition(image6);
    }

    private RotateTransition createRotateTransition(ImageView imageView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), imageView);
        rotateTransition.setFromAngle(-5);
        rotateTransition.setToAngle(5);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(true);
        return rotateTransition;
    }
    /*
        ====== Setup Ball Controlllers & Wiggle Animations =======
    */
}