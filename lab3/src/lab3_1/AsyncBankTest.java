package lab3_1;

public class AsyncBankTest {
    public static final int N_ACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;
    public static void main(String[] args) {
        Bank b = new Bank(N_ACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < N_ACCOUNTS; i++) {
            TransferThread t = new TransferThread(b, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }
}