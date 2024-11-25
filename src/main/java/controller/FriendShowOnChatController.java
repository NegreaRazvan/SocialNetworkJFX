package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import map.domain.User;

public class FriendShowOnChatController extends Controller{
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;
    User friend;

    public void setFriend(User friend) {
        this.friend = friend;
    }


    public void setTextFriend(User friend) {
        friendName.setText(friend.getLastName() + " " + friend.getFirstName());
        friendUsername.setText(friend.getUsername());
    }

    public void initializeWindow(User friend) {
        setFriend(friend);
        setTextFriend(friend);
    }
}
