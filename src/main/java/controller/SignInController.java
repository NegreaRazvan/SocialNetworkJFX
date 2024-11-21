package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import map.domain.User;
import map.domain.validators.ValidationException;
import messageAlert.MessageAlert;

public class SignInController extends Controller {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();

        if(manager.isUsernameTaken(username))
            MessageAlert.showErrorMessage(null, "Username is already in use");
        else{
            try{
                manager.addValidUser(username, password, firstName, lastName);
                this.username.clear();
                this.password.clear();
                this.firstName.clear();
                this.lastName.clear();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success", "You were successfully registered");
                manager.switchPage("login.fxml", "Log In");

            }catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }

    }

    @FXML
    private void handleLogInHyperlink(ActionEvent event) {
        manager.switchPage("login.fxml", "Log In");
    }
}
