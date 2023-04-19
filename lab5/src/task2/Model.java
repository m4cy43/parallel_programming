package task2;

import java.util.Random;
import java.util.concurrent.*;

public class Model implements Runnable
{
    private static final Random rnd = new Random();
    // thread pool
    private int queueCapacity = 10; // 100

    private int corePoolSize = 6; // AV_PROC
    private int maximumPoolSize = 6; // AV_PROC
    private long keepAliveTime = 0L;

    // task processing
    private double numTask = 1000;
    // simulation of queue movement delay
    private int QdelayOrigin = 1;
    private int QdelayBound = 6; // 10
    // simulating the delay in task processing
    private int TdelayOrigin = 1;
    private int TdelayBound = 50; // 150

    // collectable data (print)
    private double queueSize = 0;
    private double exceptedTasks = 0;
    private int id;
    private Result result;

    public Model(int id, Result result) {
        this.id = id;
        this.result = result;
    }

    @Override
    public void run()
    {
        // "обмежена черга"
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(queueCapacity);
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

            result.setResult(id, numTask, exceptedTasks, queueSize, corePoolSize, queueCapacity, QdelayBound);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class DelayedTask implements Runnable {
    private int delay;
    public DelayedTask(int delay) {
        this.delay = delay;
    }
    @Override
    public void run() {
        try
        {
            // simulating the delay in task processing
            Thread.sleep(this.delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}