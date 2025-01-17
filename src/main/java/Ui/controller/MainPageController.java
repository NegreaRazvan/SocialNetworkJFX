package Ui.controller;

import Ui.socialnetworkjfx.HelloApplication;
import Utils.events.ChangeEventType;
import Utils.events.FriendEntityChangeEvent;
import Utils.observer.Observer;
import Utils.paging.Pageable;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MainPageController extends Controller {
    int maxPage;
    Integer currentElements=0;
    User user;
    ArrayList<User> friendsOfUser;
    ArrayList<User> nonFriendsOfUser;
    ArrayList<User> notificationsOfUser;
    @FXML
    VBox friendsVBox;
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

    public void initializeWindow(User user) {
        scroller.addEventFilter(ScrollEvent.SCROLL, e->{
            if(e.getDeltaY()<0&&currentElements<Math.floor((double) maxPage /9))
                currentElements++;
            else if(e.getDeltaY()>0&&currentElements>0) currentElements--;
            friendsOfUser = manager.getFriendsOfUser(new Pageable(currentElements, 9), user.getId());
            updateContainer(friendsOfUser, friendsScrollPane, 20, "friend-delete.fxml", ControllerType.FRIENDLIST);
        });
        maxPage=manager.getCountFriends(user.getId());
        setUser(user);

        ///makes it so when I search it filters the friend suggestion list
        searchField.textProperty().addListener(o -> handleFIlter());
        ///gets all the important lists that are going to be used
        nonFriendsOfUser = manager.getNonFriendsOfUser(user.getId());
        friendsOfUser = manager.getFriendsOfUser(new Pageable(0, 9),user.getId());
        notificationsOfUser = manager.getFriendRequestsOfUser(user.getId());
        ///updates the friend suggestion Window
        updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
        //scroller.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());

    }

    public void handleNotificationButton() {
        updateContainer(notificationsOfUser, friendsScrollPane, 9, "notifications.fxml", ControllerType.NOTIFICATION);
    }

    public void handleFriendsButton() {
        updateContainer(friendsOfUser, friendsScrollPane, 20, "friend-delete.fxml", ControllerType.FRIENDLIST);
    }

    public void handleHomeButton( ) {
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


    public void update(FriendEntityChangeEvent event) {
        if(event.getType().equals(ChangeEventType.DECLINE)||event.getType().equals(ChangeEventType.ADD)||event.getType().equals(ChangeEventType.DELETE)||event.getType().equals(ChangeEventType.REQUEST)) {
            maxPage = manager.getCountFriends(user.getId());
            nonFriendsOfUser = manager.getNonFriendsOfUser(user.getId());
            friendsOfUser = manager.getFriendsOfUser(new Pageable(currentElements, 9), user.getId());
            notificationsOfUser = manager.getFriendRequestsOfUser(user.getId());
        }
        if (event.getType().equals(ChangeEventType.REQUEST)) {
            updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
        }
        if(event.getType().equals(ChangeEventType.DECLINE)||event.getType().equals(ChangeEventType.ADD))
            if(Objects.equals(event.getFriend().second(), user.getId())) {
                updateContainer(notificationsOfUser, friendsScrollPane, 9, "notifications.fxml", ControllerType.NOTIFICATION);
                updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
            }

        if(event.getType().equals(ChangeEventType.DELETE)) {
            updateContainer(nonFriendsOfUser, friendsVBox, 3, "friend-suggestion.fxml", ControllerType.FRIENDSUGGESTION);
            updateContainer(friendsOfUser, friendsScrollPane, 20, "friend-delete.fxml", ControllerType.FRIENDLIST);
        }
    }

}
