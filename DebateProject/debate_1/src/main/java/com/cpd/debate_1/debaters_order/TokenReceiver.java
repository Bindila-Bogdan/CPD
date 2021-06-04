package com.cpd.debate_1.debaters_order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TokenReceiver extends Thread {
    private final int ID;
    private boolean initialNode, initialized;
    private String tokenState;
    private final Socket socket;
    private final BufferedReader fromTokenTransmitter;

    public TokenReceiver(Socket socket, String token, int ID) throws IOException {
        initialNode = true;
        initialized = false;
        tokenState = token;
        this.socket = socket;
        this.ID = ID;
        fromTokenTransmitter = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (((tokenState = fromTokenTransmitter.readLine()) != null)) {
                if (tokenState.contains("stop debate")) {
                    closeCommunication();
                    break;
                } else if (tokenState.contains("initialize")) {
                    if (!initialized)
                        initializeCommunication();
                } else if (tokenState.contains("debate")) {
                    handleToken();
                } else {
                    System.out.println("Token is unknown.");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Connection error with token transmitter.");
        }
    }

    public void initializeCommunication() throws InterruptedException {
        int receivedId = Integer.parseInt(tokenState.split(" ")[1]);

        if (receivedId < this.ID) {
            initialNode = false;
            TimeUnit.MILLISECONDS.sleep(100);
        } else if (receivedId == this.ID && initialNode) {
            initialized = true;
            System.out.println("I will send the token first");
            tokenState = "send " + this.ID;

            TimeUnit.SECONDS.sleep(1);
            tokenState = "wait";
        } else if (receivedId == this.ID) {
            initialized = true;
            TimeUnit.MILLISECONDS.sleep(100);
            tokenState = "wait";
        }
    }

    public void handleToken() throws InterruptedException {
        int tokenValue = Integer.parseInt(tokenState.split(" ")[1]);
        if (tokenValue % 3 == (ID + 2) % 3)
            System.out.println("Token with value " + tokenValue + " was received.");
        else
            tokenState = "wait";

        TimeUnit.SECONDS.sleep(10);

        tokenValue++;
        tokenState = "send " + tokenValue;

        if (tokenValue % 3 == ID % 3)
            System.out.println("Token with value " + tokenValue + " was passed.\n");

        TimeUnit.SECONDS.sleep(1);
        tokenState = "wait";
    }

    private void closeCommunication() throws IOException {
        fromTokenTransmitter.close();
        socket.close();
    }

    public String getTokenState() {
        return tokenState;
    }
}
