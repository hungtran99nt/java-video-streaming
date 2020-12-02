package server.servermanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class JavaServer {

    private final List<RoomController> roomList;
    public static List<BufferedReader> inFromClient;
    public static List<DataOutputStream> outToClient;
    private List<Socket> connectionSocket;
    private int serverPort = 6782;

    public JavaServer() throws Exception {

        roomList = new ArrayList<RoomController>();

        // Initialize watching member list
        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Server socket opened. Listening on port " + serverPort);
        connectionSocket = new ArrayList<Socket>();

        // Receive data from client
        inFromClient = new ArrayList<BufferedReader>();

        // Send data to client
        outToClient = new ArrayList<DataOutputStream>();

        // Create new Swing View and display it
        System.out.println("View has been displayed");

        boolean listening = true;
        while (listening) {

            // Waiting for client
            System.out.println("waiting\n ");
            Socket conn = serverSocket.accept();
            connectionSocket.add(conn);
            new Thread(new ServerRequestManager(this, conn));
            System.out.println("connected " + (connectionSocket.size() - 1));

            inFromClient.add(new BufferedReader(
                    new InputStreamReader(connectionSocket.get(connectionSocket.size() - 1).getInputStream())));
            outToClient.add(new DataOutputStream(connectionSocket.get(connectionSocket.size() - 1).getOutputStream()));
            outToClient.get(outToClient.size() - 1).writeBytes("Connected: from Server\n");

        }

        serverSocket.close();
    }

    public List<RoomController> getRoomList() {
        return roomList;
    }

    /**
     * Add new room to the room list
     * 
     * @return newRoomId
     */
    public int addNewRoom() {
        try {
            RoomController room = new RoomController(new ServerSocket());
            roomList.add(room);
            return room.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * <p>
     * Get room in the room list
     * </p>
     * return -1 if don't have searched room in the list
     * 
     * @param roomId
     * @return roomPort
     */
    public int getRoomPort(int roomId) {
        for (RoomController room : roomList) {
            if (room.getRoomId() == roomId) {
                return room.getPort();
            }
        }

        return -1;
    }

    /**
     * Remove the closed socket from the connection list
     * 
     * @param closedSocket
     * @return isRemoved
     */
    public boolean removeClosedSocket(Socket closedSocket) {
        return connectionSocket.remove(closedSocket);
    }
}
