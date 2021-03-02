package lab1.prob4;

import java.util.Random;

public class ForwardReader extends Thread {
    private boolean finishedReading;
    private CommonMemory commonMemory;
    private Random rand;

    public ForwardReader(CommonMemory commonMemory) {
        this.commonMemory = commonMemory;
        finishedReading = false;
        this.rand = new Random();
    }

    @Override
    public void run() {
        System.out.println("Forward reader has been created.");
        while (!finishedReading) {
            try {
                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishedReading = commonMemory.readingRequest(true);
        }
    }
}
