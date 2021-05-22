package com.cpd.debate_0.debaters_order;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TransRecvConnection extends Thread {
    private String token;

    private String IP = "127.0.0.1";
    private Integer PORT_FOR_TRANSMITTER = 8091;
    private Integer PORT_FOR_RECEIVER = 8092;

    private final ServerSocket socketForTransmitter;
    private final Socket socketForReceiver;

    private final TokenReceiver tokenReceiver;
    private final TokenTransmitter tokenTransmitter;

    public TransRecvConnection() throws IOException {
        token = "wait";

        this.socketForTransmitter = new ServerSocket(PORT_FOR_TRANSMITTER);
        Socket socket = this.socketForTransmitter.accept();
        tokenReceiver = new TokenReceiver(socket, token);
        tokenReceiver.start();

        this.socketForReceiver = new Socket(IP, PORT_FOR_RECEIVER);
        tokenTransmitter = new TokenTransmitter(socketForReceiver, token);
        tokenTransmitter.start();
    }

    @Override
    public void run() {
        while (true) {
            this.token = tokenReceiver.getToken();
            this.tokenTransmitter.setToken(token);

            if (token.contains("stop debate"))
                return;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getIP() {
        return IP;
    }
}
