package lab1.prob3;


public class Printer extends Thread {
    private final PrinterQueue printerQueue;

    public Printer(PrinterQueue printerQueue) {
        this.printerQueue = printerQueue;
    }

    @Override
    public void run() {
        while (true) {
            printerQueue.printingDocument();
        }
    }
}
