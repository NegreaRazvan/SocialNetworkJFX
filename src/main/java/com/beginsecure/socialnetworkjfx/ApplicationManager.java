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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ApplicationManager {
    private Stage primaryStage;
    private Service service;

    private void initService(){
        String url = "jdbc:postgresql://192.168.1.51:3580/Users";
        String user = "postgres";
        String password = "PGADMINPASSWORD";
        String queryLoad="SELECT id, first_name, last_name, password, username, admin, number_notifications FROM public.\"User\"";
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

    protected Controller initController(FXMLLoader fxmlLoader,User user) {
        MainWindowController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        controller.setUser(user);
        controller.initializeMainWindow();
        return controller;
    }

    public Controller initController(FXMLLoader fxmlLoader,User user,User friend) {
        FriendSuggestionController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        controller.initializeFriendCard(user, friend);
        return controller;
    }

    public Controller initControllerFriendList(FXMLLoader fxmlLoader, User user, User friend) {
        FriendListController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        controller.initializeFriendCard(user, friend);
        return controller;
    }

    public Controller initControllerNotifications(FXMLLoader fxmlLoader, User user, User friend) {
        NotificationsAddFriendController controller = fxmlLoader.getController();
        controller.setApplicationManager(this);
        controller.initializeFriendCard(user, friend);
        return controller;
    }

    public void switchPage(String page, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        initNewView(fxmlLoader, title);
        initController(fxmlLoader);
    }

    public void switchPage(String page, String title, User user){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        initNewView(fxmlLoader, title);
        initController(fxmlLoader, user);
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
            service.updateUser(user.get().getId(), user.get().getFirstName(),user.get().getLastName(),password,username,false, 0);
        else
            service.updateUser(null,null,null,password,username,false, 0);
    }

    public User getUser(String username){
        return service.findOneUser(username).get();
    }

    public User getUser(Long id){
        return service.findOneUser(id).get();
    }

    public ArrayList<User> getNonFriendsOfUser(Long userId){
//        Iterable<Friend> friends=service.findAllFriendsOfAUser(userId);
//        List<Long> friendIDs= new java.util.ArrayList<>(StreamSupport
//                .stream(friends.spliterator(), false)
//                .flatMap(friend -> Stream.of(friend.first(), friend.second()))
//                .filter(id -> !Objects.equals(id, userId))
//                .distinct()
//                .toList());
//        friendIDs.add(userId);
//        List<Long> allUserIDsThatArentFriends= StreamSupport
//                .stream(service.findAllUsers().spliterator(),false)
//                .map(User::getId)
//                .filter(element -> !friendIDs.contains(element))
//                .toList();
//        return allUserIDsThatArentFriends;
        return StreamSupport.stream(service.findAllFriendsOfAUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<User> getFriendsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendsOfTheUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<User> getFriendRequestsOfUser(Long userId){
        return StreamSupport.stream(service.findAllFriendRequestsOfTheUser(userId).spliterator(),false).collect(Collectors.toCollection(ArrayList::new));
    }


    public void sendFriendRequest(Long userId, Long friendId){
        service.saveFriend(userId,friendId,true);
    }

    public void deleteFriendFromList(Long userId, Long friendId){
        Optional<Friend> friend = service.findOneFriend(userId,friendId);
        friend.ifPresent(value -> service.deleteFriend(value.getId(), ChangeEventType.DELETE));
    }

    public void declineFriendRequest(Long userId, Long friendId){
        Optional<Friend> friend = service.findOneFriend(userId,friendId);
        friend.ifPresent(value -> service.deleteFriend(value.getId(), ChangeEventType.DECLINE));
    }

    public void addObserverMainWindow(MainWindowController controller){
        service.addObserver(controller);
    }

    public void acceptFriendRequest(Long userId, Long friendId){
        service.acceptFriend(userId,friendId);
    }

    public void updateNotifications(User user,Integer numberOfNotifications){
        service.updateUser(user.getId(),user.getFirstName(),user.getLastName(),user.getPassword(),user.getUsername(),false,numberOfNotifications);
    }
}