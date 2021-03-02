package lab1.prob3;

import java.util.Random;

public class Clerk extends Thread {
    private final Random rand;
    private final int clerkNumber;
    private final PrinterQueue printerQueue;

    public Clerk(int clerkNumber, PrinterQueue printerQueue, Random rand) {
        this.rand = rand;
        this.clerkNumber = clerkNumber;
        this.printerQueue = printerQueue;
        System.out.println("Clerk " + clerkNumber + " has been created.");
    }

    @Override
    public void run(){
        int documentsToPrint = rand.nextInt(4);
        System.out.println("Clerk " + clerkNumber + " has to print " + (documentsToPrint + 1) + " documents.");

        for (int i = 0; i < documentsToPrint + 1; i++) {
            try {
                int nothingToPrintFor = rand.nextInt(2000);
                nothingToPrintFor += 1000;

                Thread.sleep(nothingToPrintFor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printerQueue.requestDocumentPrinting(this.clerkNumber, i);
        }
    }
}
