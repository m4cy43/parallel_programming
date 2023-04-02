import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private  final ReentrantLock lock = new ReentrantLock();
    public static final int N_TEST = 10000;
    private final int[] accounts;
    private long nTransacts = 0;
    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        Arrays.fill(accounts, initialBalance);
        nTransacts = 0;
    }

    public void transfer(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
        if (nTransacts % N_TEST == 0) {
            test();
        }
    }

    public synchronized void transferSynchronized(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
        if (nTransacts % N_TEST == 0) {
            test();
        }
    }

    public synchronized void transferWaitNotify(int from, int to, int amount) throws InterruptedException {
        while (accounts[from] < amount) {
            wait();
        }
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
        if (nTransacts % N_TEST == 0) {
            test();
        }
        notifyAll() ;
    }

    public void transferReentrantLock(int from, int to, int amount) {
        lock.lock();
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
        if (nTransacts % N_TEST == 0) {
            test();
        }
        lock.unlock();
    }

    private void test() {
        int sum = 0;
        for (int account : accounts) {
            sum += account;
        }
        System.out.println("Transactions:" + nTransacts + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}