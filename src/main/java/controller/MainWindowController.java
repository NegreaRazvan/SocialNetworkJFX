package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MainWindowController extends Controller {
    String username;
    @FXML
    Label usernameLabel;
    @FXML
    Label nameLabel;
    @FXML
    VBox friendsVBox;



    public void setUsername(String username) {
        this.username = username;
    }

    public void initializeMainWindow(){
        usernameLabel.setText(username);
        nameLabel.setText(manager.getUser(username).getLastName() + " " + manager.getUser(username).getFirstName());
        friendsVBox.getChildren().clear();
        Node[] nodes = new Node[3];
        List<Long> nonFriends=manager.getNonFriendsOfUser(manager.getUser(username).getId());
        for (int i = 0; i < nodes.length; i++) {
            try {
                final int j = i;

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-suggestion.fxml"));
                nodes[i] = fxmlLoader.load();

                manager.initController(fxmlLoader, nonFriends.get(j));

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });
                friendsVBox.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleSignout(ActionEvent event) {
        manager.switchPage("login.fxml", "Log In");
    }
}
