public class Multiplication {
    private final double[][] matrixA;
    private final double[][] matrixB;
    private final int threadsNum;
    public Multiplication(double[][] A, double[][] B, int threads) {
        this.matrixA = A;
        this.matrixB = B;
        this.threadsNum = threads;
    }
    public double[][] multiply() {
        int rows = matrixA.length;
        int cols = matrixB[0].length;
        double[][] result = new double[rows][cols];
        Thread[] threads = new Thread[threadsNum];
        int threadRows = rows / threadsNum;
        int firstRow = 0;
        for (int i = 0; i < threadsNum; i++) {
            int lastRow = firstRow + threadRows;
            if (i == threadsNum - 1) {
                lastRow += rows - threadRows;
            }
            threads[i] = new Thread(
                new MultiplicationThread(matrixA, matrixB, result, firstRow, lastRow)
            );
            threads[i].start();
            firstRow = lastRow;
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ignored) {

        }
        return result;
    }
}