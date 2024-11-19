package com.beginsecure.socialnetworkjfx;

import controller.Controller;
import controller.LogInController;
import controller.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import map.domain.User;
import map.domain.validators.UserValidator;
import map.domain.validators.Validator;
import map.repository.Repository;
import map.repository.db.FriendRepositoryDB;
import map.repository.db.UserRepositoryDB;
import map.service.Service;

import java.io.IOException;
import java.util.Optional;

public class ApplicationManager {
    private Stage primaryStage;
    private Service service;

    private void initService(){
        String url = "jdbc:postgresql://192.168.1.51:3580/Users";
        String user = "postgres";
        String password = "PGADMINPASSWORD";
        String queryLoad="SELECT id, first_name, last_name, password, username, admin FROM public.\"User\"";
        String queryLoadF="SELECT id, user_id, friend_id, request, date FROM public.\"Friendship\"";

        Repository repository = new UserRepositoryDB(url,user,password, queryLoad);
        Repository repositoryF = new FriendRepositoryDB(url,user,password,queryLoadF);
        Validator validator = new UserValidator();

        this.service=new Service(validator, repository, repositoryF);
    }

    public ApplicationManager(Stage stage) throws IOException {
        this.primaryStage=stage;
        initService();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Pane root=fxmlLoader.load();
        primaryStage.setScene(new Scene(root));

        LogInController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        primaryStage.setTitle("Log In");
        primaryStage.show();
    }



    protected Stage initNewView(FXMLLoader fxmlLoader, String title) {
        try {
            Pane root=fxmlLoader.load();
            primaryStage.getScene().setRoot(root);
            primaryStage.setWidth(root.getPrefWidth());
            primaryStage.setHeight(root.getPrefHeight());
            primaryStage.setTitle(title);
            return primaryStage;
        }
        catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected Controller initController(FXMLLoader fxmlLoader) {
        Controller controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        return controller;
    }

    protected Controller initController(FXMLLoader fxmlLoader,String username) {
        MainWindowController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        controller.setUsername(username);
        controller.initializeMainWindow();
        return controller;
    }

    public void switchPage(String page, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        initNewView(fxmlLoader, title);
        initController(fxmlLoader);
    }

    public void switchPage(String page, String title, String username){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        initNewView(fxmlLoader, title);
        initController(fxmlLoader, username);
    }

    public Boolean isUsernameTaken(String username){
        return service.findOneUser(username).isPresent();
    }

    public Boolean isUserInDatabase(String username, String password){
        return service.findOneUser(username, password).isPresent();
    }

    public void addValidUser(String username, String password, String firstName, String lastName){
        service.saveUser(firstName,lastName,password,username);
    }

    public void updateUser(String username, String password){
        Optional<User> user = service.findOneUser(username);
        if(user.isPresent())
            service.updateUser(user.get().getId(), user.get().getFirstName(),user.get().getLastName(),password,username,false);
        else
            service.updateUser(null,null,null,password,username,false);
    }

    public User getUser(String username){
        return service.findOneUser(username).get();
    }
}
