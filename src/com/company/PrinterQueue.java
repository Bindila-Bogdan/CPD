package com.company;

public class PrinterQueue {
    private int clerkNumber;
    private int documentNumber;
    private boolean isPrinting;

    public PrinterQueue() {
        this.isPrinting = false;
    }

    public synchronized void requestDocumentPrinting(int clerkNumber, int documentNumber) {
        while (isPrinting) {
            try {
                System.out.println("Clerk with number " + clerkNumber + " is waiting.");
                wait();
                System.out.println("Clerk with number " + clerkNumber + " was notified.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isPrinting = true;
        System.out.println("Clerk " + clerkNumber + " wants to print document " + documentNumber + ".");
        this.documentNumber = documentNumber;
        this.clerkNumber = clerkNumber;
        notifyAll();
    }

    public synchronized void printingDocument() {
        while (!isPrinting) {
            try {
                System.out.println("Printer is waiting.");
                wait();
                System.out.println("Printer was notified by clerk " + clerkNumber + ".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Printer is printing document " + this.documentNumber + " for clerk " + clerkNumber + ".");
        System.out.println("Printer notifies all clerks.");
        notifyAll();
        isPrinting = false;
    }
}
