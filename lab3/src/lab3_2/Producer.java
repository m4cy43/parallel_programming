package lab3_2;

import java.util.Random;

public class Producer implements Runnable {
    private final Drop drop;
    private final int SIZE = 5000;
    public Producer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        int[] importantInfo = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            importantInfo[i] = i + 1;
        }

        Random random = new Random();

        for (int info : importantInfo) {
            drop.put(info);
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException ignored) {

            }
        }
        drop.put(0);
    }
}