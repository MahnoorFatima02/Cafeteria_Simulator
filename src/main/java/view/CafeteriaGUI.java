package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.CafeteriaController;

public class CafeteriaGUI extends Application {
    private CafeteriaController controller;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        loadScene("/mainpage.fxml");
    }

    public void loadScene(String fxmlFile) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setMainApp(this);
        primaryStage.setTitle("Cafeteria Simulator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


}