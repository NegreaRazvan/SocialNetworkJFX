package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FriendSuggestionController extends Controller {
    Long friendId;
    Long userId;
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;
    @FXML
    Button friendSuggestionButton;

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
        friendSuggestionButton.setOnMouseEntered(event -> {
            friendSuggestionButton.setStyle("-fx-background-color : #7EF4CC");
        });
        friendSuggestionButton.setOnMouseExited(event -> {
            friendSuggestionButton.setStyle("-fx-background-color : #5a9e96");
        });
        friendUsername.setText(manager.getUser(friendId).getUsername());
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        manager.sendFriendRequest(userId,friendId);

    }
}