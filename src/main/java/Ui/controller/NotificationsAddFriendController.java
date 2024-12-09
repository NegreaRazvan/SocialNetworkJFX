package Ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import layers.domain.User;
import Utils.events.ChangeEventType;

public class NotificationsAddFriendController extends Controller{
    User user;
    User friend;
    @FXML
    Label nameLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Button addFriendButton;
    @FXML
    Button declineFriendButton;
    @FXML
    Label dateLabel;

    public void setFriend(User friend) {
        this.friend = friend;
    }
    public void setUser(User user) {
        this.user = user;
    }


    public void initializeWindow(User user, User friend) {
        setUser(user);
        setFriend(friend);
        dateLabel.setText(manager.getFriendRequest(user.getId(),friend.getId()).date().toString());
        nameLabel.setText(friend.getLastName() + " " + friend.getFirstName());
        addFriendButton.setOnMouseEntered(event -> {
            addFriendButton.setStyle("-fx-background-color : #7EF4CC");
        });
        addFriendButton.setOnMouseExited(event -> {
            addFriendButton.setStyle("-fx-background-color : #5a9e96");
        });
        declineFriendButton.setOnMouseEntered(event -> {
            declineFriendButton.setStyle("-fx-background-color : #7EF4CC");
        });
        declineFriendButton.setOnMouseExited(event -> {
            declineFriendButton.setStyle("-fx-background-color : #5a9e96");
        });
        usernameLabel.setText(friend.getUsername());
    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent event) {
        manager.deleteOrDeclineFriend(user.getId(), friend.getId(), ChangeEventType.DECLINE);
    }

    @FXML
    public void handleAddFriendButtonAction(ActionEvent event) {
        manager.acceptFriendRequest(user.getId(),friend.getId());
    }

}
