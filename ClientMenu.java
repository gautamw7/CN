import java.io.*;
import java.net.*;
import java.util.*;

public class ClientMenu {
    private static final String host = "localhost";
    private static final int port = 1234;

    public static void main(String[] args) {
        try (Socket client = new Socket(host, port);
             PrintWriter output = new PrintWriter(client.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server.");

            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                output.println(choice);
                output.flush(); // Flush the output stream

                switch (choice) {
                    case 1:
                        System.out.println("Server says: " + input.readLine());
                        output.println("Acknowledged");
                        break;
                    case 2:
                        System.out.print("Enter file name to transfer: ");
                        String fileName = scanner.nextLine();
                        output.println(fileName);
                        output.flush(); // Flush the output stream
                        receiveFile(input);
                        System.out.println("File received successfully");
                        output.println("Acknowledged");
                        break;
                    case 3:
                    case 4:
                        System.out.print("Enter the expression: ");
                        String expression = scanner.nextLine();
                        output.println(expression);
                        output.flush(); // Flush the output stream
                        System.out.println("Result: " + input.readLine());
                        break;
                    default:
                        System.out.println("Wrong option");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("Options:\n" +
                "1. Say Hello\n" +
                "2. File Transfer\n" +
                "3. Arithmetic Calculator\n" +
                "4. Trigonometric Calculator\n" +
                "Choose an option:");
    }

    private static void receiveFile(BufferedReader input) {
        try (FileWriter fileWriter = new FileWriter("received_file.txt")) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.equals("EOF")) {
                    break;
                }
                fileWriter.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error receiving file: " + e.getMessage());
        }
    }
}
