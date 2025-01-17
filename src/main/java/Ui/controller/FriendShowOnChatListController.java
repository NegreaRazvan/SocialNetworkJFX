package Ui.controller;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import layers.domain.User;

import java.io.File;

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
        if(friend.getProfilePicture() != null) {
            File imageFile = new File(friend.getProfilePicture());
            System.out.println(imageFile.toURI());
            Image image = new Image(imageFile.toURI().toString());
            friendImage.setImage(image);
        }

        Rectangle clip=new Rectangle(friendImage.getFitWidth(),friendImage.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        friendImage.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = friendImage.snapshot(parameters, null);
        friendImage.setClip(null);

        friendImage.setImage(image);
    }

    public void initializeWindow(User friend) {
        setFriend(friend);
        setTextFriend(friend);
    }
}
