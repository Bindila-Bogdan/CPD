package com.airbnb;

import communication.ServerComm;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerComm serverComm = new ServerComm();
        serverComm.addClient("Admin");
        serverComm.addClient("Host1");
        serverComm.addClient("Guest1");
        serverComm.addClient("Guest2");
        serverComm.addClient("Host2");

        serverComm.communicate();
    }
}
