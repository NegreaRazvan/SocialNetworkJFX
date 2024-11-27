package controller;

import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import map.domain.*;
import javafx.scene.image.ImageView;
import map.events.ChangeEventType;
import map.events.FriendEntityChangeEvent;

import javax.swing.text.Element;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import map.observer.Observer;

public class ChatController extends Controller implements Observer<FriendEntityChangeEvent> {
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
    @FXML
    ScrollPane scrollMessages;

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
//        HBox BighBox = new HBox();
//        StackPane stackPane = new StackPane();
//        stackPane.setAlignment(Pos.CENTER_LEFT);
//        stackPane.setPadding(new Insets(5,5,5,10));
//        ToggleButton toggleButton = new ToggleButton();
//        toggleButton.setText(String.valueOf(message.getId()));
//        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
//        toggleButton.setToggleGroup(toggleGroupForReplies);
//        toggleButton.prefWidthProperty().bind(stackPane.widthProperty());
//        toggleButton.prefHeightProperty().bind(stackPane.heightProperty());
//        Text text= new Text(message.getMessage());
//        TextFlow textFlow = new TextFlow(text);
//        String style = "-fx-color: rgb(233,233,235); " +
//                "-fx-background-color: rgb(0,255,0); " +
//                "-fx-background-radius: 20px;";
//        textFlow.setStyle(style);
//        textFlow.setPadding(new Insets(5,5,5,10));
//        text.setFill(Color.color(0.934,0.945,0.996));
//        textFlow.setMouseTransparent(true);
//        stackPane.getChildren().addAll(toggleButton, textFlow);
//        BighBox.getChildren().add(stackPane);
//        container.getChildren().add(BighBox);

        HBox BighBox = new HBox();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);
        stackPane.setPadding(new Insets(5,5,5,10));

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(message.getMessage());
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);
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

    public void receivedMessageReply(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(Pos.CENTER_LEFT);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_LEFT);

        StackPane stackPaneReply = new StackPane();
        stackPaneReply.setAlignment(Pos.CENTER_LEFT);

        Button jumpToMessage = new Button();
        jumpToMessage.setText(String.valueOf(((ReplyMessageDTO)message).getIdReplyMessage()));
        jumpToMessage.setStyle("-fx-text-fill: black; -fx-background-color : black");
        jumpToMessage.setOnMouseClicked(event -> {
            System.out.println(jumpToMessage.getText());
        });
        jumpToMessage.prefWidthProperty().bind(stackPaneReply.widthProperty());
        jumpToMessage.prefHeightProperty().bind(stackPaneReply.heightProperty());


        Text textReply= new Text(((ReplyMessageDTO)message).getMsg());
        TextFlow replyMessage = new TextFlow(textReply);
        String style1 = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: rgb(0,150,0); " +
                "-fx-background-radius: 20px;";
        replyMessage.setStyle(style1);
        textReply.setFill(Color.color(0.934,0.945,0.996));
        replyMessage.setPadding(new Insets(5,5,5,10));
        replyMessage.setMouseTransparent(true);

        stackPaneReply.getChildren().addAll(jumpToMessage, replyMessage);
        HBox stackContainer= new HBox();
        stackContainer.setAlignment(Pos.CENTER_LEFT);
        stackContainer.setPadding(new Insets(5,5,5,10));
        stackContainer.getChildren().addAll(stackPaneReply);
        vBox.getChildren().add(stackContainer);



        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);


        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style2 = "-fx-color: rgb(239,242,255); " +
                "-fx-background-color: rgb(0,255,0); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style2);
        textFlow.setPadding(new Insets(5,5,5,10));
        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);

        toggleButton.prefWidthProperty().bind(textFlow.widthProperty());
        toggleButton.prefHeightProperty().bind(textFlow.heightProperty());

        stackPane.getChildren().addAll(toggleButton, textFlow);
        HBox stackContainer1= new HBox();
        stackContainer1.setAlignment(Pos.CENTER_LEFT);
        stackContainer1.setPadding(new Insets(5,5,5,10));
        stackContainer1.getChildren().addAll(stackPane);
        vBox.getChildren().add(stackContainer1);


        BighBox.getChildren().add(vBox);
        container.getChildren().add(BighBox);
    }

    public void sentMessage(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(Pos.CENTER_RIGHT);
        BighBox.setPadding(new Insets(5,10,5,10));

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_RIGHT);

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);
        toggleButton.prefWidthProperty().bind(stackPane.widthProperty());
        toggleButton.prefHeightProperty().bind(stackPane.heightProperty());

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


        BighBox.getChildren().add(stackPane);
        container.getChildren().add(BighBox);
    }

    public void sentMessageReply(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(Pos.CENTER_RIGHT);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_RIGHT);

        StackPane stackPaneReply = new StackPane();
        stackPaneReply.setAlignment(Pos.CENTER_RIGHT);

        Button jumpToMessage = new Button();
        jumpToMessage.setText(String.valueOf(((ReplyMessageDTO)message).getIdReplyMessage()));
        jumpToMessage.setStyle("-fx-text-fill: black; -fx-background-color : black");
        jumpToMessage.setOnMouseClicked(event -> {
            System.out.println(jumpToMessage.getText());
        });
        jumpToMessage.prefWidthProperty().bind(stackPaneReply.widthProperty());
        jumpToMessage.prefHeightProperty().bind(stackPaneReply.heightProperty());


        Text textReply= new Text(((ReplyMessageDTO)message).getMsg());
        TextFlow replyMessage = new TextFlow(textReply);
        String style1 = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: rgb(0,0,255); " +
                "-fx-background-radius: 20px;";
        replyMessage.setStyle(style1);
        textReply.setFill(Color.color(0.934,0.945,0.996));
        replyMessage.setPadding(new Insets(5,10,5,10));
        replyMessage.setMouseTransparent(true);


        stackPaneReply.getChildren().addAll(jumpToMessage, replyMessage);
        HBox stackContainer= new HBox();
        stackContainer.setAlignment(Pos.CENTER_RIGHT);
        stackContainer.setPadding(new Insets(5,10,5,10));
        stackContainer.getChildren().addAll(stackPaneReply);
        vBox.getChildren().add(stackContainer);



        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_RIGHT);

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);
        toggleButton.prefWidthProperty().bind(stackPane.widthProperty());
        toggleButton.prefHeightProperty().bind(stackPane.heightProperty());

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
        HBox stackContainer1= new HBox();
        stackContainer1.setAlignment(Pos.CENTER_RIGHT);
        stackContainer1.setPadding(new Insets(5,10,5,10));
        stackContainer1.getChildren().addAll(stackPane);
        vBox.getChildren().add(stackContainer1);


        BighBox.getChildren().add(vBox);
        container.getChildren().add(BighBox);
    }

    @FXML
    void handleSendMessage(ActionEvent event) {
        if(!sendField.getText().isEmpty()) {
            if(toggleGroupForReplies.getSelectedToggle()!=null) {
//                var username = ((Label)c.getChildren().get(2)).getText();
                ((ToggleButton)toggleGroup.getSelectedToggle()).getText();
                manager.sendMessage(user.getId(), manager.getUser( ((ToggleButton)toggleGroup.getSelectedToggle()).getText()).getId(), sendField.getText(), ((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText(), manager.getUser(((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText()).getId());
            }
            else {
                manager.sendMessage(user.getId(), manager.getUser( ((ToggleButton)toggleGroup.getSelectedToggle()).getText()).getId(), sendField.getText(), null, null );
                System.out.println("is empty");
            }
        }
    }

    public void updateContainer(List<MessageDTO> messages, VBox container) {
        container.getChildren().clear();
        for (int i = 0; i < messages.size(); i++)
            if(messages.get(i).getTo().equals(user.getId()))
                if(messages.get(i) instanceof ReplyMessageDTO)
                    receivedMessageReply(messages.get(i), container);
                else
                    receivedMessage(messages.get(i), container);
            else
                if(messages.get(i) instanceof ReplyMessageDTO)
                    sentMessageReply(messages.get(i), container);
                else
                    sentMessage(messages.get(i),container);


    }

    private void smoothScroll(double targetValue) {

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(scrollMessages.vvalueProperty(), targetValue, Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
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
        Platform.runLater(() -> scrollMessages.setVvalue(1.0));
    }

    public void initializeWindow(User user, User friend, Stage stage) {
        manager.addObserverChatWindow(this);
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


    @Override
    public void update(FriendEntityChangeEvent event) {
        if(event.getType().equals(ChangeEventType.MESSAGESENT)){
            var list = manager.getSentMessages(event.getMessage().getTo(), event.getMessage().getFrom());
            list.sort(Comparator.comparingLong(Entity::getId));
            updateContainer(list, messages);
        }
    }
}
