package com.company;

import java.util.LinkedList;
import java.util.Queue;


public class Printer extends Thread{
    private Queue printingQueue = new LinkedList<String>();

    @Override
    public void run() {
        while(true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void print(int clerkNumber, double currentRequiredTime){
        try {
            wait();
            System.out.println(clerkNumber + " is waiting.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
