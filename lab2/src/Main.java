public class Main {
    private static final int X_SIZE = 1000;
    private static final int Y_SIZE = 1000;
    private static final int MAX_RANDOM_VALUE = 1;
    private static final String STRING_FORMAT = "%7.2f";
    public static void main(String[] args) {
        testAllAlgorithms();
        //differentSizes();

    }

    static void testAllAlgorithms (){
        Matrix m1 = new Matrix(X_SIZE,Y_SIZE);
        m1.randomMatrixFilling(MAX_RANDOM_VALUE);
        Matrix m2 = new Matrix(X_SIZE,Y_SIZE);
        m2.randomMatrixFilling(MAX_RANDOM_VALUE);
        int threadNum = Runtime.getRuntime().availableProcessors();

        StripedAlgorithm stripAlg = new StripedAlgorithm(m1,m2);
        Result resStripAlg = stripAlg.multiplyMatrix();
//        resStripAlg.printfMatrix(STRING_FORMAT);
        resStripAlg.printfTime("Striped");

        StripedAlgorithm stripAlgThread = new StripedAlgorithm(m1,m2,threadNum);
        Result resStripAlgThread = stripAlgThread.multiplyMatrixFixedThreads();
//        resStripAlgThread.printfMatrix(STRING_FORMAT);
        resStripAlgThread.printfTime("Striped thread");

        FoxAlgorithm FoxAlg = new FoxAlgorithm(m1,m2,threadNum);
        Result resFoxAlg = FoxAlg.multiplyMatrix();
//        resFoxAlg.printfMatrix(STRING_FORMAT);
        resFoxAlg.printfTime("Fox");

        Normal normalAlg = new Normal(m1, m2);
        Result resNormalAlg = normalAlg.multiplyMatrix();
//        resNormalAlg.printfMatrix(STRING_FORMAT);
        resNormalAlg.printfTime("Normal");
    }

    static void differentSizes(){
        int matrixSize[] = {10, 100, 250, 500, 1000, 1500, 2000};

        for (int size:
             matrixSize) {
            Matrix m1 = new Matrix(size,size);
            m1.randomMatrixFilling(MAX_RANDOM_VALUE);
            Matrix m2 = new Matrix(size,size);
            m2.randomMatrixFilling(MAX_RANDOM_VALUE);
            int threadNum = Runtime.getRuntime().availableProcessors();
            System.out.println(size+"x"+size+":");
            StripedAlgorithm stripAlg = new StripedAlgorithm(m1,m2);
            Result resStripAlg = stripAlg.multiplyMatrix();
            resStripAlg.printfTime("Striped");
            FoxAlgorithm FoxAlg = new FoxAlgorithm(m1,m2,threadNum);
            Result resFoxAlg = FoxAlg.multiplyMatrix();
            resFoxAlg.printfTime("Fox");
            Normal normalAlg = new Normal(m1, m2);
            Result resNormalAlg = normalAlg.multiplyMatrix();
            resNormalAlg.printfTime("Normal");
            System.out.println();
        }
    }

    static void differentThreads(){

    }
}