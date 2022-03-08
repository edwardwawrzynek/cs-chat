import java.io.*;
import java.net.*;
import java.util.*;

public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private String name;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        this.name = "[annonymous]";
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            // send all previous messages

            boolean sentHistory = false;

            String protocalString;

            do {
                protocalString = reader.readLine();
                if (protocalString.startsWith("name")) {
                    int i = protocalString.indexOf(" ");
                    name = protocalString.substring(i + 1).replace(" ", "_");

                } else if (protocalString.startsWith("message")) {
                    int i = protocalString.indexOf(" ");
                    Message message = new Message(name, protocalString.substring(i + 1));
                    server.broadcast(message.toString(), this);
                    server.addHistory(message);
                } else if (protocalString.startsWith("dm")) {
                    protocalString = protocalString.replace("dm ", "");
                    int i = protocalString.indexOf(" ");
                    String name = protocalString.substring(0, i);
                    String message = protocalString.substring(i+1);

                    
                }

                if(!sentHistory) {
                    server.sendHistory(this);
                    sentHistory = true;
                }
            } while (!protocalString.equals("exit"));

            server.removeUser(this);
            socket.close();

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.print(message);
        writer.flush();
    }
}