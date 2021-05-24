package com.cpd.debate_1.debaters_order;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TransRecvConnection extends Thread {
    private String tokenState;

    private String IP = "127.0.0.1";
    private Integer RECEIVER_PORT = 8092;
    private Integer TRANSMITTER_PORT = 8093;

    private final ServerSocket receiverSocket;
    private final Socket transmitterSocket;

    private final TokenReceiver tokenReceiver;
    private final TokenTransmitter tokenTransmitter;

    public TransRecvConnection() throws IOException {
        tokenState = "wait";

        this.receiverSocket = new ServerSocket(RECEIVER_PORT);
        Socket socket = this.receiverSocket.accept();
        tokenReceiver = new TokenReceiver(socket, tokenState);
        tokenReceiver.start();

        this.transmitterSocket = new Socket(IP, TRANSMITTER_PORT);
        tokenTransmitter = new TokenTransmitter(transmitterSocket, tokenState);
        tokenTransmitter.start();
    }

    @Override
    public void run() {
        while (true) {
            this.tokenState = tokenReceiver.getTokenState();
            this.tokenTransmitter.setTokenState(tokenState);

            if (tokenState.contains("stop debate"))
                return;
        }
    }

    public void setToken(String token) {
        this.tokenState = token;
    }

    public String getToken() {
        return tokenState;
    }
}
