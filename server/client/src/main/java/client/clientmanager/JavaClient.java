package client.clientmanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class JavaClient {
    public static DatagramSocket ds;

    public static void main(String[] args) throws Exception {

        Vidshow vidshow = new Vidshow();
        vidshow.start();

        int serverPort = 6782;
        InetAddress address = InetAddress.getLocalHost();
        Socket socket = new Socket(address, serverPort);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeBytes("Request connect\n");

        // Create new Datagram Socket
        ds = new DatagramSocket();

        // Define DataPacket buffer
        int bufSize = 60 * 1024;
        byte[] buf = new byte[bufSize];
        buf = "givedata".getBytes();

        InetAddress addr = InetAddress.getLocalHost();

        DatagramPacket dp = new DatagramPacket(buf, buf.length, addr, 4321);

        ds.send(dp);

        DatagramPacket rcv = new DatagramPacket(buf, buf.length);

        ds.receive(rcv);
        System.out.println(new String(rcv.getData()));

        System.out.println(ds.getPort());

        // Display the Swing view
        Vidshow vd = new Vidshow();
        vd.start();

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println(inetAddress);

        Socket clientSocket = new Socket(inetAddress, 6782);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes("First connect request\n");

        // Read message asynchronously
        CThread read = new CThread(inFromServer, outToServer);

        read.join();
        clientSocket.close();
    }
}
