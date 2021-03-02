package lab1.prob4;

public class CommonMemory {
    private int firstIndex;
    private int secondIndex;
    private final String word;
    private boolean isReading;
    private boolean readingForward;

    public CommonMemory(String word) {
        this.word = word;
        firstIndex = -1;
        secondIndex = word.length();
        this.isReading = false;
    }

    public synchronized boolean readingRequest(Boolean forward) {
        while (isReading)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        if (forward) {
            readingForward = true;
            firstIndex++;
        } else {
            readingForward = false;
            secondIndex--;
        }

        isReading = true;
        notifyAll();

        if (forward)
            return firstIndex >= word.length() - 1;
        else
            return secondIndex <= 0;
    }

    public synchronized void read() {
        while (!isReading) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (readingForward)
            System.out.println("Forward  reader has accessed: " + word.charAt(firstIndex));
        else
            System.out.println("Backward reader has accessed: " + word.charAt(secondIndex));
        isReading = false;
        notifyAll();
    }

    public boolean hasFinished(Boolean forward) {
        if (forward)
            return firstIndex >= word.length() - 1;
        else
            return secondIndex <= 0;
    }
}
