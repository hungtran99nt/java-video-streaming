package server.servermanager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

/**
 * Control room behaviour
 */
public class RoomController extends Thread {

    private static int roomIdCounter = 0;

    private final int roomId;
    private final ServerSocket roomSocket;
    private final int port;
    private List<Socket> roomMemberSockets;
    private List<DataOutputStream> memberOutputStreams;

    private EmbeddedMediaPlayerComponent headlessMediaPlayer;
    private String mediaMrl;
    private final String publicIp = "230.0.0.1";
    private final String options;

    /**
     * Initialize room connection socket & member connection list
     * 
     * @throws IOException
     */
    public RoomController(ServerSocket roomSocket) throws IOException {
        roomId = ++roomIdCounter;

        this.roomSocket = roomSocket;
        port = roomSocket.getLocalPort();
        roomMemberSockets = new ArrayList<Socket>();
        memberOutputStreams = new ArrayList<DataOutputStream>();

        mediaMrl = null;
        headlessMediaPlayer = new EmbeddedMediaPlayerComponent();
        headlessMediaPlayer.mediaPlayer().media().prepare(mediaMrl);
        options = formatRtspStream(publicIp, port);

        start();
    }

    @Override
    public synchronized void start() {
        super.start();
        listenForRequests();
    }

    /**
     * Listen for requests from the member in the room
     * 
     * @throws IOException
     */
    private void listenForRequests() {

        // Listen ultil room is closed
        System.out.println("Room " + roomId + " is listening on port " + port);
        while (!roomSocket.isClosed()) {
            try {
                roomMemberSockets.add(roomSocket.accept());
                int newConnId = roomMemberSockets.size() - 1;
                new Thread(new RoomRequestManager(this, roomMemberSockets.get(newConnId).getInputStream(), newConnId))
                        .start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Room " + roomId + " has been closed");
    }

    /**
     * Get room's port
     * 
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get room members' output streams
     * 
     * @return listOutputStream
     */
    public List<DataOutputStream> getMemberOutputStreams() {
        return memberOutputStreams;
    }

    /**
     * Get room id
     * 
     * @return roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Remove member from the room
     * 
     * @param roomRequestManagerId
     */
    public void removeMember(int roomRequestManagerId) {
        roomMemberSockets.remove(roomRequestManagerId);
    }

    /**
     * Start streaming
     * 
     * @param url
     */
    public void startStream() {
        headlessMediaPlayer.mediaPlayer().controls().start();
    }

    /**
     * Stop streaming
     */
    public void stopStream() {
        headlessMediaPlayer.mediaPlayer().controls().stop();
    }

    /**
     * Set Media MRL
     * 
     * @param mediaMrl
     */
    public void setMediaMrl(String mediaMrl) {
        this.mediaMrl = mediaMrl;
        headlessMediaPlayer.mediaPlayer().media().prepare(mediaMrl, options, ":no-sout-rtp-sap",
                ":no-sout-standard-sap", ":sout-all", ":sout-keep");
    }

    /**
     * Generate MRL VLC RTSP Stream Format for Video Streaming
     * 
     * @param serverAddress
     * @param serverPort
     * @return mrl
     */
    private String formatRtspStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#rtp{sdp=rtsp://@");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}");
        return sb.toString();
    }

    /**
     * Generate MRL VLC RSTP Stream Format for Webcam Streaming
     * 
     * @param serverAddress
     * @param serverPort
     * @return mrl
     */
    private String formatWebcamRtspStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(
                ":sout=#transcode{vcodec=mp4v,vb=2048,scale=1,acodec=mpga,ab=128,channels=2,samplerate=44100}:duplicate{dst=display,dst=rtp{sdp=rtsp://@");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}");
        return sb.toString();
    }
}
