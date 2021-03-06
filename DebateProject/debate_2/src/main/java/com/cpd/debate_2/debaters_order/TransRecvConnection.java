package com.cpd.debate_2.debaters_order;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TransRecvConnection extends Thread {
    private String tokenState;

    private final int ID = 2;
    private final String IP = "127.0.0.1";
    private final Integer RECEIVER_PORT = 8093;
    private final Integer TRANSMITTER_PORT = 8091;

    private final ServerSocket receiverSocket;
    private Socket transmitterSocket;

    private final TokenReceiver tokenReceiver;
    private final TokenTransmitter tokenTransmitter;

    public TransRecvConnection() throws IOException {
        receiverSocket = new ServerSocket(RECEIVER_PORT);

        tokenState = "initialize " + ID;
        transmitterSocket = new Socket(IP, TRANSMITTER_PORT);
        tokenTransmitter = new TokenTransmitter(transmitterSocket, tokenState);
        tokenTransmitter.start();

        tokenState = "wait";

        Socket socket = receiverSocket.accept();
        tokenReceiver = new TokenReceiver(socket, tokenState, ID);
        tokenReceiver.start();
    }

    @Override
    public void run() {
        while (true) {
            tokenState = tokenReceiver.getTokenState();
            tokenTransmitter.setTokenState(tokenState);

            if (tokenState.contains("stop debate"))
                return;
        }
    }

    public void setTokenState(String tokenState) {
        this.tokenState = tokenState;
    }

    public String getTokenState() {
        return tokenState;
    }
}
