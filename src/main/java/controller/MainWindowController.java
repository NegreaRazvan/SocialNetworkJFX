package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainWindowController extends Controller {
    String username;
    @FXML
    Label usernameLabel;
    @FXML
    Label nameLabel;
    @FXML
    AnchorPane friendAnchorPane;



    public void setUsername(String username) {
        this.username = username;
    }

    public void initializeMainWindow(){
        usernameLabel.setText(username);
        nameLabel.setText(manager.getUser(username).getLastName() + " " + manager.getUser(username).getFirstName());
//        Node[] nodes = new Node[3];
//        for (int i = 0; i < nodes.length; i++) {
//            try {
//
//                final int j = i;
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("friend-suggestion.fxml"));
//                FriendSuggestionController friendSuggestionController = fxmlLoader.load();
//                nodes[i] = FXMLLoader.load(getClass().getResource("friend-suggestion.fxml"));
//
//                nodes[i].setOnMouseEntered(event -> {
//                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
//                });
//                nodes[i].setOnMouseExited(event -> {
//                    nodes[j].setStyle("-fx-background-color : #02030A");
//                });
//                friendAnchorPane.getChildren().add(nodes[i]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @FXML
    public void handleSignout(ActionEvent event) {
        manager.switchPage("login.fxml", "Log In");
    }
}
