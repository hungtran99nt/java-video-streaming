package client.clientmanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Process chat messages from client
 */
class CThread extends Thread {

    private BufferedReader inFromServer;
    private Button sender = new Button("Send Text");
    private DataOutputStream outToServer;

    public CThread(BufferedReader in, DataOutputStream out) {
        inFromServer = in;
        outToServer = out;

        // Add the send button to the Swing view, so each client will have their own
        // button
        Vidshow.half.add(sender);
        sender.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sentence = Vidshow.inputMessageArea.getText();
                Vidshow.messagesDisplayArea.append("From myself: " + sentence + "\n");
                try {
                    outToServer.writeBytes(sentence + '\n');
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Vidshow.inputMessageArea.setText(null);
            }
        });

        // Start Thread automatically on create
        start();
    }

    /**
     * Chat Processing
     */
    public void run() {

        try {
            while (true) {
                readMessage();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Read new message sent from server
     * 
     * @throws IOException
     */
    private void readMessage() throws IOException {
        String rcvMessage = inFromServer.readLine();

        Vidshow.messagesDisplayArea.append(rcvMessage + "\n");
        Vidshow.messagesDisplayArea.setCaretPosition(Vidshow.messagesDisplayArea.getDocument().getLength());
        Vidshow.half.revalidate();
        Vidshow.half.repaint();
        Vidshow.jp.revalidate();
        Vidshow.jp.repaint();
    }
}
