import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server.");

            while (true) {
                displayMenu();
                String option = userInput.readLine();
                out.println(option);
                if (option.equals("1")) {
                    System.out.println("Server says: " + in.readLine());
                    out.println("Acknowledged");
                }
                else if (option.equals("2")) {
                    System.out.print("Enter file name to transfer: ");
                    String fileName = userInput.readLine();
                    out.println(fileName);
                    receiveFile(socket);
                    System.out.println("File received successfully.");
                    printReceivedFileContents();
                }
                else if (option.equals("3") || option.equals("4")) {
                    System.out.print("Enter expression: ");
                    String expression = userInput.readLine();
                    out.println(expression);
                    System.out.println("Result: " + in.readLine());
                }
                else {
                    System.out.println("Invalid option.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("Options:");
        System.out.println("1. Say Hello");
        System.out.println("2. File Transfer");
        System.out.println("3. Arithmetic Calculator");
        System.out.println("4. Trigonometric Calculator");
        System.out.print("Choose an option: ");
    }

    private static void receiveFile(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        FileWriter fileWriter = new FileWriter("received_file.txt");

        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals("EOF")) {
                break;
            }
            fileWriter.write(line + "\n");
        }

        fileWriter.close();
    }

    private static void printReceivedFileContents() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("received_file.txt"))) {
            System.out.println("Contents of received file:");
            String line;
            while ((line = fileReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}