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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

        HBox BighBox = new HBox();
        StackPane stackPane = new StackPane();
        stackPane.setAccessibleText("message");
        stackPane.setAlignment(Pos.CENTER_LEFT);
        stackPane.setPadding(new Insets(5,5,5,10));

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : black");
        toggleButton.setToggleGroup(toggleGroupForReplies);
        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: 	rgb(60,179,113); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style);
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);
        toggleButton.prefWidthProperty().bind(textFlow.widthProperty());
        toggleButton.prefHeightProperty().bind(textFlow.heightProperty());
        stackPane.getChildren().addAll(toggleButton, textFlow);
        BighBox.getChildren().add(stackPane);
        container.getChildren().add(BighBox);
    }

    public void receivedMessageReply(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(Pos.CENTER_LEFT);

        StackPane vBox = new StackPane();
        vBox.setAccessibleText("reply");
        vBox.setAlignment(Pos.CENTER_LEFT);

        StackPane stackPaneReply = new StackPane();
        stackPaneReply.setAlignment(Pos.CENTER_LEFT);

        Button jumpToMessage = new Button();
        jumpToMessage.setText(String.valueOf(((ReplyMessageDTO)message).getIdReplyMessage()));
        jumpToMessage.setStyle("-fx-text-fill: black; -fx-background-color : black");
        jumpToMessage.setOnMouseClicked(event -> {
            System.out.println(jumpToMessage.getText());
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
                        ToggleButton button2 = (ToggleButton) stackPane2.getChildren().get(0);
                        TextFlow textFlow1=(TextFlow) stackPane2.getChildren().get(1);
                        if (button2.getText().equals(jumpToMessage.getText())) {
                            smoothScroll(node.getBoundsInParent().getMinY()/container.getHeight());
                            addGlowAnimation(textFlow1);
                            break;
                        }
                    }
                }
            }
        });
        jumpToMessage.prefWidthProperty().bind(stackPaneReply.widthProperty());
        jumpToMessage.prefHeightProperty().bind(stackPaneReply.heightProperty());


        Text textReply= new Text(((ReplyMessageDTO)message).getMsg());
        TextFlow replyMessage = new TextFlow(textReply);
        String style1 = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: rgb(105,105,105); " +
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
        stackPane.setAlignment(Pos.TOP_LEFT);

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : transparent");
        toggleButton.setToggleGroup(toggleGroupForReplies);


        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style2 = "-fx-color: rgb(239,242,255); " +
                "-fx-background-color: 	rgb(60,179,113); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style2);
        textFlow.setPadding(new Insets(5,5,5,10));
        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);

        toggleButton.prefWidthProperty().bind(textFlow.widthProperty());
        toggleButton.prefHeightProperty().bind(textFlow.heightProperty());

        stackPane.getChildren().addAll(toggleButton, textFlow);
        HBox stackContainer1= new HBox();
        stackContainer1.setAlignment(Pos.TOP_LEFT);
        stackContainer1.setPadding(new Insets(5,5,5,10));
        stackContainer1.getChildren().addAll(stackPane);

        AtomicReference<Double> replyHeight= new AtomicReference<>((double) 0);
        AtomicReference<Double> messageHeight = new AtomicReference<>((double) 0);
        Platform.runLater(()->{
            messageHeight.set(textFlow.getBoundsInLocal().getHeight());
            messageHeight.set(replyMessage.getBoundsInLocal().getHeight());
            System.out.println("Inside of the runLater: " +  textFlow.getBoundsInLocal().getHeight() + "and " + replyMessage.getBoundsInLocal().getHeight());
            stackPane.setMinHeight(textFlow.getLayoutBounds().getHeight());
            stackPane.setMaxHeight(textFlow.getLayoutBounds().getHeight());
            stackContainer1.setTranslateY((double) (0.95 * replyMessage.getBoundsInLocal().getHeight()));
            VBox.setMargin(BighBox, new Insets(0,0,textFlow.getBoundsInLocal().getHeight(),0));

        });
        System.out.println("Outside of the runLater: " +  messageHeight.get() + "and " + replyHeight.get());
        stackPane.setMinHeight(0);
        stackPane.setMaxHeight(0);
        vBox.getChildren().add(stackContainer1);
        stackContainer1.setTranslateY(20);
        BighBox.getChildren().add(vBox);
        container.getChildren().add(BighBox);
        VBox.setMargin(BighBox, new Insets(0,0,20,0));
    }

    public void sentMessage(MessageDTO message, VBox container) {
        HBox BighBox = new HBox();
        BighBox.setAlignment(Pos.CENTER_RIGHT);
        BighBox.setPadding(new Insets(5,10,5,10));

        StackPane stackPane = new StackPane();
        stackPane.setAccessibleText("message");
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
                "-fx-background-color: 	rgb(30,144,255); " +
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

        StackPane vBox = new StackPane();
        vBox.setAccessibleText("reply");
        vBox.setAlignment(Pos.TOP_RIGHT);

        StackPane stackPaneReply = new StackPane();
        stackPaneReply.setAlignment(Pos.CENTER_RIGHT);

        Button jumpToMessage = new Button();
        jumpToMessage.setText(String.valueOf(((ReplyMessageDTO)message).getIdReplyMessage()));
        jumpToMessage.setStyle("-fx-text-fill: black; -fx-background-color : black");
        jumpToMessage.prefWidthProperty().bind(stackPaneReply.widthProperty());
        jumpToMessage.prefHeightProperty().bind(stackPaneReply.heightProperty());

        String f= vBox.getAccessibleText();

        jumpToMessage.setOnMouseClicked(event -> {
            System.out.println(jumpToMessage.getText());
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
                        ToggleButton button2 = (ToggleButton) stackPane2.getChildren().get(0);
                        TextFlow textFlow1=(TextFlow) stackPane2.getChildren().get(1);
                        if (button2.getText().equals(jumpToMessage.getText())) {
                            smoothScroll(node.getBoundsInParent().getMinY()/container.getHeight());
                            addGlowAnimation(textFlow1);
                            break;
                        }
                    }
                }
            }
        });


        Text textReply= new Text(((ReplyMessageDTO)message).getMsg());
        TextFlow replyMessage = new TextFlow(textReply);
        String style1 = "-fx-color: rgb(233,233,235); " +
                "-fx-background-color: rgb(105,105,105); " +
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
        stackPane.setAlignment(Pos.TOP_RIGHT);

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setText(String.valueOf(message.getId()));
        toggleButton.setStyle("-fx-text-fill: black; -fx-background-color : transparent");
        toggleButton.setToggleGroup(toggleGroupForReplies);
        toggleButton.prefWidthProperty().bind(stackPane.widthProperty());
        toggleButton.prefHeightProperty().bind(stackPane.heightProperty());

        Text text= new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);
        String style2 = "-fx-color: rgb(239,242,255); " +
                "-fx-background-color: 	rgb(30,144,255); " +
                "-fx-background-radius: 20px;";
        textFlow.setStyle(style2);
        textFlow.setPadding(new Insets(5,10,5,10));

        text.setFill(Color.color(0.934,0.945,0.996));
        textFlow.setMouseTransparent(true);
        stackPane.getChildren().addAll(toggleButton, textFlow);
        HBox stackContainer1= new HBox();
        stackContainer1.setAlignment(Pos.TOP_RIGHT);
        stackContainer1.setPadding(new Insets(5,10,5,10));


        AtomicReference<Double> replyHeight= new AtomicReference<>((double) 0);
        AtomicReference<Double> messageHeight = new AtomicReference<>((double) 0);
        Platform.runLater(()->{
            messageHeight.set(textFlow.getBoundsInLocal().getHeight());
            messageHeight.set(replyMessage.getBoundsInLocal().getHeight());
            System.out.println("Inside of the runLater: " +  textFlow.getBoundsInLocal().getHeight() + "and " + replyMessage.getBoundsInLocal().getHeight());
            stackPane.setMinHeight(textFlow.getLayoutBounds().getHeight());
            stackPane.setMaxHeight(textFlow.getLayoutBounds().getHeight());
            stackContainer1.setTranslateY((double) (0.95 * replyMessage.getBoundsInLocal().getHeight()));
            VBox.setMargin(BighBox, new Insets(0,0,textFlow.getBoundsInLocal().getHeight(),0));

        });
        System.out.println("Outside of the runLater: " +  messageHeight.get() + "and " + replyHeight.get());
        stackPane.setMinHeight(messageHeight.get());
        stackPane.setMaxHeight(messageHeight.get());
//        double messageHeight=textFlow.getBoundsInLocal().getHeight();
        stackContainer1.getChildren().addAll(stackPane);
        stackContainer1.setTranslateY((double) (0.95 * replyHeight.get()));
        VBox.setMargin(BighBox, new Insets(0,0,messageHeight.get(),0));

        vBox.getChildren().add(stackContainer1);
        BighBox.getChildren().add(vBox);
        container.getChildren().add(BighBox);
    }

    @FXML
    void handleSendMessage(ActionEvent event) {
        if(!sendField.getText().isEmpty()) {
            if(toggleGroupForReplies.getSelectedToggle()!=null) {
                manager.sendMessage(user.getId(), manager.getUser( ((ToggleButton)toggleGroup.getSelectedToggle()).getText()).getId(), sendField.getText(), manager.getMessage(Long.valueOf(((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText())).getMessage(), manager.getMessage(Long.valueOf(((ToggleButton)toggleGroupForReplies.getSelectedToggle()).getText())).getId());
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
                if(container.getHeight()>537)
                     Platform.runLater(() -> scrollMessages.setVvalue(1.0));
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
        System.out.println(messages.getHeight());
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
