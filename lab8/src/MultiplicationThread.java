public class MultiplicationThread implements Runnable {
    private final double[][] matrixA;
    private final double[][] matrixB;
    private final double[][] result;
    private final int firstIndex;
    private final int lastIndex;
    public MultiplicationThread(double[][] A, double[][] B, double[][] result, int firstIndex, int lastIndex) {
        this.matrixA = A;
        this.matrixB = B;
        this.result = result;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
    }
    private void calculateRow(int row) {
        for (int col = 0; col < matrixB[0].length; col++) {
            for (int i = 0; i < matrixB.length; i++) {
                result[row][col] += matrixA[row][i] * matrixB[i][col];
            }
        }
    }
    @Override
    public void run() {
        for (int i = firstIndex; i < lastIndex; i++) {
            calculateRow(i);
        }
    }
}