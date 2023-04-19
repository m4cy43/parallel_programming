package task4;

import java.util.Arrays;
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

    public void transferReentrantLock(int from, int to, int amount) {
        lock.lock();
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
//        if (nTransacts % N_TEST == 0) {
//            test("ReentrantLock method");
//        }
        lock.unlock();
    }

    private void test(String methName) {
        int sum = 0;
        for (int account : accounts) {
            sum += account;
        }
        System.out.println(methName + ": Transactions:" + nTransacts + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}