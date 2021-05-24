package com.cpd.debate_0.debaters_order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TokenReceiver extends Thread {
    public String tokenState;
    private final Socket socket;
    private final BufferedReader fromTokenTransmitter;

    public TokenReceiver(Socket socket, String token) throws IOException {
        this.tokenState = token;
        this.socket = socket;
        this.fromTokenTransmitter = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (((tokenState = fromTokenTransmitter.readLine()) != null)) {
                if (tokenState.contains("stop debate")) {
                    this.closeCommunication();
                    break;
                } else if (tokenState.contains("debate")) {
                    this.handleToken(tokenState);
                } else {
                    System.out.println("Token is unknown.");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Connection error with token transmitter.");
        }
    }

    public void handleToken(String tokenState) throws InterruptedException {
        Integer tokenValue = Integer.parseInt(tokenState.split(" ")[1]);
        if(tokenValue % 3 == 0)
            System.out.println("Token with value " + tokenValue.toString() + " was received.");
        else
            this.tokenState = "wait";

        TimeUnit.SECONDS.sleep(10);

        tokenValue++;
        this.tokenState = "send " + tokenValue.toString();

        if(tokenValue % 3 == 1)
            System.out.println("Token with value " + tokenValue.toString() + " was passed.\n");

        TimeUnit.SECONDS.sleep(1);
        this.tokenState = "wait";
    }

    private void closeCommunication() throws IOException {
        this.fromTokenTransmitter.close();
        this.socket.close();
    }

    public String getTokenState() {
        return tokenState;
    }
}
