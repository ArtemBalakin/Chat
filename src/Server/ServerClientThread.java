package Server;

import GUI.ChatWindow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerClientThread extends Thread {
    private ExecutorService fixed = Executors.newFixedThreadPool(2);
    private Socket serverClient;
    private int clientNo;
    private ArrayList<String> messages = new ArrayList<>();
    private DataOutputStream outStream;
    private DataInputStream inStream;

    public ServerClientThread(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;
        try {
            outStream = new DataOutputStream(serverClient.getOutputStream());
            inStream = new DataInputStream(serverClient.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        fixed.execute(this::receiveMessage);
        fixed.execute(this::sendMessages);
    }


    public void addMessage(String message) {
        messages.add(0, message);
    }

    public int getClientNo() {
        return clientNo;
    }

    private void sendMessages() {
        while (true) {
            if (messages.size() != 0) {
                try {
                    for (String s : messages) {
                        outStream.writeUTF(s);
                    }
                    messages.clear();
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Thread.yield();
        }
    }

    private void receiveMessage() {
        String clientMessage;
        while (true) {
            try {
                clientMessage = inStream.readUTF();
                ChatWindow.addMessage(clientMessage);
                Server.addMessages(clientNo, clientMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
    }
}
