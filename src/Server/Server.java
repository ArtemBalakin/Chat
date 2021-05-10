package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ExecutorService fixed = Executors.newFixedThreadPool(2);
    private static ArrayList<ServerClientThread> clientThreads = new ArrayList<>();
    private static int clientCount = 0;
    private static ServerSocket server;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(8888);
            fixed.execute(Server::addClients);
            fixed.execute(Server::readMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void start(){
        try {
            server = new ServerSocket(8888);
            fixed.execute(Server::addClients);
            fixed.execute(Server::readMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addMessages(int num, String message) {
        for (ServerClientThread client : clientThreads) {
            if (client.getClientNo() != num) client.addMessage(message);
        }
    }

    private static void readMessage() {
        Scanner input = new Scanner(System.in);
        String message;
        while (true) {
            message = input.nextLine();
            if (message != null) {
                addMessages(-1, message);
            }
            Thread.yield();
        }
    }

    private static void addClients() {
        Socket serverClient;  // сервер принимает запрос на подключение клиента
        while (true) {
            try {
                serverClient = server.accept();
                clientCount++;
                ServerClientThread sct = new ServerClientThread(serverClient, clientCount);
                clientThreads.add(sct);
                sct.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
    }
}


