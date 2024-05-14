import java.io.*;
import java.net.*;

public class Server {
    public static final int Port = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Port)) {
            System.out.println("Server is set up ");

            Socket client = serverSocket.accept();
            try (PrintWriter output = new PrintWriter(client.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

                String message = "Server is set up ";
                output.println(message);

                String response = input.readLine();
                System.out.println("Client response: " + response);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
