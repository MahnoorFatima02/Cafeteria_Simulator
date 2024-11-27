package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CafeteriaGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Cafeteria Simulator");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

//There is not yet any logic in this class to switch scenes or panels.
//The IDs of buttons and fields has been added to FXML files.