package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import map.domain.*;
import javafx.scene.image.ImageView;

import javax.swing.text.Element;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class ChatController extends Controller{
    Stage primaryStage;
    User friend;
    User user;
    @FXML
    ScrollPane scrollFriends;
    @FXML
    ScrollPane scrollChats;
    @FXML
    TextField friendName;
    @FXML
    TextField friendUsername;
    @FXML
    HBox friendHBox;
    @FXML
    VBox friendListChat;
    @FXML
    TextField searchFriend;
    @FXML
    ToggleGroup toggleGroup;
    @FXML
    ToggleGroup toggleGroupForReplies;
    @FXML
    AnchorPane sendMessage;
    @FXML
    TextField sendField;
    @FXML
    Button sendButton;
    @FXML
    VBox messages;

    public void setFriend(User friend) {
        this.friend = friend;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void initObject(User friend,VBox container, String fxmlFile, ControllerType controllerType) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(container.widthProperty());


        }catch (IOException e){
            e.printStackTrace();
        }
        final ToggleButton toggleButton= (ToggleButton) node.getChildren().get(0);
        manager.initController(fxmlLoader, user, friend, controllerType);
        toggleButton.setToggleGroup(toggleGroup);
        toggleButton.setOnMouseEntered(event -> {
            toggleButton.setStyle("-fx-background-color : #808080");
        });
        toggleButton.setOnMouseExited(event -> {
            toggleButton.setStyle("-fx-background-color : black");
        });
        toggleButton.setOnAction(event -> {
            toggleButton.setStyle("-fx-background-color : #808080");
        });
        container.getChildren().add(node);
    }

    public void updateContainer(List<User> friends, VBox container, Integer numberOfObjects, String fxmlFile, ControllerType controllerType) {
        Integer length = numberOfObjects;
        container.getChildren().clear();
        if (friends.size() < numberOfObjects)
            length = friends.size();
        for (int i = 0; i < length; i++)
            initObject(friends.get(i), container, fxmlFile, controllerType);
    }

    public void receivedMessage(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);
        stackPane.setPadding(new Insets(5,5,5,10));

//        Image image = new Image("C://Users//Razvan//IdeaProjects//SocialNetworkGUI//src//main//resources//com//beginsecure//socialnetworkjfx//images//Plus.png");
//        ImageView imageView=new ImageView(image);
        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(message.getMessage());
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);
//        toggleButton.setGraphic(imageView);
        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: rgb(0,255,0); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style);
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);
        stackPane.getChildren().addAll(toggleButton, textFlow);
        BighBox.getChildren().add(stackPane);
        container.getChildren().add(BighBox);
    }

    public void sentMessage(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        stackPane.setPadding(new Insets(5,5,5,10));
        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(message.getMessage());
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);
        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style = "-fx-color: rgb(239,242,255); " +
                "-fx-background-color: rgb(15,125,242); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style);
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);
        stackPane.getChildren().addAll(toggleButton, textFlow);
        BighBox.getChildren().add(stackPane);
        container.getChildren().add(BighBox);
    }

    public void sentMessageReply(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(Pos.CENTER_RIGHT);
        BighBox.setPadding(new Insets(5,5,5,10));
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(5,5,5,10));
        Text textReply= new Text(((ReplyMessageDTO)message).getMsg());
        TextFlow replyMessage = new TextFlow(textReply);
        String style1 = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: rgb(0,0,255); " +
                "-fx-background-radius: 20px;";
        replyMessage.setStyle(style1);
        replyMessage.setPadding(new Insets(5,10,5,10));
        textReply.setFill(Color.color(0.934,0.945,0.996));
        replyMessage.setMouseTransparent(true);
        vBox.getChildren().add(replyMessage);



        HBox hBox = new HBox();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        stackPane.setPadding(new Insets(5,5,5,10));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5,5,5,10));
        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(message.getMessage());
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);

        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style2 = "-fx-color: rgb(239,242,255); " +
                "-fx-background-color: rgb(15,125,242); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style2);
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);
        stackPane.getChildren().addAll(toggleButton, textFlow);
        vBox.getChildren().add(stackPane);
        BighBox.getChildren().add(vBox);
        container.getChildren().add(BighBox);
    }

    public void updateContainer(List<MessageDTO> messages, VBox container) {
        container.getChildren().clear();
        for (int i = 0; i < messages.size(); i++)
            if(messages.get(i).getTo().equals(user.getId()))
                receivedMessage(messages.get(i), container);
            else
                if(messages.get(i) instanceof ReplyMessageDTO)
                    sentMessageReply(messages.get(i), container);
                else
                    sentMessage(messages.get(i),container);

    }



    public void handleToggleButtonPress(){
        ToggleButton selected = (ToggleButton) toggleGroup.getSelectedToggle();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend-show.fxml"));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            node.prefWidthProperty().bind(friendHBox.widthProperty());
        }catch (IOException e){
            e.printStackTrace();
        }
        User friend=manager.getUser(selected.getText());
        manager.initController(fxmlLoader, user, friend, ControllerType.FRIENDSHOWCHAT);
        friendHBox.getChildren().clear();
        friendHBox.getChildren().add(node);
        var list = manager.getSentMessages(user.getId(), friend.getId());
        list.sort(Comparator.comparingLong(Entity::getId));
        sendMessage.setVisible(true);
        sendMessage.setLayoutY(primaryStage.getHeight() - 2.5*sendMessage.getHeight());
        updateContainer(list, messages);
    }

    public void initializeWindow(User user, User friend, Stage stage) {
        sendMessage.setVisible(false);
        primaryStage = stage;
        sendMessage.setLayoutY(primaryStage.getHeight() - 2.5*sendMessage.getHeight());
        primaryStage.heightProperty().addListener((_, _, newValue) -> {
            System.out.println(primaryStage.getHeight());
            sendMessage.setLayoutY(newValue.doubleValue() - 2.5*sendMessage.getHeight());
        });
        toggleGroupForReplies=new ToggleGroup();
        toggleGroupForReplies.selectedToggleProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                System.out.println(((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText());
            } else {

            }
        });
        toggleGroup=new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                handleToggleButtonPress();
            } else {
                sendMessage.setVisible(false);
                friendHBox.getChildren().clear();
                messages.getChildren().clear();
            }
        });
        setFriend(friend);
        setUser(user);
        searchFriend.textProperty().addListener(o -> handleFilter());
        updateContainer(manager.getFriendsOfUser(user.getId()), friendListChat, 20, "friend-show-chat-list.fxml", ControllerType.FRIENDSHOWCHATLIST);
//        scrollFriends.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
//        scrollChats.getScene().getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
    }

    public void handleFilter(){
        Predicate<User> p1 = user -> ((user.getLastName() + " " + user.getLastName()).startsWith(searchFriend.getText()));
        List<User> filteredList = manager.getFriendsOfUser(user.getId())
                .stream()
                .filter(p1)
                .toList();
        updateContainer(filteredList, friendListChat, 20, "friend-show-chat-list.fxml", ControllerType.FRIENDSHOWCHATLIST);
    }

}
