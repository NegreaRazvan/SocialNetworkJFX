package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    }

    @FXML
    private void handleSignUpHyperLink(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sign-up.fxml"));
        Stage SignUpStage = initNewView(fxmlLoader, "Sign up");
        initController(fxmlLoader, SignUpStage);
        stage.close();
        SignUpStage.show();

    }


}
