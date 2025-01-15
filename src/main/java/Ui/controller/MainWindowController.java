package Ui.controller;

import Ui.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import layers.domain.User;
import Utils.events.ChangeEventType;
import Utils.events.FriendEntityChangeEvent;
import Utils.observer.Observer;
import Utils.paging.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MainWindowController extends Controller implements Observer<FriendEntityChangeEvent> {
    Integer currentElements=0;
    Boolean chatOnTop;
    Stage primaryStage;
    User user;
    Integer numberOfNotifications;
    MainPageController mainPageController;
    @FXML
    AnchorPane switchInterfaceAnchor;
    @FXML
    Label NotifCountLabel;
    @FXML
    ImageView redDotImage;
    @FXML
    Label usernameLabel;
    @FXML
    Label nameLabel;
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
    AnchorPane mainPane;
    @FXML
    AnchorPane containerAnchorPane;
    @FXML
    VBox logOut;


    public void setUser(User user) {
        this.user = user;
    }

    public void setStage(Stage stage) { this.primaryStage=stage;}


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
        ///set the name and the username of the user curently logged in
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getLastName() + " " + user.getFirstName());
    }

    public void initializeWindow(User user, Stage stage) {
        setUser(user);
        setStage(stage);
        setCss();
        initializeMainPageController();
        ///number of notifications
        var notificationsOfUser=manager.getFriendRequestsOfUser(user.getId());
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
    }


    void initializeMainPageController(){
        switchInterfaceAnchor.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-page.fxml"));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(switchInterfaceAnchor.widthProperty());

        }catch (IOException e){
            e.printStackTrace();
        }
        manager.initController(fxmlLoader, user, null, ControllerType.MAINPAGE);
        switchInterfaceAnchor.getChildren().add(node);
        AnchorPane.setLeftAnchor(node, 0.0);
        mainPageController=null;
    }

    void initializeChatPageController(){
        switchInterfaceAnchor.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat.fxml"));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(switchInterfaceAnchor.widthProperty());


        }catch (IOException e){
            e.printStackTrace();
        }
        manager.initController(fxmlLoader, user, null, ControllerType.CHAT);
        mainPageController=null;
        switchInterfaceAnchor.getChildren().add(node);
    }

    @FXML
    void handleMessages(ActionEvent event) {
        initializeChatPageController();
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
        initializeMainPageController();
        redDotImage.setVisible(false);
        NotifCountLabel.setVisible(false);
        numberOfNotifications=0;
        if(mainPageController!=null) mainPageController.handleNotificationButton();
    }

    @FXML
    public void handleFriendsButton(ActionEvent event) {
        initializeMainPageController();
        if(mainPageController!=null) mainPageController.handleFriendsButton();
    }

    @FXML
    public void handleHomeButton(ActionEvent event) {
        initializeMainPageController();
        if(mainPageController!=null) mainPageController.handleHomeButton();
    }

    @Override
    public void update(FriendEntityChangeEvent event) {
        if (event.getType().equals(ChangeEventType.REQUEST)) {
            if (Objects.equals(event.getFriend().second(), user.getId())) {
                numberOfNotifications++;
                redDotImage.setVisible(true);
                NotifCountLabel.setVisible(true);
                NotifCountLabel.setText(numberOfNotifications + "");
            }
        }
        if(mainPageController!=null) mainPageController.update(event);
    }
}