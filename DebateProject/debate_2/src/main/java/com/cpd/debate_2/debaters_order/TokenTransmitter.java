package com.cpd.debate_2.debaters_order;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TokenTransmitter extends Thread {
    public String token;
    private final Socket socket;
    private final PrintWriter forTokenReceiver;

    public TokenTransmitter(Socket socket, String token) throws IOException {
        this.socket = socket;
        this.token = token;
        this.forTokenReceiver = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (token.contains("send")) {
                    this.forTokenReceiver.println("debate after 2");
                    this.token = "wait";
                }
                else if (token.contains("stop debate")) {
                    this.closeClientCommunication();
                    this.forTokenReceiver.println(token);
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Connection error with token transmitter.");
        }
    }

    void closeClientCommunication() throws IOException {
        this.forTokenReceiver.close();
        this.socket.close();
    }

    public void setToken(String token) {
        this.token = token;
    }
}
