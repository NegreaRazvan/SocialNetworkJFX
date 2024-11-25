package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import map.domain.User;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class ChatController extends Controller{
    User friend;
    User user;
    @FXML
    ScrollPane scrollFriends;
    @FXML
    ScrollPane scrollChats;
    @FXML
    TextField friendName;
    @FXML
    TextField friendUsername;
    @FXML
    HBox friendHBox;
    @FXML
    VBox friendListChat;
    @FXML
    TextField searchFriend;
    @FXML
    ToggleGroup toggleGroup;

    public void setFriend(User friend) {
        this.friend = friend;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void initObject(User friend,VBox container, String fxmlFile, ControllerType controllerType) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(container.widthProperty());


        }catch (IOException e){
            e.printStackTrace();
        }
        final ToggleButton toggleButton= (ToggleButton) node.getChildren().get(0);
        manager.initController(fxmlLoader, user, friend, controllerType);
        toggleButton.setToggleGroup(toggleGroup);
        toggleButton.setOnMouseEntered(event -> {
            toggleButton.setStyle("-fx-background-color : #808080");
        });
        toggleButton.setOnMouseExited(event -> {
            toggleButton.setStyle("-fx-background-color : black");
        });
        toggleButton.setOnAction(event -> {
            toggleButton.setStyle("-fx-background-color : #808080");
        });
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


    public void initializeWindow(User user, User friend) {
        toggleGroup=new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selected = (ToggleButton) toggleGroup.getSelectedToggle();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-show.fxml"));
                AnchorPane node = null;
                try {
                    node = fxmlLoader.load();
                    node.prefWidthProperty().bind(friendHBox.widthProperty());
                }catch (IOException e){
                    e.printStackTrace();
                }
                manager.initController(fxmlLoader, user, manager.getUser(selected.getText()), ControllerType.FRIENDSHOWCHAT);
                friendHBox.getChildren().add(node);
            } else {
                friendHBox.getChildren().clear();
            }
        });
        setFriend(friend);
        setUser(user);
        searchFriend.textProperty().addListener(o -> handleFilter());
        updateContainer(manager.getFriendsOfUser(user.getId()), friendListChat, 20, "friend-show-chat-list.fxml", ControllerType.FRIENDSHOWCHATLIST);
//        scrollFriends.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
//        scrollChats.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
    }

    public void handleFilter(){
        Predicate<User> p1 = user -> ((user.getLastName() + " " + user.getLastName()).startsWith(searchFriend.getText()));
        List<User> filteredList = manager.getFriendsOfUser(user.getId())
                .stream()
                .filter(p1)
                .toList();
        updateContainer(filteredList, friendListChat, 20, "friend-show-chat-list.fxml", ControllerType.FRIENDSHOWCHATLIST);
    }

}
