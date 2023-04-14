package Symbols;

public class ControlledThread extends Thread {
    private String symbol = "";
    private Object syncKey;
    public ControlledThread(String symbol, Object syncKey){
        this.symbol = symbol;
        this.syncKey = syncKey;
    };
    @Override
    public void run(){
        synchronized (syncKey) {
            for (int i=0; i<100; i++){
                System.out.print(this.symbol);
                syncKey.notify();
                try { syncKey.wait(); } catch (InterruptedException e) { throw new RuntimeException(e); }
            }
        }
    }
}
