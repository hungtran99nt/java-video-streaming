package server.servermanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RoomRequestManager implements Runnable {

    private final RoomController roomController;
    private final int roomRequestManagerId;
    private final BufferedReader inputStream;

    public RoomRequestManager(RoomController roomController, InputStream inputStream, int roomRequestManagerId) {
        this.roomController = roomController;
        this.roomRequestManagerId = roomRequestManagerId;
        this.inputStream = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void run() {
        while (true) {
            try {
                processRequest(inputStream.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send message to all room members
     * 
     * @param message
     */
    private void sendMessage(String message) {
        roomController.getMemberOutputStreams().forEach(outputStream -> {
            try {
                outputStream.writeBytes(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Process the requests of the members in the room
     * 
     * @param request
     * @throws IOException
     */
    private void processRequest(String request) throws IOException {
        switch (request) {
            case "start_stream":
                roomController.startStream();
                break;

            case "stop_stream":
                roomController.stopStream();
                break;

            case "open_video":
                int videoId = Integer.parseInt(inputStream.readLine());
                roomController.setMediaMrl(Integer.toString(videoId));
                break;

            case "upload_video":
                String mediaSource = inputStream.readLine();
                roomController.setMediaMrl(mediaSource);
                break;

            case "send_message":
                sendMessage(inputStream.readLine());
                break;

            case "kick_member":
                int memberId = Integer.parseInt(inputStream.readLine());
                roomController.removeMember(memberId);
                sendMessage("Room member number " + memberId + " has been kicked out of the room");
                break;

            case "leave_room":
                roomController.removeMember(roomRequestManagerId);
                sendMessage("Room member number " + roomRequestManagerId + " has left the room");
                break;

            case "join_room":
            default:
                sendMessage("Room member number " + roomRequestManagerId + " has joined the room");
                break;
        }
    }

}
