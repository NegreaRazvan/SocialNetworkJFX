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

    Service service;

    private void initService(){
        String url = "jdbc:postgresql://localhost:3580/Users";
        String user = "postgres";
        String password = "PGADMINPASSWORD";
        String queryLoad="SELECT id, first_name, last_name, password FROM public.\"User\"";
        String queryLoadF="SELECT id, user_id, friend_id FROM public.\"Friendship\"";

        Repository repository = new UserRepositoryDB(url,user,password, queryLoad);
        Repository repositoryF = new FriendRepositoryDB(url,user,password,queryLoadF);
        Validator validator = new UserValidator();

        this.service=new Service(validator, repository, repositoryF);
    }

    @Override
    public void start(Stage stage) throws IOException {
        initService();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Pane root=fxmlLoader.load();
        stage.setScene(new Scene(root));

        Controller controller = fxmlLoader.getController();
        controller.setService(service);
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}