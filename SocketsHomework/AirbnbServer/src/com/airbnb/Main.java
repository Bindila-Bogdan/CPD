package com.airbnb;

import communication.ClientComm;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ClientComm clientComm = new ClientComm();
        clientComm.handleRequests();
    }
}
