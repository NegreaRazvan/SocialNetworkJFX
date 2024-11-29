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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import map.domain.*;
import map.events.ChangeEventType;
import map.events.FriendEntityChangeEvent;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import map.observer.Observer;

public class ChatController extends Controller implements Observer<FriendEntityChangeEvent> {
    Stage primaryStage;
    User friend;
    User user;
    @FXML
    ScrollPane scrollFriends;
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
            toggleButton.setStyle("-fx-background-color : #808080; -fx-text-fill: transparent;");
        });
        toggleButton.setOnMouseExited(event -> {
            toggleButton.setStyle("-fx-background-color : black; -fx-text-fill: transparent;");
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

    public void handleScrollToTheMessage(Button jumpToMessage, VBox container) {
        for (Node node : container.getChildren()) {
            if (node instanceof HBox) {
                if(((HBox) node).getChildren().get(0).getAccessibleText().equals("message")) {
                    StackPane stackPane1 = (StackPane) ((HBox) node).getChildren().get(0);
                    ToggleButton button = (ToggleButton) stackPane1.getChildren().get(0);
                    TextFlow textFlow1=(TextFlow) stackPane1.getChildren().get(1);
                    if (button.getText().equals(jumpToMessage.getText())) {
                        smoothScroll(node.getBoundsInParent().getMinY()/container.getHeight());
                        addGlowAnimation(textFlow1);
                        break;
                    }
                }else {
                    StackPane vBox1=(StackPane) ((HBox) node).getChildren().get(0);
                    HBox hbox2=(HBox) vBox1.getChildren().get(1);
                    StackPane stackPane2 = (StackPane) hbox2.getChildren().get(0);
                    ToggleButton button = (ToggleButton) stackPane2.getChildren().get(0);
                    TextFlow textFlow1=(TextFlow) stackPane2.getChildren().get(1);
                    if (button.getText().equals(jumpToMessage.getText())) {
                        smoothScroll(node.getBoundsInParent().getMinY()/container.getHeight());
                        addGlowAnimation(textFlow1);
                        break;
                    }
                }
            }
        }
    }

    public void handleMessage(MessageDTO message, VBox container, Pos pos,String style){
        HBox BighBox = new HBox();
        BighBox.setAlignment(pos);
        BighBox.setPadding(new Insets(5,10,5,10));

        StackPane stackPane = new StackPane();
        messageStructure(message, stackPane , style);

        BighBox.getChildren().add(stackPane);
        container.getChildren().add(BighBox);
    }

    public void replyStructure(MessageDTO message,VBox container, StackPane stackPane){

        Button jumpToMessage = new Button();
        jumpToMessage.setText(String.valueOf(((ReplyMessageDTO)message).getIdReplyMessage()));
        jumpToMessage.setStyle("-fx-text-fill: black; -fx-background-color : transparent;");
        jumpToMessage.prefWidthProperty().bind(stackPane.widthProperty());
        jumpToMessage.prefHeightProperty().bind(stackPane.heightProperty());

        jumpToMessage.setOnMouseClicked(event -> {
            handleScrollToTheMessage( jumpToMessage,  container);
        });


        Text textReply= new Text(((ReplyMessageDTO)message).getMsg());
        TextFlow replyMessage = new TextFlow(textReply);
        String style = "-fx-background-color: rgb(105,105,105); " +
                "-fx-background-radius: 20px;";
        replyMessage.setStyle(style);
        textReply.setFill(Color.LIGHTGREY);
        replyMessage.setPadding(new Insets(5,10,5,10));
        replyMessage.setMouseTransparent(true);


        stackPane.getChildren().addAll(jumpToMessage, replyMessage);
    }

    public void messageStructure(MessageDTO message,StackPane stackPane,String style) {
        stackPane.setAccessibleText("message");

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : transparent;");
        toggleButton.setToggleGroup(toggleGroupForReplies);
        toggleButton.prefWidthProperty().bind(stackPane.widthProperty());
        toggleButton.prefHeightProperty().bind(stackPane.heightProperty());

        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(style);
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.WHITE);
        textFlow.setMouseTransparent(true);

        stackPane.getChildren().addAll(toggleButton, textFlow);
    }

    public void handleReply(MessageDTO message,VBox container, Pos pos,String style) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(pos);

        StackPane stackPaneThatContainsTheReplyAndTheMessage = new StackPane();
        stackPaneThatContainsTheReplyAndTheMessage.setAccessibleText("reply");
        stackPaneThatContainsTheReplyAndTheMessage.setAlignment(pos);

        StackPane stackPaneReply = new StackPane();
        replyStructure(message, container, stackPaneReply);
        TextFlow replyMessage= (TextFlow) stackPaneReply.getChildren().get(1);

        HBox stackContainer= new HBox();
        stackContainer.setAlignment(pos);
        stackContainer.setPadding(new Insets(5,10,5,10));
        stackContainer.getChildren().addAll(stackPaneReply);


        stackPaneThatContainsTheReplyAndTheMessage.getChildren().add(stackContainer);



        StackPane stackPane = new StackPane();
        messageStructure(message,stackPane,style);
        TextFlow textFlow = (TextFlow) stackPane.getChildren().get(1);

        HBox stackContainer1= new HBox();
        stackContainer1.setAlignment(pos);
        stackContainer1.setPadding(new Insets(5,10,5,10));
        stackContainer1.getChildren().addAll(stackPane);


        stackPaneThatContainsTheReplyAndTheMessage.getChildren().add(stackContainer1);

        stackPaneThatContainsTheReplyAndTheMessage.setMinHeight(0);
        stackPaneThatContainsTheReplyAndTheMessage.setMaxHeight(0);


        Platform.runLater(()->{
            stackContainer1.setTranslateY((double) (0.95 * replyMessage.getBoundsInLocal().getHeight()));
            VBox.setMargin(BighBox, new Insets(0,0,textFlow.getBoundsInLocal().getHeight(),0));
            BighBox.setPadding(new Insets(0,0,0.95 * replyMessage.getBoundsInLocal().getHeight()+10,0));
        });

        BighBox.getChildren().add(stackPaneThatContainsTheReplyAndTheMessage);
        container.getChildren().add(BighBox);
    }

    public void receivedMessage(MessageDTO message, VBox container) {
        String style = "-fx-background-color: 	rgb(60,179,113); " +
                "-fx-background-radius: 20px;";
        handleMessage(message, container, Pos.TOP_LEFT, style);
    }

    public void sentMessage(MessageDTO message, VBox container) {
        String style = "-fx-background-color: 	rgb(30,144,255); " +
                "-fx-background-radius: 20px;";
        handleMessage(message, container, Pos.TOP_RIGHT, style);
    }

    public void receivedMessageReply(MessageDTO message, VBox container) {
        String style = "-fx-background-color: 	rgb(60,179,113); " +
                "-fx-background-radius: 20px;";
        handleReply(message, container, Pos.TOP_LEFT, style);
    }

    public void sentMessageReply(MessageDTO message, VBox container) {
        String style = "-fx-background-color: 	rgb(30,144,255); " +
                "-fx-background-radius: 20px;";
        handleReply(message, container, Pos.TOP_RIGHT, style);

    }

    @FXML
    void handleSendMessage(ActionEvent event) {
        if(!sendField.getText().isEmpty()) {
            if(toggleGroupForReplies.getSelectedToggle()!=null) {
                manager.sendMessage(user.getId(), manager.getUser( ((ToggleButton)toggleGroup.getSelectedToggle()).getText()).getId(), sendField.getText(), manager.getMessage(Long.valueOf(((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText())).getMessage(), manager.getMessage(Long.valueOf(((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText())).getId());
                sendField.setText("");
            }
            else {
                manager.sendMessage(user.getId(), manager.getUser( ((ToggleButton)toggleGroup.getSelectedToggle()).getText()).getId(), sendField.getText(), null, null );
                sendField.setText("");
            }
        }
    }

    public void updateContainer(List<MessageDTO> messagesInOrder, VBox container) {
        container.getChildren().clear();
        for (int i = 0; i < messagesInOrder.size(); i++) {
            if (messagesInOrder.get(i).getTo().equals(user.getId()))
                if (messagesInOrder.get(i) instanceof ReplyMessageDTO)
                    receivedMessageReply(messagesInOrder.get(i), container);
                else
                    receivedMessage(messagesInOrder.get(i), container);
            else if (messagesInOrder.get(i) instanceof ReplyMessageDTO)
                sentMessageReply(messagesInOrder.get(i), container);
            else
                sentMessage(messagesInOrder.get(i), container);
        }

        smoothScroll(messages.getHeight());
    }

    private void smoothScroll(double targetValue) {

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(scrollMessages.vvalueProperty(), targetValue, Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
    }

    public void addGlowAnimation(TextFlow textFlow) {
        Glow glow = new Glow(0.0); // Start with no glow
        textFlow.setEffect(glow);


        Timeline glowAnimation = new Timeline(
                new KeyFrame(Duration.millis(1000), new KeyValue(glow.levelProperty(), 0.0)),
                new KeyFrame(Duration.millis(1500), new KeyValue(glow.levelProperty(), 1.0)),
                new KeyFrame(Duration.millis(2000), new KeyValue(glow.levelProperty(), 0.0))
        );


        glowAnimation.play();
    }

    public void handleToggleButtonPress(){
        System.out.println(messages.getHeight());
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

        sendField.setStyle("-fx-background-color: #313131;-fx-text-fill: white;");

        sendButton.setStyle("-fx-background-color: transparent;");
        manager.addObserverChatWindow(this);
        sendMessage.setVisible(false);
        primaryStage = stage;
        sendMessage.setLayoutY(primaryStage.getHeight() - 2.5*sendMessage.getHeight());
        scrollMessages.prefHeightProperty().bind(primaryStage.heightProperty().subtract(friendHBox.heightProperty()).subtract(85));
        messages.prefHeightProperty().bind(scrollMessages.heightProperty());
        primaryStage.heightProperty().addListener((_, _, newValue) -> {
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
