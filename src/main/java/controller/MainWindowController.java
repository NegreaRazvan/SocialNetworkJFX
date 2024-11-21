package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import map.domain.Friend;
import map.domain.User;
import map.events.ChangeEventType;
import map.events.FriendEntityChangeEvent;
import map.observer.Observer;

import javax.swing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainWindowController extends Controller implements Observer<FriendEntityChangeEvent> {
    Integer numberOfNotifications;
    ArrayList<User> friendsOfUser;
    ArrayList<User> nonFriendsOfUser;
    ArrayList<User> notificationsOfUser;
    @FXML
    Label NotifCountLabel;
    @FXML
    ImageView redDotImage;
    @FXML
    User user;
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
    @FXML
    TextField searchField;
    @FXML
    ScrollPane scroller;


    public void setUser(User user) {
        this.user = user;
    }

    public void updateFriendSuggestions(List<User> friends) {
        Integer length = 3;
        friendsVBox.getChildren().clear();
        Node[] nodes = new Node[3];
        if (friends.size() < 3) {
            length = friends.size();
        }
        for (int i = 0; i < length; i++) {
            try {
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-suggestion.fxml"));
                nodes[i] = fxmlLoader.load();

                manager.initController(fxmlLoader, user, friends.get(j));

                friendsVBox.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeMainWindow() {
        scroller.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
        redDotImage.setVisible(false);
        NotifCountLabel.setVisible(false);
        searchField.textProperty().addListener(o -> handleFIlter());
        nonFriendsOfUser = manager.getNonFriendsOfUser(user.getId());
        friendsOfUser = manager.getFriendsOfUser(user.getId());
        notificationsOfUser = manager.getFriendRequestsOfUser(user.getId());
        if(notificationsOfUser.size() > user.getNumberOfNotifications())
            numberOfNotifications = notificationsOfUser.size()-user.getNumberOfNotifications();
        else
            numberOfNotifications = 0;
        if(numberOfNotifications > 0){
            redDotImage.setVisible(true);
            NotifCountLabel.setVisible(true);
            NotifCountLabel.setText(numberOfNotifications + "");
        }
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
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getLastName() + " " + user.getFirstName());
        updateFriendSuggestions(nonFriendsOfUser);
    }

    @FXML
    public void handleSignout(ActionEvent event) {
        manager.updateNotifications(user,numberOfNotifications);
        manager.switchPage("login.fxml", "Log In");
    }

    @Override
    public void update(FriendEntityChangeEvent event) {
        if (event.getType().equals(ChangeEventType.REQUEST)) {
            if (Objects.equals(event.getFriend().first(), user.getId())) {
                nonFriendsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().second()));
            }
            else {
                nonFriendsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().first()));
            }
            updateFriendSuggestions(nonFriendsOfUser);
        }
        if (event.getType().equals(ChangeEventType.DELETE)) {
            if (Objects.equals(event.getFriend().first(), user.getId())) {
                friendsOfUser.removeIf(user -> user.getId().equals(event.getFriend().second()));
                nonFriendsOfUser.add(manager.getUser(event.getFriend().second()));
            } else {
                friendsOfUser.removeIf(user -> user.getId().equals(event.getFriend().first()));
                nonFriendsOfUser.add(manager.getUser(event.getFriend().first()));
            }
            handleFriendsButton(null);
            updateFriendSuggestions(nonFriendsOfUser);
        }
        if(event.getType().equals(ChangeEventType.ADD)) {
            if(Objects.equals(event.getFriend().first(), user.getId())) {
                friendsOfUser.add(manager.getUser(event.getFriend().second()));
                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().second()));
            }
            else {
                friendsOfUser.add(manager.getUser(event.getFriend().first()));
                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().first()));
            }
            handleNotificationButton(null);
        }
        if(event.getType().equals(ChangeEventType.DECLINE)) {
            if(Objects.equals(event.getFriend().first(), user.getId())) {
                nonFriendsOfUser.add(manager.getUser(event.getFriend().second()));
                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().second()));
            }
            else {
                nonFriendsOfUser.add(manager.getUser(event.getFriend().first()));
                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().first()));
            }
            handleNotificationButton(null);
            updateFriendSuggestions(nonFriendsOfUser);
        }
    }

    public void handleFriendList(List<User> friends) {
        Integer length = 9;
        friendsScrollPane.getChildren().clear();
        Node[] nodes = new Node[9];
        if (friends.size() < 9) {
            length = friends.size();
        }
        for (int i = 0; i < length; i++) {
            try {
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-delete.fxml"));
                nodes[i] = fxmlLoader.load();

                manager.initControllerFriendList(fxmlLoader, user, friends.get(j));

                friendsScrollPane.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleFriendListNotifications(List<User> friends) {
        Integer length = 9;
        friendsScrollPane.getChildren().clear();
        Node[] nodes = new Node[9];
        if (friends.size() < 9) {
            length = friends.size();
        }
        for (int i = 0; i < length; i++) {
            try {
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("notifications.fxml"));
                nodes[i] = fxmlLoader.load();

                manager.initControllerNotifications(fxmlLoader, user, friends.get(j));

                friendsScrollPane.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleNotificationButton(ActionEvent event) {
        redDotImage.setVisible(false);
        NotifCountLabel.setVisible(false);
        numberOfNotifications=0;
        handleFriendListNotifications(notificationsOfUser);
    }

    @FXML
    public void handleFriendsButton(ActionEvent event) {
        handleFriendList(friendsOfUser);
    }

    @FXML
    public void handleHomeButton(ActionEvent event) {
        friendsScrollPane.getChildren().clear();
    }

    public void handleFIlter() {
        Predicate<User> p1 = user -> ((user.getLastName() + " " + user.getLastName()).startsWith(searchField.getText()));
        List<User> filteredList = nonFriendsOfUser
                .stream()
                .filter(p1)
                .toList();
        updateFriendSuggestions(filteredList);
    }
}