import java.net.*;
import java.io.*;
import java.util.*;

public class SelectiveRepeatClient {
    static final int port = 12345;
    static int SEQ_LEN;
    static int WIN_LEN;
    static final int TIMEOUT = 2000;
    static int n;
    static int nextSeqNum = 0;

    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;
    static String[] data;
    static boolean[] sent;

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

        WIN_LEN = SEQ_LEN / 2;

        s = new Socket("localhost", port);
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

        System.out.print("\nEnter number of frames to be sent: ");
        n = sc.nextInt();

        data = new String[n];
        sent = new boolean[n];

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
        while(nextSeqNum < n) {
            System.out.print("\nData Frames: ");
            for(int i=0; i < n; i++) {
                if(i==nextSeqNum) {
                    System.out.print(" ||| ");
                }
                if(i==nextSeqNum+WIN_LEN) {
                    System.out.print(" ||| ");
                }
                if(sent[i]) {
                    System.out.print(" [ " + data[i] + " ] ");
                } else {
                    System.out.print(" " + data[i] + " ");
                }
            }
            System.out.println();

            for(int i=nextSeqNum; i<Math.min(nextSeqNum+WIN_LEN, n); i++) {
                if(!sent[i]) {
                    sendPacket(i);
                }
            }
            waitForACK();
        }
    }

    static void sendPacket(int seqNum) throws Exception {
        System.out.println("\nSending Packet: " + seqNum);
        dout.writeUTF(seqNum + ": " + data[seqNum]);
        dout.flush();
    }

    static void waitForACK() throws Exception {
        s.setSoTimeout(TIMEOUT);
        while(true) {
            try {
                String message = din.readUTF();
                int ackNum = Integer.parseInt(message.trim());
                System.out.println("Received ACK " + ackNum);
                sent[ackNum] = true;
                if(ackNum == nextSeqNum) {
                    while(nextSeqNum < n && sent[nextSeqNum]) {
                        nextSeqNum++;
                    }
                }
                break;
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout occurred. Resending packets in the window.");
                break;
            }
        }
    }
}


