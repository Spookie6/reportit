package dev.reportit;

import dev.reportit.networking.AuthService;
import dev.reportit.networking.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controllers {
    @FXML private Label usernameLabel;

    @FXML private TextField tfUser;
    @FXML private TextField tfEmail;
    @FXML private PasswordField pfPass;

    @FXML
    public void initialize() {
        UserData user = AppContext.getCurrentUser();
        if (ScreenManager.getCurrent().equals(Scenes.MIDDLE)) {
            System.out.println(user.toString());
            usernameLabel.setText("Welcome, " + user.username);
        }
    }

    @FXML
    protected void goHome() {
        ScreenManager.switchScene(Scenes.MIDDLE);
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
                System.out.println(body);
            },
            Throwable::printStackTrace
        );
    }

    @FXML
    protected void onLogin() {
        AuthService.login(
                tfEmail.getText(),
                pfPass.getText(),
                login -> AuthService.getUser(
                        user -> {
                            AppContext.setCurrentUser(user);
                            ScreenManager.switchScene(Scenes.MIDDLE);
                        },
                        Throwable::printStackTrace
                ),
                Throwable::printStackTrace
        );
    }
}