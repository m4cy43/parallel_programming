public class Matrix {
    public static double[][] randomMatrix(int size) {
        double[][] matrix = new double[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                matrix[x][y] = Math.random();
            }
        }
        return matrix;
    }

    public static double[][] staticMatrix(int size) {
        double[][] matrix = new double[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                matrix[x][y] = 1;
            }
        }
        return matrix;
    }

    public static double[][] multiply(double[][] A, double[][] B) {
        double[][] result = new double[A.length][B[0].length];
        for (int x = 0; x < A.length; x++) {
            for (int y = 0; y < A[0].length; y++) {
                for (int i = 0; i < A[0].length; i++) {
                    result[x][y] += A[x][i] * B[i][y];
                }
            }
        }
        return result;
    }

    public static double[][] getSubmatrix(double[][] matrix, int start, int end, int column) {
        double[][] result = new double[end - start + 1][column];
        int resultIndex = 0;
        for (int i = start; i < end; i++) {
            double[] temp = new double[column];
            System.arraycopy(matrix[i], 0, temp, 0, column);
            result[resultIndex] = temp;
            resultIndex++;
        }
        return result;
    }

    public static double[][] addSubmatrix(double[][] submatrix, double[][] matrix, int start, int end) {
        int rowIndex = 0;
        for (int i = start; i < end; i++) {
            System.arraycopy(submatrix[rowIndex], 0, matrix[i], 0, matrix[0].length);
            rowIndex++;
        }
        return matrix;
    }

    public static void draw(double[][] matrix) {
        for (double[] row : matrix) {
            for (double cell : row) {
                System.out.printf("%10.2f", cell);
            }
            System.out.println();
        }
    }
}
