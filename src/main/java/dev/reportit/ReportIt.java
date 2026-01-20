package dev.reportit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportIt extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppContext.init();

        FXMLLoader fxmlLoader =
                new FXMLLoader(ReportIt.class.getResource(Scenes.LOGIN.getFxml()));

        Scene scene = new Scene(fxmlLoader.load(), 390, 844);

        ScreenManager.setStage(stage);

        stage.setTitle("Reportit");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        AppContext.shutdown();
        System.exit(200);
    }
}
