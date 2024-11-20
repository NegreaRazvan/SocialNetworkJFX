package controller;

import com.beginsecure.socialnetworkjfx.ApplicationManager;
import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.domain.validators.ValidationException;
import messageAlert.MessageAlert;

public class LogInController extends Controller {
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    private void handleLogInButton(ActionEvent event) {
        String username=this.username.getText();
        String password=this.password.getText();
        System.out.println("username: "+username +" password: "+password);

        if(manager.isUserInDatabase(username, password)) {;
            manager.switchPage("main-window.fxml", "SocialNetwork", username);
        }
        else{
            MessageAlert.showErrorMessage(null, "The password or the username was incorrect");
        }
    }

    @FXML
    private void handleSignUpHyperLink(ActionEvent event) {
        manager.switchPage("sign-up.fxml","Sign up");
    }

    @FXML
    private void handleForgotHyperLink(ActionEvent event) {
        manager.switchPage("forgot-password.fxml","Forgot password");
    }


}
