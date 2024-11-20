package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FriendSuggestionController extends Controller {
    Long friendId;
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

     public void initializeFriendCard(Long friendId) {
        setFriendId(friendId);
         friendName.setText(manager.getUser(friendId).getLastName() + " " + manager.getUser(friendId).getFirstName());
         friendUsername.setText(manager.getUser(friendId).getUsername());
     }
}
