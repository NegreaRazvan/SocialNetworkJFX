package com.beginsecure.socialnetworkjfx;

import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        ApplicationManager applicationManager = new ApplicationManager(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}