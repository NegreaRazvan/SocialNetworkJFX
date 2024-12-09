package Ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import Ui.messageAlert.MessageAlert;

import java.io.IOException;

public class LogInController extends Controller {
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    private void handleLogInButton(ActionEvent event) throws IOException {
        String username=this.username.getText();
        String password=this.password.getText();
        System.out.println("username: "+username +" password: "+password);

        if(manager.isUserInDatabase(username, password)) {;
            manager.switchPage("main-window.fxml", "SocialNetwork", manager.getUser(username), null, ControllerType.MAINWINDOW);
        }
        else{
            MessageAlert.showErrorMessage(null, "The password or the username was incorrect");
        }
    }

    @FXML
    private void handleSignUpHyperLink(ActionEvent event) {
        manager.switchPage("sign-up.fxml","Sign up",null,null,null);
    }

    @FXML
    private void handleForgotHyperLink(ActionEvent event) {
        manager.switchPage("forgot-password.fxml","Forgot password",null,null,null);
    }


}