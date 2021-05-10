package GUI;

import Client.Client;
import Server.Server;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ChatWindow extends Application {
    private static final AnchorPane anchorPane = new AnchorPane();
    private static final Scene scene = new Scene(anchorPane, 700, 700);
    private static final Button sendMessage = new Button("Send");
    private static final Label ipAdress = new Label("Ip:");
    private static final TextField messageArea = new TextField();
    private static final Label portNumber = new Label("Port number:");
    private static final TextArea textArea = new TextArea("Chat started " + new Date().toString());
    private static String login;
    private static InetAddress addr;
    private static boolean isServer = false;

    public ChatWindow(String login) {
        try {
            addr = InetAddress.getLocalHost();
            ChatWindow.login = login;
            textArea.setText(textArea.getText() + "\nClient " + login + " joined");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public ChatWindow(boolean isServer, String login) {
        ChatWindow.isServer = isServer;
        try {
            addr = InetAddress.getLocalHost();
            System.out.println(addr.getHostAddress());
            ChatWindow.login = login;
            textArea.setText(textArea.getText() + "\nClient " + login + " joined");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void addMessage(String message) {
        textArea.setText(textArea.getText() + "\n" + message);
    }

    @Override
    public void start(Stage primaryStage) {
        makeSettings(primaryStage);
        addEllements(primaryStage);
    }

    private void addEllements(Stage stage) {
        if (isServer) anchorPane.getChildren().addAll(sendMessage, ipAdress, messageArea, portNumber, textArea);
        else {
            anchorPane.getChildren().addAll(sendMessage, messageArea, textArea);
        }
        stage.setScene(scene);
        stage.show();
    }

    private void makeSettings(Stage stage) {
        anchorPane.setStyle("-fx-background-color: yellow");
        stage.setResizable(false);
        AnchorPane.setTopAnchor(textArea, 30d);
        if (!isServer) {
            textArea.setMaxSize(400, 600);
            textArea.setMinSize(400, 600);
            AnchorPane.setLeftAnchor(textArea, 30d);
            AnchorPane.setRightAnchor(textArea, 35d);
        } else {
            ipAdress.setText(ipAdress.getText() + " " + addr.getHostAddress());
            portNumber.setText(portNumber.getText() + " " + 8888);
            textArea.setMaxSize(400, 600);
            textArea.setMinSize(400, 600);
            AnchorPane.setLeftAnchor(textArea, 270d);
        }

        AnchorPane.setLeftAnchor(messageArea, 300d);
        AnchorPane.setTopAnchor(messageArea, 640d);
        AnchorPane.setRightAnchor(messageArea, 100d);
        AnchorPane.setTopAnchor(sendMessage, 640d);
        AnchorPane.setRightAnchor(sendMessage, 35d);
        AnchorPane.setTopAnchor(ipAdress, 50d);
        AnchorPane.setTopAnchor(portNumber, 80d);
        AnchorPane.setLeftAnchor(ipAdress, 20d);
        AnchorPane.setLeftAnchor(portNumber, 20d);
        sendMessage.setOnAction(event -> {
            if (messageArea.getText() != null && !messageArea.getText().equals("")) {
                if (isServer) {
                    Server.addMessages(-1, login + ":" + messageArea.getText());
                } else {
                    Client.sendMessagesFromGUI(login + ":" + messageArea.getText());
                }
                textArea.setText(textArea.getText() + "\n" + "Me: " + messageArea.getText());
                messageArea.clear();
            }
        });
    }

}
