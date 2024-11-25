package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import map.domain.User;

public class ChatController extends Controller{
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
    }

}
