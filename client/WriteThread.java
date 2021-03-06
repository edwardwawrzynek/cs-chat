 import java.io.*;
 import java.net.*;
 import java.util.Arrays;
 
/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 *
 * @author www.codejava.net
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
 
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
 
        Console console = System.console();
 
        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println("name " + userName);
 
        String text;
 
        do {
            text = console.readLine("[" + userName + "]: ");
            if (text.indexOf("/DM ") == 0) {
                String tmp = text.replace("/DM ", "");
                String[] parts = tmp.split(" ");
                String msg = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                writer.println("dm " + parts[0] +" " + msg);
            } else {
                writer.println("message " + text);
            }
 
        } while (!text.equals("/EXIT"));
 
        try {
            writer.println("exit");
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}