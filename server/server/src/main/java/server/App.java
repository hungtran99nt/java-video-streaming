package server;

import server.servermanager.JavaServer;

public class App {

    public static void main(String[] args) {
        try {
            new JavaServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
