import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private int port;
    private Set<UserThread> users = new HashSet<>();
    private ArrayList<Message> messages = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                users.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread user : users) {
            if (user != excludeUser) {
                user.sendMessage(message);
            }
        }
    }

    // send all past messages to a user
    void sendHistory(UserThread user) {
        for (Message msg : messages) {
            user.sendMessage(msg.toString());
        }
    }

    void addHistory(Message msg) {
        messages.add(msg);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(UserThread user) {
        users.remove(user);
    }

    Set<UserThread> getUsers() {
        return users;
    }
}