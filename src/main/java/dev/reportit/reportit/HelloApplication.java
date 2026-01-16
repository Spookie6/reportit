package dev.reportit.reportit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home/middleScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 390, 844);
        stage.setTitle("Reportit");
        stage.setScene(scene);
        stage.show();
    }
}
