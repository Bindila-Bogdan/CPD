package lab1.prob4;

public class Reader extends Thread {
    private CommonMemory commonMemory;

    public Reader(CommonMemory commonMemory) {
        this.commonMemory = commonMemory;
    }

    @Override
    public void run() {
        while (!commonMemory.hasFinished(true) || !commonMemory.hasFinished(false)) {
            commonMemory.read();
        }
    }
}
