package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import map.domain.User;

public class FriendShowOnChatListController extends Controller{
    @FXML
    Label friendName;
    @FXML
    Label friendUsername;
    @FXML
    ImageView friendImage;
    @FXML
            ToggleButton friendToggleButton;
    User friend;

    public void setFriend(User friend) {
        this.friend = friend;
    }


    public void setTextFriend(User friend) {
        friendToggleButton.setText(friend.getUsername());
        friendToggleButton.setStyle("-fx-text-fill: transparent; -fx-background-color: transparent;");
        friendImage.setMouseTransparent(true);
        friendName.setMouseTransparent(true);
        friendUsername.setMouseTransparent(true);
        friendName.setText(friend.getLastName() + " " + friend.getFirstName());
        friendUsername.setText(friend.getUsername());
    }

    public void initializeWindow(User friend) {
        setFriend(friend);
        setTextFriend(friend);
    }
}
