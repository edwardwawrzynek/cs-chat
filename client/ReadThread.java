import java.io.*;
import java.net.*;
import java.util.Arrays; 
/**
 * This thread is responsible for reading server's input and printing it
 * to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 *
 * @author www.codejava.net
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
 
    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
 
    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                if (response.indexOf("message") == 0) {
                    String tmp = response.replace("message ", "");
                    String[] parts = tmp.split(" ");
                    String name = parts[0];
                    String msg = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    System.out.println("\r[" + name + "]: " + msg);
                    System.out.flush();
                }

                if (response.indexOf("dm") == 0) {
                    String tmp = response.replace("dm ", "");
                    String[] parts = tmp.split(" ");
                    String name = parts[0];
                    String msg = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    System.out.println("\r\033[94m[DM: " + name + "]: \033[0m" + msg);
                    System.out.flush();
                }
 
                // prints the username after displaying the server's message
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                    System.out.flush();
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
