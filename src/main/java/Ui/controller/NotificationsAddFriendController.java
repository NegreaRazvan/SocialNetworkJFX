package Ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import layers.domain.User;
import Utils.events.ChangeEventType;


import java.io.File;

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
    @FXML
    ImageView friendImage;

    public void setFriend(User friend) {
        this.friend = friend;
    }
    public void setUser(User user) {
        this.user = user;
    }


    public void initializeWindow(User user, User friend) {
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
