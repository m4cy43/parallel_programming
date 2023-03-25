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
        // if threadNum is greater than matrix size (for small matrices)
        threadNum = threadNum < aMatrix.getSizeX() ? threadNum : aMatrix.getSizeX();
        // to divide into equal numbers
        threadNum = Help.nearestDivisor(threadNum, aMatrix.getSizeX());
        int stepSize = aMatrix.getSizeX() / threadNum;

        // we don't need "future-list", if we can execute threads immediately
        ExecutorService execPool = Executors.newFixedThreadPool(threadNum);
        long timestamp0 = System.nanoTime();

        // Maps of sizes for matrices A and B
        int[][] sizesX = new int[threadNum][threadNum];
        int[][] sizesY = new int[threadNum][threadNum];
        for (int x = 0; x < threadNum; x++) {
            for (int y = 0; y < threadNum; y++) {
                sizesX[x][y] = stepSize * x;
                sizesY[x][y] = stepSize * y;
            }
        }

        // Shift the submatrices of A and B cyclically to the right and bottom,
        // respectively, so that each processor receives
        // a new submatrix of A and B for the next iteration.
        for (int move = 0; move < threadNum; move++) {
            for (int x = 0; x < threadNum; x++) {
                for (int y = 0; y < threadNum; y++) {
                    // (x + move) % threadNum ---> shift
                    // x + move ---> for sync shift in both submatrices (cyclically and in appropriate dir)
                    // x + move < threadNum ---> (x + move)
                    // x + move > threadNum ---> threadNum - (x + move)
                    int aShiftIndexX = sizesX[x][(x + move) % threadNum];
                    int aShiftIndexY = sizesY[x][(x + move) % threadNum];

                    int bShiftIndexX = sizesX[(x + move) % threadNum][y];
                    int bShiftIndexY = sizesY[(x + move) % threadNum][y];
                    FoxThread thread = new FoxThread(
                        getBlock(aMatrix, aShiftIndexX, aShiftIndexY, stepSize),
                        getBlock(bMatrix, bShiftIndexX, bShiftIndexY, stepSize),
                        sizesX[x][y], sizesY[x][y], resultMatrix
                    );
                    // immediately execute
                    execPool.execute(thread);
                }
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
                    // multiply in submatrices and write to result matrix
                    resultMatrix.matrix[x + stepX][y + stepY] += aMatrix.matrix[x][i] * bMatrix.matrix[i][y];
                }
            }
        }
    }
}