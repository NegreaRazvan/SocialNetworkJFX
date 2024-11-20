package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import map.events.ChangeEventType;
import map.events.FriendEntityChangeEvent;
import map.observer.Observer;

import java.io.IOException;
import java.util.List;

public class MainWindowController extends Controller implements Observer<FriendEntityChangeEvent> {
    String username;
    @FXML
    Label usernameLabel;
    @FXML
    Label nameLabel;
    @FXML
    VBox friendsVBox;
    @FXML
    Button homeButton;
    @FXML
    Button exploreButton;
    @FXML
    Button notificationsButton;
    @FXML
    Button messagesButton;
    @FXML
    Button bookmarksButton;
    @FXML
    Button profileButton;
    @FXML
    Button friendsButton;
    @FXML
    VBox friendsScrollPane;


    public void setUsername(String username) {
        this.username = username;
    }

    public void updateFriendSuggestions(){
        Integer length=3;
        friendsVBox.getChildren().clear();
        Node[] nodes = new Node[3];
        List<Long> nonFriends=manager.getNonFriendsOfUser(manager.getUser(username).getId());
        System.out.println(nonFriends);
        if(nonFriends.size()<3){
            length=nonFriends.size();
        }
        for (int i = 0; i < length; i++) {
            try {
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-suggestion.fxml"));
                nodes[i] = fxmlLoader.load();

                manager.initController(fxmlLoader, manager.getUser(username).getId(), nonFriends.get(j));

                friendsVBox.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeMainWindow(){
        homeButton.setOnMouseEntered(event -> {
            homeButton.setStyle("-fx-background-color : #808080");
        });
        homeButton.setOnMouseExited(event -> {
            homeButton.setStyle("-fx-background-color : black");
        });
        exploreButton.setOnMouseEntered(event -> {
            exploreButton.setStyle("-fx-background-color : #808080");
        });
        exploreButton.setOnMouseExited(event -> {
            exploreButton.setStyle("-fx-background-color : black");
        });
        notificationsButton.setOnMouseEntered(event -> {
            notificationsButton.setStyle("-fx-background-color : #808080");
        });
        notificationsButton.setOnMouseExited(event -> {
            notificationsButton.setStyle("-fx-background-color : black");
        });
        messagesButton.setOnMouseEntered(event -> {
            messagesButton.setStyle("-fx-background-color : #808080");
        });
        messagesButton.setOnMouseExited(event -> {
            messagesButton.setStyle("-fx-background-color : black");
        });
        bookmarksButton.setOnMouseEntered(event -> {
            bookmarksButton.setStyle("-fx-background-color : #808080");
        });
        bookmarksButton.setOnMouseExited(event -> {
            bookmarksButton.setStyle("-fx-background-color : black");
        });
        profileButton.setOnMouseEntered(event -> {
            profileButton.setStyle("-fx-background-color : #808080");
        });
        profileButton.setOnMouseExited(event -> {
            profileButton.setStyle("-fx-background-color : black");
        });
        friendsButton.setOnMouseEntered(event -> {
            friendsButton.setStyle("-fx-background-color : #808080");
        });
        friendsButton.setOnMouseExited(event -> {
            friendsButton.setStyle("-fx-background-color : black");
        });
        friendsScrollPane.getChildren().clear();
        manager.addObserverMainWindow(this);
        usernameLabel.setText(username);
        nameLabel.setText(manager.getUser(username).getLastName() + " " + manager.getUser(username).getFirstName());
        updateFriendSuggestions();
    }

    @FXML
    public void handleSignout(ActionEvent event) {
        manager.switchPage("login.fxml", "Log In");
    }

    @Override
    public void update(FriendEntityChangeEvent event) {
        if(event.getType().equals(ChangeEventType.REQUEST))
            updateFriendSuggestions();
        if(event.getType().equals(ChangeEventType.DELETE))
            handleFriendsButton(null);
    }

    @FXML
    public void handleFriendsButton(ActionEvent event) {
        Integer length=3;
        friendsScrollPane.getChildren().clear();
        Node[] nodes = new Node[9];
        List<Long> nonFriends=manager.getFriendsOfUser(manager.getUser(username).getId());
        System.out.println(nonFriends);
        if(nonFriends.size()<3){
            length=nonFriends.size();
        }
        for (int i = 0; i < length; i++) {
            try {
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-delete.fxml"));
                nodes[i] = fxmlLoader.load();

                manager.initControllerFriendList(fxmlLoader, manager.getUser(username).getId(), nonFriends.get(j));

                friendsScrollPane.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleHomeButton(ActionEvent event){
        friendsScrollPane.getChildren().clear();
    }
}