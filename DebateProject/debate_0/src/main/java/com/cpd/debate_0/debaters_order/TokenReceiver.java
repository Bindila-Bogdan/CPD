package com.cpd.debate_0.debaters_order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TokenReceiver extends Thread {
    public String token;
    private final Socket socket;
    private final BufferedReader fromTokenTransmitter;

    public TokenReceiver(Socket socket, String token) throws IOException {
        this.token = token;
        this.socket = socket;
        this.fromTokenTransmitter = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (((token = fromTokenTransmitter.readLine()) != null)) {
                if (token.contains("stop debate")) {
                    System.out.println(token);
                    this.closeCommunication();
                    break;
                } else if (token.contains("debate")) {
                    this.handleToken();
                } else {
                    System.out.println("Token is unknown.");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Connection error with token transmitter.");
        }
    }

    public void handleToken() throws InterruptedException {
        System.out.println("Token was received.");

        this.token = "debate";
        TimeUnit.SECONDS.sleep(10);
        this.token = "send";

        System.out.println("Token was passed.");
        System.out.println("------------------------------");
    }

    private void closeCommunication() throws IOException {
        this.fromTokenTransmitter.close();
        this.socket.close();
    }

    public String getToken() {
        return token;
    }
}
