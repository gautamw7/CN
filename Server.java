import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to client: " + clientSocket.getInetAddress());

                handleClient(clientSocket);

                clientSocket.close();
                System.out.println("Client disconnected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String option;
        while ((option = in.readLine()) != null) {
            switch (option) {
                case "1":
                    System.out.println("Client selected: Say Hello");
                    sayHello(out, in);
                    break;
                case "2":
                    System.out.println("Client selected: File Transfer");
                    fileTransfer(in, out);
                    break;
                case "3":
                    System.out.println("Client selected: Arithmetic Calculator");
                    arithmeticCalculator(in, out);
                    break;
                case "4":
                    System.out.println("Client selected: Trigonometric Calculator");
                    trigonometricCalculator(in, out);
                    break;
                default:
                    System.out.println("Client selected: Invalid option");
                    out.println("Invalid option.");
                    break;
            }
        }

        in.close();
        out.close();
    }

    private static void sayHello(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Hello from server!");
        System.out.println("Server responded: Hello sent");

        System.out.println("Waiting for client's acknowledgment...");
        String acknowledgment = in.readLine();
        System.out.println("Client's acknowledgment: " + acknowledgment);
    }

    private static void fileTransfer(BufferedReader in, PrintWriter out) throws IOException {
        String fileName = in.readLine();
        Path filePath = Paths.get(fileName);

        if (Files.exists(filePath)) {
            try (InputStream fileStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    out.println(new String(buffer, 0, bytesRead));
                }
            }
            System.out.println("File sent successfully.");
            out.println("EOF");
        } else {
            System.out.println("File not found.");
            out.println("File not found.");
        }
    }

    private static void arithmeticCalculator(BufferedReader in, PrintWriter out) throws IOException {
        String expression = in.readLine();
        try {
            double result = eval(expression);
            out.println(result);
            System.out.println("Result sent: " + result);
        } catch (Exception e) {
            out.println(e.getMessage());
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void trigonometricCalculator(BufferedReader in, PrintWriter out) throws IOException {
        String expression = in.readLine();
        try {
            double result = eval(expression);
            out.println(result);
            System.out.println("Result sent: " + result);
        } catch (Exception e) {
            out.println(e.getMessage());
            System.out.println("Error occurred: " + e.getMessage());
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
}