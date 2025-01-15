package Ui.controller;

import Utils.paging.Pageable;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import layers.domain.User;

import java.io.File;

public class ProfileController extends Controller {

    User user;
    @FXML
    Label username;
    @FXML
    Label name;
    @FXML
    Label numberOfFriends;
    @FXML
    ImageView profileImage;


    public void setUser(User user) {
        this.user = user;
    }

    public void initializeWindow(User user) {
        if(user.getProfilePicture() != null) {
            File imageFile = new File(user.getProfilePicture());
            System.out.println(imageFile.toURI());
            Image image = new Image(imageFile.toURI().toString());
            profileImage.setImage(image);
        }
        setUser(user);
        username.setText(user.getUsername());
        name.setText(user.getLastName()+ " " + user.getFirstName());
        numberOfFriends.setText("Number of friends: " + manager.getCountFriends(user.getId()));
        Rectangle clip=new Rectangle(profileImage.getFitWidth(),profileImage.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        profileImage.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = profileImage.snapshot(parameters, null);
        profileImage.setClip(null);

        profileImage.setImage(image);

    }
}
