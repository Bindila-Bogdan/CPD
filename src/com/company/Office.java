package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Office extends Thread{
    private int noOfClerks;
    private Printer printer;
    private Random rand;
    private ArrayList<Clerk> clerks = new ArrayList<>();

    public Office (int noOfClerks){
        this.rand = new Random();
        this.noOfClerks = noOfClerks;
        this.printer = new Printer();
    }

    @Override
    public void run() {
        for(int i=0; i < this.noOfClerks; i++){
            clerks.add(new Clerk(i));
            clerks.get(i).run();
            clerks.get(i).setupPrinting(printer, rand);
        }
    }
}
