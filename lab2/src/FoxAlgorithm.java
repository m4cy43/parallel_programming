import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FoxAlgorithm {
    private final Matrix aMatrix;
    private final Matrix bMatrix;
    private int threadNum;
    public FoxAlgorithm(Matrix a, Matrix b, int threads){
        this.aMatrix = a;
        this.bMatrix = b;
        this.threadNum = threads;
    }

    public Result multiplyMatrix(){
        Matrix resultMatrix = new Matrix(aMatrix.getSizeX(), bMatrix.getSizeY());
        int stepSize = aMatrix.getSizeX() / Help.nearestDivisor(threadNum, aMatrix.getSizeX());
        threadNum = threadNum < aMatrix.getSizeX() ? threadNum : aMatrix.getSizeX();
        ExecutorService execPool = Executors.newFixedThreadPool(threadNum);
        long timestamp0 = System.nanoTime();
        int stepX = 0;
        int stepY = 0;
        for (int x = 0; x < threadNum; x++) {
            for (int y = 0; y < threadNum; y++) {
                stepX = stepSize * x;
                stepY = stepSize * y;
                FoxThread thread = new FoxThread(
                        getBlock(aMatrix, stepX, stepY, stepSize),
                        getBlock(bMatrix, stepY, stepX, stepSize),
                        stepX, stepY, resultMatrix);
                execPool.execute(thread);
            }
        }
        execPool.shutdown();
        try {
            execPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long timestamp1 = System.nanoTime();
        return new Result(resultMatrix, ((timestamp1-timestamp0)/1000000));
    }

    private Matrix getBlock(Matrix matrix, int indexX, int indexY, int stepSize){
        Matrix block = new Matrix(stepSize, stepSize);
        for (int index = 0; index < stepSize; index++) {
            System.arraycopy(matrix.matrix[index + indexX], indexY, block.matrix[index], 0, stepSize);
        }
        return block;
    }
}

class FoxThread extends Thread {
    private Matrix aMatrix;
    private Matrix bMatrix;
    private final int stepX;
    private final int stepY;
    private Matrix resultMatrix;

    public FoxThread(Matrix a, Matrix b, int x, int y, Matrix result) {
        this.aMatrix = a;
        this.bMatrix = b;
        this.stepX = x;
        this.stepY = y;
        this.resultMatrix = result;
    }

    @Override
    public void run(){
        for (int x = 0; x < aMatrix.getSizeX(); x++) {
            for (int y = 0; y < bMatrix.getSizeY(); y++) {
                for (int i = 0; i < bMatrix.getSizeX(); i++) {
                    resultMatrix.matrix[x + stepX][y + stepY] += aMatrix.matrix[x][i] * bMatrix.matrix[i][y];
                }
            }
        }
    }
}