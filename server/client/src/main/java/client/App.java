package client;

import client.clientmanager.JavaClient;

public class App {

    public static void main(String[] args) {
        try {
            JavaClient.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
