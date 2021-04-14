package com.airbnb;

import communication.Communication;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Communication communication = new Communication();
        communication.handleRequests();
    }
}
