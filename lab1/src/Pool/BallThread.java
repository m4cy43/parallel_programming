public class BallThread extends Thread {
    private Ball b;
    private int maxIterations = 10000;
    private int speed = 5;

    public BallThread(Ball ball){
        b = ball;
    }

    public int getIterations (){
        return maxIterations;
    }

    public void setIterations (int num){
        this.maxIterations = num;
    }

    public int getSpeed (){
        return speed;
    }

    public void setSpeed (int num){
        this.speed = num;
    }

    @Override
    public void run(){
        try{
            for(int i=1; i<maxIterations; i++){
                b.move();

                // break (exit) from the thread
                // https://stackoverflow.com/questions/26509075/java-break-or-exit-from-a-thread
                if (b.isPocked) {
                    return;
                }

                System.out.println("Thread name = "
                        + Thread.currentThread().getName());
                Thread.sleep(speed);
            }
        } catch(InterruptedException ex){

        }
    }
}