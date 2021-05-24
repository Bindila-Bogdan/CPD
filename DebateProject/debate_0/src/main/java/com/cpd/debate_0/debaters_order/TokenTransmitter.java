package com.cpd.debate_0.debaters_order;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TokenTransmitter extends Thread {
    public String tokenState;
    private final Socket socket;
    private final PrintWriter forTokenReceiver;

    public TokenTransmitter(Socket socket, String token) throws IOException {
        this.socket = socket;
        this.tokenState = token;
        this.forTokenReceiver = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (tokenState.contains("send")){
                    this.forTokenReceiver.println("debate " + tokenState.split(" ")[1]);
                    this.tokenState = "wait";
                    TimeUnit.SECONDS.sleep(2);
                }
                else if (tokenState.contains("stop debate")) {
                    this.closeClientCommunication();
                    this.forTokenReceiver.println(tokenState);
                    return;
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Connection error with token transmitter.");
        }
    }

    void closeClientCommunication() throws IOException {
        this.forTokenReceiver.close();
        this.socket.close();
    }

    public void setTokenState(String tokenState) {
        this.tokenState = tokenState;
    }
}
