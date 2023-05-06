import mpi.MPI;

public class MessageSharing {
    public static int MASTER = 0;
    public static int SIZE = 2000;
    public static boolean drawFlag = false;
    public static void main(String[] args) {
        double[][] matrixA = new double[SIZE][SIZE];
        double[][] matrixB = new double[SIZE][SIZE];
        double[][] resultMatrix = new double[SIZE][SIZE];
        double time1 = 0.0, time2 = 0.0;

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int partOfWork = SIZE / size;
        int extraWork = SIZE % size;

        if (rank == MASTER) {
            System.out.printf ("Matrix size:%d Task number:%d\n", SIZE, size);
            matrixA = Matrix.staticMatrix(SIZE);
            matrixB = Matrix.staticMatrix(SIZE);
            time1 = System.nanoTime();
        }

        int[] sendcount = new int[size];
        int[] displs = new int[size];
        displs[0] = 0;
        for (int i = 1; i < size; i++) {
            sendcount[i - 1] = partOfWork;
            displs[i] = sendcount[i - 1] + displs[i - 1];
        }
        sendcount[size - 1] = partOfWork + extraWork;
        int recvcount = sendcount[rank];

        double[][] submatrixA = new double[recvcount][SIZE];
        MPI.COMM_WORLD.Scatterv(
            matrixA, 0, sendcount, displs, MPI.OBJECT,
            submatrixA, 0, recvcount, MPI.OBJECT, 0
        );
        MPI.COMM_WORLD.Bcast(matrixB, 0, matrixB.length, MPI.OBJECT, 0);

        MPI.COMM_WORLD.Barrier();
        System.out.printf(
            "Start:%d, End:%d, Worker:%d\n",
            displs[rank], (displs[rank] + sendcount[rank]), rank
        );

        double[][] multipliedSubmatrix = Matrix.multiply(submatrixA, matrixB);
        MPI.COMM_WORLD.Gatherv(
            multipliedSubmatrix, 0, multipliedSubmatrix.length, MPI.OBJECT,
            resultMatrix, 0, sendcount, displs, MPI.OBJECT, 0
        );

        if (rank == MASTER) {
            time2 = System.nanoTime();
            System.out.printf("Algorithm time: %.2fms\n", (time2 - time1) / 1000000);
            if (drawFlag) {
                Matrix.draw(resultMatrix);
            }
        }
        MPI.Finalize();
    }
}
