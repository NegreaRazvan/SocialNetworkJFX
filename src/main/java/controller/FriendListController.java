package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import map.domain.Friend;
import map.domain.User;
import map.events.FriendEntityChangeEvent;
import map.observer.Observer;

public class FriendListController extends Controller {

    User friend;
    User user;
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;
    @FXML
    Button deleteButton;

    public void setFriend(User friend) {
        this.friend = friend;
    }
    public void setUser(User user) {
        this.user = user;
    }


    public void initializeFriendCard(User user,User friend) {
        setUser(user);
        setFriend(friend);
        friendName.setText(friend.getLastName() + " " + friend.getFirstName());
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setStyle("-fx-background-color : #7EF4CC");
        });
        deleteButton.setOnMouseExited(event -> {
            deleteButton.setStyle("-fx-background-color : #5a9e96");
        });
        friendUsername.setText(friend.getUsername());
    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent event) {
        manager.deleteFriendFromList(user.getId(), friend.getId());
    }


}
