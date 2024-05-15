
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.util.Scanner;

public class SelectiveRepeatServer {
    static final int PORT = 12345;
    static int SEQ_LEN;
    static DataInputStream din;
    static DataOutputStream dout;
    static int n;
    static String[] data;
    static boolean flag;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter sequence length: ");
        SEQ_LEN = sc.nextInt();
        System.out.print("\nEnter total number of frames: ");
        n = sc.nextInt();

        data = new String[n];

        for(int i=0; i<n; i++) {
            data[i] = "Frame " + (i%SEQ_LEN);
        }

        ServerSocket ss = new ServerSocket(PORT);
        Socket s = ss.accept();
        System.out.println("\nConnected\n");
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

        int expectedSeqNum = 0;

        while (expectedSeqNum < n) {
            String message = din.readUTF();
            int seqNum = Integer.parseInt(message.split(":")[0]);
            String data = message.split(":")[1];
            System.out.println("Received packet " + seqNum + ": " + data);

            if (new java.util.Random().nextInt(10) < 5) {
                System.out.println("Packet loss detected. Discarding packet " + seqNum);
            } else {
                if (seqNum == expectedSeqNum) {
                    System.out.println("Sending ACK for packet " + seqNum);
                    sendACK(seqNum);
                    expectedSeqNum++;
                } else {
                    System.out.println("Out-of-order packet received. Discarding.");
                }
            }
        }

        s.close();
        ss.close();
        din.close();
        dout.close();
    }

    static void sendACK(int seqNum) throws Exception {
        dout.writeUTF(Integer.toString(seqNum));
    }
}