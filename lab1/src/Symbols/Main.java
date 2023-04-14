package Symbols;

public class Main {
    public static void main(String[] args) {
        Runnable runSmbThr = new Runnable() {
            @Override
            public void run() {
                SymbolThread t1 = new SymbolThread("-");
                SymbolThread t2 = new SymbolThread("|");
                t1.start();
                t2.start();
            }
        };
        Thread smbThr = new Thread(runSmbThr);
        smbThr.start();
        try { smbThr.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }

        try { Thread.sleep(1000); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.print("\n\n");

        Object syncKey = new Object();
        Runnable runCtrlThr = new Runnable() {
            @Override
            public void run() {
                ControlledThread t1 = new ControlledThread("-",syncKey);
                ControlledThread t2 = new ControlledThread("|",syncKey);
                t1.start();
                t2.start();
            }
        };
        Thread ctrlThr = new Thread(runCtrlThr);
        ctrlThr.start();
        try { ctrlThr.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}