public class Matrix {
    public double[][] matrix;
    private int sizeX;
    public int getSizeX(){
        return this.sizeX;
    }
    private int sizeY;
    public int getSizeY(){
        return this.sizeY;
    }
    public Matrix(int X,int Y){
        this.sizeX = X;
        this.sizeY = Y;
        this.matrix = new double[X][Y];
    }
    public void randomMatrixFilling() {
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                this.matrix[x][y] = Math.random();
            }
        }
    }

    public void printMatrix() {
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                System.out.printf("%7.2f", this.matrix[x][y]);
            }
            System.out.println();
        }
    }
}
