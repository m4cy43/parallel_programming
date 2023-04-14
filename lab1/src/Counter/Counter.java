package Counter;

public class Counter {
    private int count = 0;
    public int get(){
        return count;
    }

    // simple
    public void increment(){
        count++;
    }
    public void decrement(){
        count--;
    }

    // sync method
    public synchronized void incrementSyncMeth(){
        count++;
    }
    public synchronized void decrementSyncMeth(){
        count--;
    }

    // block sync
    public void incrementSyncBlock(){
        synchronized (this) {
            count++;
        }
    }
    public void decrementSyncBlock(){
        synchronized (this) {
            count--;
        }
    }

    // obj lock
    //
}