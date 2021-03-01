package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Office extends Thread {
    private final Random rand;
    private final int noOfClerks;
    private final Printer printer;
    private final PrinterQueue printerQueue;
    private final ArrayList<Clerk> clerks = new ArrayList<>();

    public Office(int noOfClerks) {
        this.rand = new Random();
        this.noOfClerks = noOfClerks;
        this.printerQueue = new PrinterQueue();

        printer = new Printer(printerQueue);
        printer.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < this.noOfClerks; i++) {
            clerks.add(new Clerk(i, printerQueue, rand));
            clerks.get(i).start();
        }
    }
}
