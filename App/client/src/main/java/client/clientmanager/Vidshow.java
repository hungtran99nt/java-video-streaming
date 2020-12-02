package client.clientmanager;

import java.net.DatagramPacket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

class Vidshow extends Thread {

    JFrame jf = new JFrame();
    public static JPanel jp = new JPanel(new GridLayout(2, 1));
    public static JPanel half = new JPanel(new GridLayout(3, 1));
    JLabel jl = new JLabel();
    public static JTextArea messagesDisplayArea, inputMessageArea;

    // Define Data Packet buffer size
    int bufSize = 60 * 1024;
    byte[] rcvbyte = new byte[bufSize];

    DatagramPacket dp = new DatagramPacket(rcvbyte, rcvbyte.length);
    BufferedImage bf;
    ImageIcon imc;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private String publicIp, publicServer;
    private short publicPort;

    public Vidshow() throws Exception {
        // Create new media player
        publicIp = "230.0.0.1";
        publicPort = 1678;
        publicServer = formatRtpStream(publicIp, publicPort);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        // Add components to the panel
        jf.setSize(640, 960);
        jf.setTitle("Client View");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setAlwaysOnTop(true);
        jf.setLayout(new BorderLayout());
        jf.setVisible(true);
        jp.add(mediaPlayerComponent);
        // jp.add(jl);
        jp.add(half);
        jf.add(jp);

        JScrollPane jpane = new JScrollPane();
        jpane.setSize(300, 200);

        messagesDisplayArea = new JTextArea();
        messagesDisplayArea.setBackground(Color.BLACK);
        messagesDisplayArea.setDisabledTextColor(Color.WHITE);
        messagesDisplayArea.setEnabled(false);

        inputMessageArea = new JTextArea();

        jpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jpane.add(messagesDisplayArea);
        jpane.setViewportView(messagesDisplayArea);
        half.add(jpane);
        half.add(inputMessageArea);
        messagesDisplayArea.setText("Begins\n");

    }

    /**
     * Thực hiện định dạng MRL để lấy dữ liệu từ host
     * 
     * @param publicIp
     * @param publicPort
     * @return mrl
     */
    private String formatRtpStream(String publicIp, short publicPort) {
        StringBuilder sb = new StringBuilder();
        sb.append("rtp://@");
        sb.append(publicIp);
        sb.append(":");
        sb.append(publicPort);
        return sb.toString();
    }

    @Override
    public void run() {

        try {
            System.out.println("got in");
            System.out.println(publicServer);
            mediaPlayerComponent.mediaPlayer().media().play(publicServer);
            jf.revalidate();
            jf.repaint();

        } catch (Exception e) {
            System.out.println("couldn't do it");
        }
    }
}
