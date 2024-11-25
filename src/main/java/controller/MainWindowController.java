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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import map.domain.User;
import map.events.ChangeEventType;
import map.events.FriendEntityChangeEvent;
import map.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MainWindowController extends Controller implements Observer<FriendEntityChangeEvent> {
    Stage primaryStage;
    User user;
    Integer numberOfNotifications;
    ArrayList<User> friendsOfUser;
    ArrayList<User> nonFriendsOfUser;
    ArrayList<User> notificationsOfUser;
    @FXML
    Label NotifCountLabel;
    @FXML
    ImageView redDotImage;
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
    @FXML
    AnchorPane mainPane;
    @FXML
    AnchorPane containerAnchorPane;
    @FXML
    VBox logOut;
    @FXML
    AnchorPane listFriendsSuggestionFriendsAnchorPane;
    @FXML
    AnchorPane chatPane;


    public void setUser(User user) {
        this.user = user;
    }

    public void setStage(Stage stage) { this.primaryStage=stage;}

    public void initObject(User friend,VBox container, String fxmlFile, ControllerType controllerType) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(container.widthProperty());


        }catch (IOException e){
            e.printStackTrace();
        }
        manager.initController(fxmlLoader, user, friend, controllerType);
        container.getChildren().add(node);
    }

    public void updateContainer(List<User> friends, VBox container, Integer numberOfObjects, String fxmlFile, ControllerType controllerType) {
        Integer length = numberOfObjects;
        container.getChildren().clear();
        if (friends.size() < numberOfObjects)
            length = friends.size();
        for (int i = 0; i < length; i++)
            initObject(friends.get(i), container, fxmlFile, controllerType);
    }

    public void setCss(){
        ///resize  handle
        primaryStage.setMinWidth(1036.0);
        primaryStage.setMinHeight(730);

        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double center = (newValue.doubleValue() - containerAnchorPane.getWidth()) / 2;

            // Avoid overlapping issues by ensuring bounds are valid
            containerAnchorPane.setLayoutX(Math.max(center, 0)); // Prevent negative layoutX values
        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            double yPosition = newValue.doubleValue() - logOut.getHeight();

            // Ensure logOut is always visible and does not overlap buttons
            logOut.setLayoutY(Math.max(yPosition, 0));
        });



        ///hide the scrollbar
        scroller.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
        ///hide the initial notifcation image
        redDotImage.setVisible(false);
        NotifCountLabel.setVisible(false);
        ///hove

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
        ///set the name and the username of the user curently logged in
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getLastName() + " " + user.getFirstName());
    }

    public void initializeWindow(User user, Stage stage) {
        setUser(user);
        setStage(stage);
        setCss();
        ///makes it so when I search it filters the friend suggestion list
        searchField.textProperty().addListener(o -> handleFIlter());
        ///gets all the important lists that are going to be used
        nonFriendsOfUser = manager.getNonFriendsOfUser(user.getId());
        friendsOfUser = manager.getFriendsOfUser(user.getId());
        notificationsOfUser = manager.getFriendRequestsOfUser(user.getId());
        ///number of notifications
        if(notificationsOfUser.size() > user.getNumberOfNotifications())
            numberOfNotifications = notificationsOfUser.size()-user.getNumberOfNotifications();
        else
            numberOfNotifications = 0;
        ///if user received notifications while offline, show them
        if(numberOfNotifications > 0){
            redDotImage.setVisible(true);
            NotifCountLabel.setVisible(true);
            NotifCountLabel.setText(numberOfNotifications + "");
        }
        ///make this window an observer of the service
        manager.addObserverMainWindow(this);
        ///updates the friend suggestion Window
        updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
    }

    @FXML void handleMessages(ActionEvent event) {
        var childen = chatPane.getChildren();
        for(var child: childen){
            child.setVisible(false);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat.fxml"));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(listFriendsSuggestionFriendsAnchorPane.widthProperty());


        }catch (IOException e){
            e.printStackTrace();
        }
        manager.initController(fxmlLoader, user, null, ControllerType.CHAT);
        listFriendsSuggestionFriendsAnchorPane.getChildren().add(node);
    }

    @FXML
    public void handleSignout(ActionEvent event) {
        manager.updateNotifications(user,numberOfNotifications);
        if(primaryStage==null)
            manager.switchPage("login.fxml", "Log In", null,null,null);
        else
            primaryStage.close();
    }

    @Override
    public void update(FriendEntityChangeEvent event) {
//        if (event.getType().equals(ChangeEventType.REQUEST)) {
//            if (Objects.equals(event.getFriend().first(), user.getId())) {
//                nonFriendsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().second()));
//            }
//            else {
//            }
//            updateFriendSuggestions(nonFriendsOfUser);
        //}
//        if (event.getType().equals(ChangeEventType.DELETE)) {
//            if (Objects.equals(event.getFriend().first(), user.getId())) {
//                friendsOfUser.removeIf(user -> user.getId().equals(event.getFriend().second()));
//                nonFriendsOfUser.add(manager.getUser(event.getFriend().second()));
//            } else {
//                friendsOfUser.removeIf(user -> user.getId().equals(event.getFriend().first()));
//                nonFriendsOfUser.add(manager.getUser(event.getFriend().first()));
//            }
//            handleFriendsButton(null);
//            updateFriendSuggestions(nonFriendsOfUser);
//        }
//        if(event.getType().equals(ChangeEventType.ADD)) {
//            if(Objects.equals(event.getFriend().first(), user.getId())) {
//                friendsOfUser.add(manager.getUser(event.getFriend().second()));
//            }
//            else {
//                friendsOfUser.add(manager.getUser(event.getFriend().first()));
//                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().first()));
//            }
//            handleFriendsButton(null);
//        }
//        if(event.getType().equals(ChangeEventType.DECLINE)) {
//            if(Objects.equals(event.getFriend().first(), user.getId())) {
//                nonFriendsOfUser.add(manager.getUser(event.getFriend().second()));
//                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().second()));
//            }
//            else {
//                nonFriendsOfUser.add(manager.getUser(event.getFriend().first()));
//                notificationsOfUser.removeIf(user1 -> user1.getId().equals(event.getFriend().first()));
//            }
//            handleFriendsButton(null);
//            updateFriendSuggestions(nonFriendsOfUser);
//        }
        nonFriendsOfUser=manager.getNonFriendsOfUser(user.getId());
        friendsOfUser = manager.getFriendsOfUser(user.getId());
        notificationsOfUser = manager.getFriendRequestsOfUser(user.getId());
        if (event.getType().equals(ChangeEventType.REQUEST)) {
            updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
            if (Objects.equals(event.getFriend().second(), user.getId())) {
                numberOfNotifications++;
                redDotImage.setVisible(true);
                NotifCountLabel.setVisible(true);
                NotifCountLabel.setText(numberOfNotifications + "");
            }
        }
        if(event.getType().equals(ChangeEventType.DECLINE))
            if(Objects.equals(event.getFriend().second(), user.getId()))
                updateContainer(notificationsOfUser, friendsScrollPane, 9, "notifications.fxml", ControllerType.NOTIFICATION);

        if(event.getType().equals(ChangeEventType.ADD))
            updateContainer(friendsOfUser, friendsScrollPane, 9, "friend-delete.fxml", ControllerType.FRIENDLIST);
        if(event.getType().equals(ChangeEventType.DELETE)) {
            updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
            updateContainer(friendsOfUser, friendsScrollPane, 9, "friend-delete.fxml", ControllerType.FRIENDLIST);
        }
    }


    @FXML
    public void handleNotificationButton(ActionEvent event) {
        var childen = chatPane.getChildren();
        for(var child: childen){
            child.setVisible(true);
        }
        listFriendsSuggestionFriendsAnchorPane.getChildren().clear();
        redDotImage.setVisible(false);
        NotifCountLabel.setVisible(false);
        numberOfNotifications=0;
        updateContainer(notificationsOfUser, friendsScrollPane, 9, "notifications.fxml", ControllerType.NOTIFICATION);
    }

    @FXML
    public void handleFriendsButton(ActionEvent event) {
        updateContainer(friendsOfUser, friendsScrollPane, 9, "friend-delete.fxml", ControllerType.FRIENDLIST);
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
        updateContainer(filteredList, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
    }
}