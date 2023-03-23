import java.util.ArrayList;
import java.util.List;

public class StripedAlgorithm {
    private final Matrix aMatrix;
    private final Matrix bMatrix;
    public StripedAlgorithm(Matrix a, Matrix b){
        this.aMatrix = a;
        this.bMatrix = b;
    }
    public Result multiplyMatrix(){
        Matrix resultMatrix = new Matrix(aMatrix.getSizeX(), bMatrix.getSizeY());
        List<StripedThread> threadList = new ArrayList<>();

        long timestamp0 = System.nanoTime();
        for (int rowIndex = 0; rowIndex < aMatrix.getSizeX(); rowIndex++) {
            StripedThread newThread = new StripedThread(bMatrix,
                    aMatrix.matrix[rowIndex], rowIndex, resultMatrix);
            threadList.add(newThread);
            threadList.get(rowIndex).start();
        }
        for (StripedThread thread:
             threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long timestamp1 = System.nanoTime();

        return new Result(resultMatrix, ((timestamp1-timestamp0)/1000000));
    }
}

class StripedThread extends Thread {
    private final Matrix matrix;
    private final double[] strip;
    private final int index;
    private final Matrix resultMatrix;
    public StripedThread(Matrix matrix, double[] matrixRow, int rowIndex, Matrix resultObj){
        this.matrix = matrix;
        this.strip = matrixRow;
        this.index = rowIndex;
        this.resultMatrix = resultObj;
    }
    @Override
    public void run(){
        for (int y = 0; y < matrix.getSizeY(); y++) {
            for (int x = 0; x < strip.length; x++) {
                resultMatrix.matrix[index][y] += strip[x] * matrix.matrix[x][y];
            }
        }
    }
}