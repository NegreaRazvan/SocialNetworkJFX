package Ui.socialnetworkjfx;

import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        ApplicationManager applicationManager = new ApplicationManager();
        applicationManager.startApplication(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}