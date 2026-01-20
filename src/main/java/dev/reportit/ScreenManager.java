package dev.reportit;

import dev.reportit.networking.AuthState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScreenManager {
    private static Stage stage;

    private static Scenes last = null;
    private static Scenes current = Scenes.LOGIN;

    public static void setStage(Stage st) {
        stage = st;
    }

    public static void switchScene(Scenes scene) {
        if (scene != Scenes.LOGIN && scene != Scenes.REGISTER) {
            if (!AuthState.isAuthenticated()) {
                scene = Scenes.LOGIN;
            }
        }

        last = current;
        current = scene;

        String fxml = scene.getFxml();
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(ScreenManager.class.getResource(fxml))
            );

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scenes getCurrent() {
        return current;
    }
}