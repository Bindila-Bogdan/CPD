package lab1.prob4;


public class Main {
    public static void main(String[] args) {
        CommonMemory commonMemory = new CommonMemory("abcdefg");

        Reader reader = new Reader(commonMemory);
        reader.start();

        ForwardReader forwardReader = new ForwardReader(commonMemory);
        forwardReader.start();

        BackwardReader backwardReader = new BackwardReader(commonMemory);
        backwardReader.start();
    }
}
