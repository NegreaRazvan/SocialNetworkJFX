package Ui.socialnetworkjfx;

import Ui.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import layers.domain.Friend;
import layers.domain.MessageDTO;
import layers.domain.User;
import layers.domain.validators.UserValidator;
import layers.domain.validators.Validator;
import Utils.events.ChangeEventType;
import Utils.paging.Pageable;
import layers.repository.Repository;
import layers.repository.db.FriendRepositoryDB;
import layers.repository.db.MessageRepositoryDB;
import layers.repository.db.UserRepositoryDB;
import layers.service.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        String queryLoad="SELECT id, first_name, last_name, password, username, admin, number_notifications,profile_picture FROM public.\"User\"";
        String queryLoadF="SELECT id, user_id, friend_id, request, date FROM public.\"Friendship\"";
        String queryLoadM="SELECT id, \"to\", \"from\", message, date, \"idReplyMessage\", \"idOfReplyMessage\" FROM public.\"Message\"";

        Repository repository = new UserRepositoryDB(url,user,password, queryLoad);
        Repository repositoryF = new FriendRepositoryDB(url,user,password,queryLoadF);
        Repository repositoryM = new MessageRepositoryDB(url,user,password,queryLoadM);

        Validator validator = new UserValidator();

        this.service=new Service(validator, repository, repositoryF, repositoryM);
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
            case MAINWINDOW -> {if( controller instanceof MainWindowController mainWindowController) mainWindowController.initializeWindow(user, primaryStage);}
            case NOTIFICATION -> {if( controller instanceof NotificationsAddFriendController notificationsAddFriendController) notificationsAddFriendController.initializeWindow(user, friend);}
            case FRIENDLIST -> {if( controller instanceof FriendListController friendListController) friendListController.initializeWindow(user, friend);}
            case FRIENDSUGGESTION -> {if (controller instanceof FriendSuggestionController friendSuggestionController) friendSuggestionController.initializeWindow(user, friend);}
            case CHAT -> {if( controller instanceof ChatController chatController) chatController.initializeWindow(user, friend, primaryStage);}
            case FRIENDSHOWCHATLIST -> {if(controller instanceof FriendShowOnChatListController friendShowOnChatListController) friendShowOnChatListController.initializeWindow(friend);}
            case FRIENDSHOWCHAT -> {if (controller instanceof FriendShowOnChatController friendShowOnChatController) friendShowOnChatController.initializeWindow(friend);}
            case MAINPAGE -> {if (controller instanceof MainPageController mainPageController) mainPageController.initializeWindow(user);}
            case PROFILE -> {if (controller instanceof ProfileController profileController) profileController.initializeWindow(user); }
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

    public void addValidUser(String username, String password, String firstName, String lastName) throws NoSuchAlgorithmException {
        service.saveUser(firstName,lastName,password,username);
    }

    public void updateUser(String username, String password) throws NoSuchAlgorithmException {
        Optional<User> user = service.findOneUser(username);
        if(user.isPresent())
            service.updateUser(user.get().getId(), user.get().getFirstName(),user.get().getLastName(),password,username,false, 0,null,true);
        else
            service.updateUser(null,null,null,password,username,false, 0,null,true);
    }

    public User getUser(String username){
        return service.findOneUser(username).get();
    }

    public int getCountFriends(Long userId) {
        return service.countFriends(userId);
    }




    ///The 4 main lists

    public ArrayList<User> getNonFriendsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendsOfAUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<User> getFriendsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendsOfTheUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<User> getFriendsOfUser(Pageable pageable, Long userId){
        return StreamSupport.stream(service.findAllFriendsOfTheUser(pageable,userId).getElementsOnPage().spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<User> getFriendRequestsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendRequestsOfTheUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<MessageDTO> getSentMessages(Long userId, Long friendId){
        return StreamSupport.stream(service.findAllSentMessages(friendId, userId).spliterator(), false).collect(Collectors.toCollection(ArrayList::new));
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

    public void updateNotifications(User user,Integer numberOfNotifications) throws NoSuchAlgorithmException {
        service.updateUser(user.getId(),user.getFirstName(),user.getLastName(),user.getPassword(),user.getUsername(),false,numberOfNotifications, null,false);
    }

    public Friend getFriendRequest(Long userId, Long friendId){
        return service.findOneFriend(userId,friendId).get();
    }

    public void addObserverMainWindow(MainWindowController controller){
        service.addObserver(controller);
    }


    public void addObserverChatWindow(ChatController controller){
        service.addObserver(controller);
    }

    //message related logic

    public void sendMessage(Long from, Long to, String message, String replyMessage, Long idOfTheReplyMessage){
        service.saveMessage(to,from,message,replyMessage,idOfTheReplyMessage);
    }

    public MessageDTO getMessage(Long id){
        return service.findOneMessage(id).get();
    }

}