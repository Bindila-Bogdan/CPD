package com.airbnb;

import communication.Client;
import communication.Communication;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Communication communication = new Communication();
        communication.addClient("admin");
        communication.addClient("Host1");
        communication.addClient("Guest1");
        communication.addClient("Guest2");
        communication.addClient("Host2");

        communication.communicate();
    }
}
