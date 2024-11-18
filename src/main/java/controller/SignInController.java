package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import map.domain.User;
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
        System.out.println("Username: " + username + " Password: " + password + " First Name: " + firstName + " Last Name: " + lastName);

        if(manager.isUsernameUnique(username))
            MessageAlert.showErrorMessage(null, "Username is already in use");
        else
            System.out.println("Username is unique");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Controller controller = fxmlLoader.getController();


        this.username.clear();
        this.password.clear();
        this.firstName.clear();
        this.lastName.clear();
    }

    @FXML
    private void handleLogInHyperlink(ActionEvent event) {
        manager.switchToLogInPage();
    }
}
