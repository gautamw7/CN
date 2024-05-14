import java.awt.desktop.SystemSleepEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.net.*;

public class ServerMenu {
    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(1234)) {
            System.out.println("Server is listening...");

            while (true) {
                Socket client = socket.accept();
                System.out.println("Connected to client: " + client.getLocalAddress());

                handleClient(client);

                client.close();
                System.out.println("Clint disconnected");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleClient(Socket client) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter output = new PrintWriter(client.getOutputStream());

        String option;
        while ((option = input.readLine()) != null) {
            switch (option) {
                case "1":
                    System.out.println("Client selected: Say Hello");
                    sayHello(output, input);
                    break;
                case "2":
                    System.out.println("Client selected: File Transfer");
                    fileTransfer(input, output);
                    break;
                case "3":
                    System.out.println("Client selected: Arithmetic Calculator");
                    arithmeticCalculator(input, output);
                    break;
                case "4":
                    System.out.println("Client selected: Trigonometric Calculator");
                    trigonometricCalculator(input, output);
                    break;
                default:
                    System.out.println("Client selected: Invalid option");
                    output.println("Invalid option.");

            }
        }
    }

    private static void trigonometricCalculator(BufferedReader input, PrintWriter output) throws IOException{
        String exp = input.readLine();
        try{
            double result = eval(exp);
            output.println(result);
            System.out.println("Result sent: " + result);
        }catch (Exception e){
            output.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private static double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expression.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) {
                        if (x % 90 == 0) {
                            return Double.POSITIVE_INFINITY;
                        } else if (x % 270 == 0) {
                            return Double.NEGATIVE_INFINITY;
                        }
                        else {
                            x = Math.tan(Math.toRadians(x));

                        }
                    }
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());

                return x;
            }
        }.parse();

    }

    private static void arithmeticCalculator(BufferedReader input, PrintWriter output) throws IOException {
        String exp = input.readLine();
        try{
            double result = eval(exp);
            output.println(result);
            System.out.println("Result sent: " + result);
        }catch (Exception e){
            output.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private static void sayHello(PrintWriter output, BufferedReader input) throws IOException{
        output.println("Hello from server!");
        System.out.println("Server responded: Hello sent");

        System.out.println("Waiting for client's acknowledgment...");
        String acknowledgment = input.readLine();
        System.out.println("Client's acknowledgment: " + acknowledgment);
    }


    private static void fileTransfer(BufferedReader input, PrintWriter output) throws IOException {
        String fileName = input.readLine();
        Path filePath = Paths.get(fileName);

        if (Files.exists(filePath)) {
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.println(new String(buffer, 0, bytesRead));
                }
            }
            System.out.println("File sent successfully.");
            output.println("EOF");
        } else {
            System.out.println("File not found.");
            output.println("File not found.");

        }


    }
}
