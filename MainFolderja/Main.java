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
            // Dit moet het pad zijn naar de eerste scherm van de app
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/javaFx2/homeS/HomeScreen.fxml")));

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