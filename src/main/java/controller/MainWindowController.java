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
    Boolean chatOnTop;
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

        primaryStage.widthProperty().addListener((_, _, newValue) -> {
            double center = (newValue.doubleValue() - containerAnchorPane.getWidth()) / 2;
            containerAnchorPane.setLayoutX(center);
        });

        primaryStage.heightProperty().addListener((_, _, newValue) -> {
            logOut.setLayoutY(newValue.doubleValue() - logOut.getHeight());
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
        chatOnTop=false;
        changePriorityOrderForContainerAnchorPane();
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

    void changePriorityOrderForContainerAnchorPane(){
        var sizeChildren=containerAnchorPane.getChildren().size();
        var secondToLastChild= containerAnchorPane.getChildren().get(sizeChildren-2);
        var lastChild = containerAnchorPane.getChildren().get(sizeChildren-1);
        containerAnchorPane.getChildren().remove(lastChild);
        containerAnchorPane.getChildren().remove(secondToLastChild);
        containerAnchorPane.getChildren().add(lastChild);
        containerAnchorPane.getChildren().add(secondToLastChild);
    }

    @FXML
    void handleMessages(ActionEvent event) {
        var children = chatPane.getChildren();
        for(var child: children){
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
        if(!chatOnTop) {
            changePriorityOrderForContainerAnchorPane();
            chatOnTop=true;
        }
    }

    @FXML
    public void handleSignout(ActionEvent event) {
        manager.updateNotifications(user,numberOfNotifications);
        if(primaryStage==null)
            manager.switchPage("login.fxml", "Log In", null,null,null);
        else
            primaryStage.close();
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
        if(chatOnTop) {
            changePriorityOrderForContainerAnchorPane();
            chatOnTop=false;
        }
    }

    @FXML
    public void handleFriendsButton(ActionEvent event) {
        var childen = chatPane.getChildren();
        for(var child: childen){
            child.setVisible(true);
        }
        listFriendsSuggestionFriendsAnchorPane.getChildren().clear();
        updateContainer(friendsOfUser, friendsScrollPane, 9, "friend-delete.fxml", ControllerType.FRIENDLIST);
        if(chatOnTop) {
            changePriorityOrderForContainerAnchorPane();
            chatOnTop=false;
        }
    }

    @FXML
    public void handleHomeButton(ActionEvent event) {
        var childen = chatPane.getChildren();
        for(var child: childen){
            child.setVisible(true);
        }
        listFriendsSuggestionFriendsAnchorPane.getChildren().clear();
        friendsScrollPane.getChildren().clear();
        if(chatOnTop) {
            changePriorityOrderForContainerAnchorPane();
            chatOnTop=false;
        }
    }

    public void handleFIlter() {
        Predicate<User> p1 = user -> ((user.getLastName() + " " + user.getLastName()).startsWith(searchField.getText()));
        List<User> filteredList = nonFriendsOfUser
                .stream()
                .filter(p1)
                .toList();
        updateContainer(filteredList, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
    }

    @Override
    public void update(FriendEntityChangeEvent event) {
        if(event.getType().equals(ChangeEventType.DECLINE)||event.getType().equals(ChangeEventType.ADD)||event.getType().equals(ChangeEventType.DELETE)){
            nonFriendsOfUser = manager.getNonFriendsOfUser(user.getId());
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
}