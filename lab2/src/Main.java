public class Main {
    private static final int X_SIZE = 100;
    private static final int Y_SIZE = 100;
    private static final int MAX_RANDOM_VALUE = 1;
    private static final String STRING_FORMAT = "%7.2f";
    public static void main(String[] args) {
        Matrix m1 = new Matrix(X_SIZE,Y_SIZE);
        m1.randomMatrixFilling(MAX_RANDOM_VALUE);
        Matrix m2 = new Matrix(X_SIZE,Y_SIZE);
        m2.randomMatrixFilling(MAX_RANDOM_VALUE);

        StripedAlgorithm stripAlg = new StripedAlgorithm(m1,m2);
        Result resStripAlg = stripAlg.multiplyMatrix();
        resStripAlg.printfMatrix(STRING_FORMAT);
        resStripAlg.printfTime("Striped");

        StripedAlgorithm FoxAlg = new StripedAlgorithm(m1,m2);
        Result resFoxAlg = FoxAlg.multiplyMatrix();
        resFoxAlg.printfMatrix(STRING_FORMAT);
        resFoxAlg.printfTime("Fox");
    }
}