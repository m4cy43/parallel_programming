public class Main {
    private static final int X_SIZE = 1000;
    private static final int Y_SIZE = 1000;
    private static final int MAX_RANDOM_VALUE = 1;
    private static final String STRING_FORMAT = "%7.2f";
    public static void main(String[] args) {
        Matrix m1 = new Matrix(X_SIZE,Y_SIZE);
        m1.randomMatrixFilling(MAX_RANDOM_VALUE);
        Matrix m2 = new Matrix(X_SIZE,Y_SIZE);
        m2.randomMatrixFilling(MAX_RANDOM_VALUE);

        StripedAlgorithm stripAlg = new StripedAlgorithm(m1,m2);
        Result resStripAlg = stripAlg.multiplyMatrix();
//        resStripAlg.printfMatrix(STRING_FORMAT);
        resStripAlg.printfTime("Striped");

        int threadNum = Runtime.getRuntime().availableProcessors();
        FoxAlgorithm FoxAlg = new FoxAlgorithm(m1,m2,threadNum);
        Result resFoxAlg = FoxAlg.multiplyMatrix();
//        resFoxAlg.printfMatrix(STRING_FORMAT);
        resFoxAlg.printfTime("Fox");

        Normal normalAlg = new Normal(m1, m2);
        Result resNormalAlg = normalAlg.multiplyMatrix();
//        resNormalAlg.printfMatrix(STRING_FORMAT);
        resNormalAlg.printfTime("Normal");
    }
}