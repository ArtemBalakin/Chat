package Client;

import GUI.ChatWindow;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static DataInputStream inStream;
    private static DataOutputStream outStream;
    private Socket socket;
    private ExecutorService fixed = Executors.newFixedThreadPool(2);
    private BufferedReader br;

    public Client(String address) {
        try {
            socket = new Socket(address, 8888);
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessagesFromGUI(String clientMessage) {
        try {
            outStream.writeUTF(clientMessage);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages() {
        String clientMessage;
        while (true) {
            try {
                clientMessage = br.readLine();
                outStream.writeUTF(clientMessage);
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        fixed.execute(this::receiveMessage);
        fixed.execute(this::sendMessages);
    }

    private void receiveMessage() {
        while (true) {
            try {
                ChatWindow.addMessage(inStream.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

