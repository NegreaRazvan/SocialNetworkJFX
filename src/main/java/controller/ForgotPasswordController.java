package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.domain.validators.ValidationException;
import messageAlert.MessageAlert;

public class ForgotPasswordController extends Controller{
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField usernameField;

    @FXML
    private void handleLogInHyperlink(ActionEvent event) {
        manager.switchPage("login.fxml", "Log In");
    }

    @FXML
    private void handleSignUpHyperLink(ActionEvent event) {
        manager.switchPage("sign-up.fxml","Sign up");
    }

    @FXML
    private void handlePasswordChange(ActionEvent event) {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String username = usernameField.getText();
        if (!password.equals(confirmPassword))
            MessageAlert.showErrorMessage(null, "Passwords do not match");
        else
            try{
                manager.updateUser(username, password);
                this.confirmPasswordField.clear();
                this.passwordField.clear();
                this.usernameField.clear();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success", "Successfully updated your password");
                manager.switchPage("login.fxml", "Log In");

            }catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }catch (IllegalArgumentException e) {
                MessageAlert.showErrorMessage(null, "Type the username and the password");
            }
    }
}
