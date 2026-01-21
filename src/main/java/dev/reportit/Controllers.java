package dev.reportit;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.reportit.networking.AuthService;
import dev.reportit.networking.UserData;
import dev.reportit.networking.responses.MessageResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static dev.reportit.networking.AuthService.mapper;

public class Controllers {
    @FXML private Label usernameLabel;

    @FXML private TextField tfUser;
    @FXML private TextField tfEmail;
    @FXML private PasswordField pfPass;

    @FXML private Label infoLabel;

    @FXML
    public void initialize() {
        UserData user = AppContext.getCurrentUser();
        if (ScreenManager.getCurrent().equals(Scenes.MIDDLE)) {
            usernameLabel.setText("Welcome, " + user.username);
        } else {
            if (usernameLabel != null) usernameLabel.setText(user.username);
        }
    }

    @FXML
    protected void goPrevious() {
        ScreenManager.switchScene(ScreenManager.getLast() == null ? Scenes.MIDDLE : ScreenManager.getLast());
    }

    @FXML
    protected void goHome() {
        ScreenManager.switchScene(Scenes.MIDDLE);
    }

    @FXML
    protected void goChat() {
        ScreenManager.switchScene(Scenes.CHATUI);
    }

    @FXML
    protected void goThread() {
        ScreenManager.switchScene(Scenes.THREAD);
    }

    @FXML
    protected void goAddAccident() {
        ScreenManager.switchScene(Scenes.ADDINCIDENTDIALOG);
    }

    @FXML
    protected void goFeaturedIncidents() {
        ScreenManager.switchScene(Scenes.FEATUREDINCIDENTS);
    }

    @FXML
    protected void goGetHelp() {
        ScreenManager.switchScene(Scenes.GETHELPDIALOG);
    }

    @FXML
    protected void goIncidentsNear() {
        ScreenManager.switchScene(Scenes.INCIDENTSNEARYOUDIALOG);
    }

    @FXML
    protected void goReport() {
        ScreenManager.switchScene(Scenes.REPORTSCREEN);
    }

    @FXML
    protected void goRegister() {
        ScreenManager.switchScene(Scenes.REGISTER);
    }

    @FXML
    protected void goLogin() {
        ScreenManager.switchScene(Scenes.LOGIN);
    }

    @FXML
    protected void onRegister() {
        String email = tfEmail.getText();
        String username = tfUser.getText();
        String password = pfPass.getText();

        AuthService.register(email, username, password,
            (String body) -> {
                try {
                    MessageResponse res = mapper.readValue(body, MessageResponse.class);
                    infoLabel.setText(res.message);
                } catch (JsonProcessingException _) { ; }
            },
            Throwable::printStackTrace
        );
    }

    @FXML
    protected void onLogin() {
        AuthService.login(
                tfEmail.getText(),
                pfPass.getText(),
                login -> {
                    if (login instanceof MessageResponse) {
                        infoLabel.setText(((MessageResponse) login).message);
                    } else {
                        AuthService.getUser(
                                user -> {
                                    AppContext.setCurrentUser(user);
                                    ScreenManager.switchScene(Scenes.MIDDLE);
                                },
                                Throwable::printStackTrace
                        );
                    }
                },
                Throwable::printStackTrace
        );
    }
}