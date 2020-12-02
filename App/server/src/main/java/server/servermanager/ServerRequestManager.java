package server.servermanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerRequestManager implements Runnable {

    private final Socket conn;
    private BufferedReader inputStream;
    private DataOutputStream outputStream;

    private final JavaServer server;

    public ServerRequestManager(JavaServer server, Socket conn) {
        this.conn = conn;
        this.server = server;

        try {
            inputStream = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            outputStream = new DataOutputStream(conn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!conn.isClosed()) {
            try {
                processRequest(inputStream.readLine());
            } catch (IOException e) {
                if (conn.isClosed()) {

                    // Remove the socket on exception because it is closed
                    server.removeClosedSocket(conn);
                }
            }
        }

    }

    /**
     * Process the open_room & join_room request
     * 
     * @param request
     * @throws NumberFormatException
     * @throws IOException
     */
    private void processRequest(String request) throws NumberFormatException, IOException {
        switch (request) {

            // Send the port of the room to the client
            // If the room does not exist, send -1
            case "join_room":
                int roomId = Integer.parseInt(inputStream.readLine());
                int roomPort = server.getRoomPort(roomId);
                outputStream.writeBytes(roomPort + "\n");
                break;

            // Create new room and send room id to the client so the client can make
            // join_room request
            case "open_room":
            default:
                int newRoomId = server.addNewRoom();
                outputStream.writeBytes(newRoomId + "\n");
                break;
        }
    }

}
