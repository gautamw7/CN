import java.io.*;
import java.net.*;

public class Client {
    public static final int Port = 1234;
    public static final String Address = "localhost";

    public static void main(String[] args) {
        System.out.println("Client server is set up.");

        try (Socket clientSocket = new Socket(Address, Port)) {
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message = "We are client socket!!!!";
            output.println(message);

            String response = input.readLine();
            System.out.println(response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
