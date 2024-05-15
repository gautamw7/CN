
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.util.Scanner;

public class GoBackNServer {
    static final int PORT = 12345;
    static int SEQ_LEN;
    static DataInputStream din;
    static DataOutputStream dout;
    static int n;
    static String[] data;
    static boolean[] visited;
    static int currentIndex=0;
    static boolean flag;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter sequence length: ");
        SEQ_LEN = sc.nextInt();
        System.out.print("\nEnter total number of frames: ");
        n = sc.nextInt();

        data = new String[n];
        visited = new boolean[n];

        for(int i=0; i<n; i++) {
            data[i] = "Frame " + (i%SEQ_LEN);
        }

        ServerSocket ss = new ServerSocket(PORT);
        Socket s = ss.accept();
        System.out.println("\nConnected\n");
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

        while (true) {
            String message = din.readUTF();
            int seqNum = Integer.parseInt(message.split(":")[0]);
            String data = message.split(":")[1];
            System.out.println("Received packet " + seqNum + ": " + data);

            if (new java.util.Random().nextInt(10) < 8) {
                System.out.println("Packet loss detected. Discarding packet " + seqNum);
            } else {
                while(din.available()!=0) {
                    System.out.println("\nClearing....");
                    din.readUTF();
                }
                System.out.println("Sending ACK for packet " + seqNum);
                sendACK(seqNum);
            }


            if(din.available()==0) {
                System.out.println("\nEmptied");
            }

            if (seqNum == n) {
                break;
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