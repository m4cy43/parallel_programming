package task1;

import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static final Random rnd = new Random();
    private static final int AV_PROC = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) throws InterruptedException {
        // thread pool
        int queueCapacity = 10; // 100

        int corePoolSize = 6; // AV_PROC
        int maximumPoolSize = 6; // AV_PROC
        long keepAliveTime = 0L;

        // task processing
        double numTask = 1000;
        // simulation of queue movement delay
        int QdelayOrigin = 1;
        int QdelayBound = 6; // 10
        // simulating the delay in task processing
        int TdelayOrigin = 1;
        int TdelayBound = 50; // 150

        // collectable data (print)
        double queueSize = 0;
        double exceptedTasks = 0;

        // "обмежена черга"
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(queueCapacity);
        // "пул потоків"
        try (ExecutorService executorService = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue
        )) {
            for (int i = 0; i < numTask; i++) {
                // simulation of queue movement delay
                Thread.sleep(rnd.nextInt(QdelayOrigin, QdelayBound));
                try {
                    // "окремі підзадачі"
                    DelayedTask Task = new DelayedTask(rnd.nextInt(TdelayOrigin, TdelayBound));
                    executorService.submit(Task);

                    queueSize += workQueue.size();
                } catch (Exception e) {
                    queueSize += workQueue.size();
                    exceptedTasks++;
                }
            }
            executorService.shutdown();

            System.out.printf("Tasks number: %.0f\n", numTask);
            System.out.printf("Excepted tasks: %.0f\n", exceptedTasks);
            System.out.printf("Failure prob: %.2f%%\n", exceptedTasks / numTask * 100);
            System.out.printf("Average queue size: %.2f\n", queueSize / numTask);
            System.out.printf("Pool - %d | Q capacity - %d | Delay - %d", corePoolSize, queueCapacity, QdelayBound);
        }
    }
}

class DelayedTask implements Runnable {
    public int Delay;
    public DelayedTask(int delay) {
        this.Delay = delay;
    }
    @Override
    public void run() {
        try
        {
            // simulating the delay in task processing
            Thread.sleep(this.Delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}