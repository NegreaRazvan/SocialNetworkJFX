package com.beginsecure.socialnetworkjfx;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import map.domain.Friend;
import map.domain.User;
import map.domain.validators.UserValidator;
import map.domain.validators.Validator;
import map.events.ChangeEventType;
import map.repository.Repository;
import map.repository.db.FriendRepositoryDB;
import map.repository.db.UserRepositoryDB;
import map.service.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ApplicationManager {
    private Stage primaryStage;
    private Service service;

    private void initService(){
        String url = "jdbc:postgresql://localhost:3580/Users";
        String user = "postgres";
        String password = "PGADMINPASSWORD";
        String queryLoad="SELECT id, first_name, last_name, password, username, admin, number_notifications FROM public.\"User\"";
        String queryLoadF="SELECT id, user_id, friend_id, request, date FROM public.\"Friendship\"";

        Repository repository = new UserRepositoryDB(url,user,password, queryLoad);
        Repository repositoryF = new FriendRepositoryDB(url,user,password,queryLoadF);
        Validator validator = new UserValidator();

        this.service=new Service(validator, repository, repositoryF);
    }


    public void startApplication(Stage stage) throws IOException {
        primaryStage=stage;
        initService();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Pane root=fxmlLoader.load();
        primaryStage.setScene(new Scene(root));

        LogInController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        primaryStage.setTitle("Log In");
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

    public <C extends Controller> void callInitialization(C controller,User user, User friend, ControllerType type) {
        switch(type){
            case MAINWINDOW -> {if( controller instanceof MainWindowController mainWindowController) mainWindowController.initializeWindow(user, null);}
            case NOTIFICATION -> {if( controller instanceof NotificationsAddFriendController notificationsAddFriendController) notificationsAddFriendController.initializeWindow(user, friend);}
            case FRIENDLIST -> {if( controller instanceof FriendListController friendListController) friendListController.initializeWindow(user, friend);}
            case FRIENDSUGGESTION -> {if (controller instanceof FriendSuggestionController friendSuggestionController) friendSuggestionController.initializeWindow(user, friend);}
        }
    }

    public <C extends Controller> void initController(FXMLLoader fxmlLoader, User user, User friend, ControllerType controllerType) {
        C controller=fxmlLoader.getController();
        controller.setApplicationManager(this);
        if(controllerType!=null)
            callInitialization(controller,user,friend, controllerType);
    }

    public void switchPage(String page, String title, User user, User friend, ControllerType controllerType) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        initNewView(fxmlLoader, title);
        initController(fxmlLoader,user,friend,controllerType);
    }

    public void makeNewPage(String page, String title, User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        Pane root=fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        MainWindowController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        controller.initializeWindow(user, stage);
        stage.setTitle(title);
        stage.show();
    }

    ///user related logic

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
            service.updateUser(user.get().getId(), user.get().getFirstName(),user.get().getLastName(),password,username,false, 0);
        else
            service.updateUser(null,null,null,password,username,false, 0);
    }

    public User getUser(String username){
        return service.findOneUser(username).get();
    }



    ///The 3 main lists

    public ArrayList<User> getNonFriendsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendsOfAUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<User> getFriendsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendsOfTheUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<User> getFriendRequestsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendRequestsOfTheUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    ///friend related logic

    public void sendFriendRequest(Long userId, Long friendId){
        service.saveFriend(userId,friendId,true);
    }

    public void deleteOrDeclineFriend(Long userId, Long friendId, ChangeEventType changeEventType){
        Optional<Friend> friend = service.findOneFriend(userId,friendId);
        friend.ifPresent(value -> service.deleteFriend(value.getId(), changeEventType));
    }

    public void acceptFriendRequest(Long userId, Long friendId){
        service.acceptFriend(userId,friendId);
    }

    public void updateNotifications(User user,Integer numberOfNotifications){
        service.updateUser(user.getId(),user.getFirstName(),user.getLastName(),user.getPassword(),user.getUsername(),false,numberOfNotifications);
    }

    public Friend getFriendRequest(Long userId, Long friendId){
        return service.findOneFriend(userId,friendId).get();
    }

    public void addObserverMainWindow(MainWindowController controller){
        service.addObserver(controller);
    }
}