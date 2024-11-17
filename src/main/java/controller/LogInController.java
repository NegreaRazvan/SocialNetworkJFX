package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

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
    private void handleSignUpHyperLink(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Pane root=fxmlLoader.load();
        Scene signInScene=new Scene(root);
        Stage signInStage=new Stage();

        Controller signInController=fxmlLoader.getController();
        signInController.setStage(signInStage); signInController.setService(service);
    }


}
