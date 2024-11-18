package com.beginsecure.socialnetworkjfx;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import map.domain.validators.UserValidator;
import map.domain.validators.Validator;
import map.repository.Repository;
import map.repository.db.FriendRepositoryDB;
import map.repository.db.UserRepositoryDB;
import map.service.Service;
import map.service.ServiceInterface;

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