import java.net.*;
import java.io.*;
import java.util.*;

public class GoBackNClient {
    static final int port = 12345;
    static int SEQ_LEN;
    static int WIN_LEN = SEQ_LEN;
    static final int TIMEOUT = 2000;
    static int n;
    static int sendBase = 0;
    static int nextSeqNum = 0;

    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;
    static String[] data;
    static boolean[] visited;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.print("\nEnter sequence length (must be a positive power of 2: 2, 4, 8, 16 or 32): ");
            SEQ_LEN = sc.nextInt();

            if(SEQ_LEN == 2 || SEQ_LEN == 4 || SEQ_LEN == 8 || SEQ_LEN == 16 || SEQ_LEN == 32) {
                break;
            }
            System.out.print("\nEnter valid sequence length.");
        }

        WIN_LEN = SEQ_LEN - 1;

        s = new Socket("localhost", port);
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

        System.out.print("\nEnter number of frames to be sent: ");
        n = sc.nextInt();

        data = new String[n];
        visited = new boolean[n];

        for(int i=0; i<n; i++) {
            data[i] = "Frame " + (i%SEQ_LEN);
        }

        Thread senderThread = new Thread(() -> {
            try {
                send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        senderThread.start();
    }

    static void send() throws Exception {
        while(sendBase < n) {
            System.out.print("\nData Frames: ");
            for(int i=0; i < n; i++) {
                if(i==sendBase) {
                    System.out.print(" [ ");
                }

                if(i==sendBase+WIN_LEN) {
                    System.out.print(" ] ");
                }

                System.out.print(" "+data[i]+" ");

            }
            System.out.println();

            for(int i=sendBase; i<Math.min(sendBase+WIN_LEN, n); i++) {
                sendPacket(i);
            }
            waitForACK();
        }
    }

    static void sendPacket(int seqNum) throws Exception {
        if(seqNum>=n) {
            return;
        }
        System.out.println("\nSending Packet: " + seqNum);
        dout.writeUTF(seqNum + ": " + data[seqNum]);
        dout.flush();
        nextSeqNum++;
    }

    static void waitForACK() throws Exception {
        s.setSoTimeout(TIMEOUT);
        while(true) {
            try {
                String message = din.readUTF();
                int ackNum = Integer.parseInt(message.trim());
                System.out.println("Received ACK " + ackNum);
                sendBase = ackNum + 1;
                break;
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout occurred. Resending packets in the window.");
                for(int i=sendBase; i<nextSeqNum; i++) {
                    sendPacket(i);
                }
            }
        }
    }
}


