package GUI;

import Client.Client;
import Server.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;

public class LoginWindow extends Application {
    private static final GridPane grid = new GridPane();
    private static final Scene scene = new Scene(grid, 300, 170);
    private static final Button beHost = new Button("Be host");
    private static final Button joinToChat = new Button("Join to chat");
    private static final Label userName = new Label("User name:");
    private static final TextField userNameField = new TextField();
    private static final Label ipAddress = new Label("Ip address:");
    private static final TextField ipAddressField = new TextField();
    private static final ColumnConstraints columnConstraints = new ColumnConstraints();
    private static final ColumnConstraints columnConstraints1 = new ColumnConstraints();

    public LoginWindow() {
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private static boolean checkConnect() throws IOException {
        int i = 0;
        while (i < 5) {

            if (!InetAddress.getByName(ipAddressField.getText()).isReachable(1000)) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            } else return true;
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        addElement();
        setSettings();
        beHost.setOnAction(event -> {
            if (!userNameField.getText().equals("")) {
                Platform.runLater(Server::start);
                new ChatWindow(true, userNameField.getText()).start(new Stage());
                stage.close();
            }
        });
        joinToChat.setOnAction(event -> {
            try {
                if (!userNameField.getText().equals("") && !ipAddressField.getText().equals("")
                        && checkConnect()) {
                    Runnable runnable = () -> {
                        Client client = new Client(ipAddressField.getText());
                        client.start();
                        Client.sendMessagesFromGUI("Client" + userNameField.getText() + " joined");
                    };
                    Platform.runLater(runnable);
                    new ChatWindow(userNameField.getText()).start(new Stage());
                    stage.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("");
        stage.show();
    }

    private void signIn(Stage stage) throws SQLException {
    }

    private void addElement() {
        grid.getColumnConstraints().addAll(columnConstraints, columnConstraints1);
        grid.add(userName, 0, 0);
        grid.add(ipAddress, 0, 1);
        grid.add(userNameField, 1, 0);
        grid.add(ipAddressField, 1, 1);
        grid.add(beHost, 0, 3);
        grid.add(joinToChat, 1, 3);
    }

    private void setSettings() {
        columnConstraints.setPrefWidth(100);
        columnConstraints1.setPrefWidth(190);
        GridPane.setMargin(userNameField, new Insets(10, 10, 0, 0));
        GridPane.setMargin(userName, new Insets(10, 0, 0, 10));
        GridPane.setMargin(ipAddress, new Insets(10, 0, 0, 10));
        GridPane.setMargin(ipAddressField, new Insets(10, 10, 0, 0));
        GridPane.setMargin(beHost, new Insets(10, 10, 0, 20));
        GridPane.setMargin(joinToChat, new Insets(10, 20, 0, 70));
        grid.setVgap(5);
        grid.setHgap(5);
    }
}