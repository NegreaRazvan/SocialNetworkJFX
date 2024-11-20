package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import map.domain.Friend;
import map.events.FriendEntityChangeEvent;
import map.observer.Observer;

public class FriendListController extends Controller {

    Long friendId;
    Long userId;
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;
    @FXML
    Button deleteButton;

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public void initializeFriendCard(Long userId,Long friendId) {
        setUserId(userId);
        setFriendId(friendId);
        friendName.setText(manager.getUser(friendId).getLastName() + " " + manager.getUser(friendId).getFirstName());
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setStyle("-fx-background-color : #7EF4CC");
        });
        deleteButton.setOnMouseExited(event -> {
            deleteButton.setStyle("-fx-background-color : #5a9e96");
        });
        friendUsername.setText(manager.getUser(friendId).getUsername());
    }


}
