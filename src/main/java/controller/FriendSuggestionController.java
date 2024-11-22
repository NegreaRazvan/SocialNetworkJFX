package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import map.domain.User;

public class FriendSuggestionController extends Controller {
    User friend;
    User user;
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;
    @FXML
    Button friendSuggestionButton;

    public void setFriend(User friend) {
        this.friend = friend;
    }
    public void setUser(User user) {
        this.user = user;
    }


    public void initializeWindow(User user, User friend) {
        setFriend(friend);
        setUser(user);
        friendName.setText(friend.getLastName() + " " + friend.getFirstName());
        friendSuggestionButton.setOnMouseEntered(event -> {
            friendSuggestionButton.setStyle("-fx-background-color : #7EF4CC");
        });
        friendSuggestionButton.setOnMouseExited(event -> {
            friendSuggestionButton.setStyle("-fx-background-color : #5a9e96");
        });
        friendUsername.setText(friend.getUsername());
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        manager.sendFriendRequest(user.getId(),friend.getId());

    }
}