import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // UPDATED PATH: Points to LoginScreen now
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/javaFx2/loginS/LoginScreen.fxml")));

            Scene scene = new Scene(root);
            primaryStage.setTitle("ReportIt App");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}