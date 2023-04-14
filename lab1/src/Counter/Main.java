package Counter;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        // inc-thread 100000 and dec-thread 100000
        Counter c1 = new Counter();
        Runnable run1 = new Runnable() {
            @Override
            public void run() {
                countingSimple(c1);
            }
        };
        Thread t1 = new Thread(run1);
        t1.start();
        try { t1.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("Simple: "+c1.get());

        // sync method
        Counter c2 = new Counter();
        Runnable run2 = new Runnable() {
            @Override
            public void run() {
                countingSyncMeth(c2);
            }
        };
        Thread t2 = new Thread(run2);
        t2.start();
        try { t2.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("Sync meth: "+c2.get());

        // sync block
        Counter c3 = new Counter();
        Runnable run3 = new Runnable() {
            @Override
            public void run() {
                countingSyncBlock(c3);
            }
        };
        Thread t3 = new Thread(run3);
        t3.start();
        try { t3.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("Block sync: "+c3.get());

        // obj lock
        Counter c4 = new Counter();
        Runnable run4 = new Runnable() {
            @Override
            public void run() {
                countingLock(c4);
            }
        };
        Thread t4 = new Thread(run4);
        t4.start();
        try { t4.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("Block sync: "+c4.get());
    }

    public static void countingSimple (Counter counter) {
        Runnable runInc = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    counter.increment();
                }
            }
        };
        Thread theadInc = new Thread(runInc);

        Runnable runDec = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    counter.decrement();
                }
            }
        };
        Thread theadDec = new Thread(runDec);

        theadInc.start();
        theadDec.start();
        try {
            theadInc.join();
            theadDec.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void countingSyncMeth (Counter counter) {
        Runnable runInc = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    counter.incrementSyncMeth();
                }
            }
        };
        Thread theadInc = new Thread(runInc);

        Runnable runDec = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    counter.decrementSyncMeth();
                }
            }
        };
        Thread theadDec = new Thread(runDec);

        theadInc.start();
        theadDec.start();
        try {
            theadInc.join();
            theadDec.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void countingSyncBlock (Counter counter) {
        Runnable runInc = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    counter.incrementSyncBlock();
                }
            }
        };
        Thread theadInc = new Thread(runInc);

        Runnable runDec = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    counter.decrementSyncBlock();
                }
            }
        };
        Thread theadDec = new Thread(runDec);

        theadInc.start();
        theadDec.start();
        try {
            theadInc.join();
            theadDec.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void countingLock (Counter counter) {
        ReentrantLock reentrantLock = new ReentrantLock();

        Runnable runInc = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    reentrantLock.lock();
                    counter.increment();
                    reentrantLock.unlock();
                }
            }
        };
        Thread theadInc = new Thread(runInc);

        Runnable runDec = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100000; i++) {
                    reentrantLock.lock();
                    counter.decrement();
                    reentrantLock.unlock();
                }
            }
        };
        Thread theadDec = new Thread(runDec);

        theadInc.start();
        theadDec.start();
        try {
            theadInc.join();
            theadDec.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}