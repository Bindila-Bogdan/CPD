package com.company;

import java.util.Random;

public class Clerk extends Thread{
    private int clerkNumber;
    private double currentRequiredTime;

    public Clerk(int clerkNumber){
        System.out.println("Clerk " + Integer.toString(clerkNumber) + " has been created.");
        this.clerkNumber = clerkNumber;
    }

    public void setupPrinting(Printer printer, Random rand){
        while(true){
            currentRequiredTime = rand.nextDouble() * 10;
            printer.print(clerkNumber, currentRequiredTime);
        }
    }
}
