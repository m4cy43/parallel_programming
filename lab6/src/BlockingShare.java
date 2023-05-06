import mpi.*;
public class BlockingShare {
    private static final int SIZE = 1000;
    private static final int MASTER = 0;
    private static final int FROM_MASTER = 1;
    private static final int FROM_WORKER = 2;
    private static final boolean drawFlag = false;
    public static void main(String[] args) throws MPIException {
        double[][] matrixA = Matrix.staticMatrix(SIZE);
        double[][] matrixB = Matrix.staticMatrix(SIZE);
        double[][] resultMatrix = new double[SIZE][SIZE];

        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        int workersNum = size - 1;

        if (size < 2 || SIZE < workersNum) {
            System.out.println("Not enough workers");
            MPI.COMM_WORLD.Abort(1);
            System.exit(1);
        }
        
        if (rank == MASTER) {
            System.out.printf("Matrix size: %d Task number: %d\n", SIZE, size);
            double time1 = System.nanoTime();
            int partOfWork = SIZE / workersNum;
            int extraWork = SIZE % workersNum;
            for (int worker = 1; worker <= workersNum; worker++) {
                int startIndex = (worker - 1) * partOfWork;
                int endIndex = startIndex + partOfWork;
                if (worker == workersNum) {
                    endIndex += extraWork;
                }
                MPI.COMM_WORLD.Send(new int[]{startIndex}, 0,  1, MPI.INT, worker, FROM_MASTER);
                MPI.COMM_WORLD.Send(new int[]{endIndex}, 0,  1, MPI.INT, worker, FROM_MASTER);

                double[][] submatrixA = Matrix.getSubmatrix(matrixA, startIndex, endIndex, SIZE);
                MPI.COMM_WORLD.Send(submatrixA, 0, submatrixA.length , MPI.OBJECT, worker, FROM_MASTER);
                MPI.COMM_WORLD.Send(matrixB, 0, matrixB.length , MPI.OBJECT, worker, FROM_MASTER);
            }

            for (int worker = 1; worker <= workersNum; worker++) {
                int[] startIndex = new int[1];
                int[] endIndex = new int[1];
                MPI.COMM_WORLD.Recv(startIndex, 0, 1, MPI.INT, worker, FROM_WORKER);
                MPI.COMM_WORLD.Recv(endIndex, 0, 1, MPI.INT, worker, FROM_WORKER);

                int submatrixSize = endIndex[0] - startIndex[0] + 1;
                double[][] multipliedSubmatrix = new double[submatrixSize][SIZE];
                MPI.COMM_WORLD.Recv(
                    multipliedSubmatrix, 0, multipliedSubmatrix.length, MPI.OBJECT, worker, FROM_WORKER
                );
                Matrix.addSubmatrix(multipliedSubmatrix, resultMatrix, startIndex[0], endIndex[0]);
            }
            double time2 = System.nanoTime();
            System.out.printf("Algorithm time: %.2fms\n", (time2 - time1) / 1000000);
            if (drawFlag) {
                Matrix.draw(resultMatrix);
            }
        } else {
            int[] startIndex = new int[1];
            int[] endIndex = new int[1];
            MPI.COMM_WORLD.Recv(startIndex, 0, 1, MPI.INT, MASTER, FROM_MASTER);
            MPI.COMM_WORLD.Recv(endIndex, 0, 1, MPI.INT, MASTER, FROM_MASTER);
            System.out.printf("Start:%d, End:%d, Worker:%d\n", startIndex[0], endIndex[0], rank);

            double[][] recvSubmatrixA = new double[endIndex[0] - startIndex[0] + 1][SIZE];
            double[][] recvMatrixB = new double[SIZE][SIZE];
            MPI.COMM_WORLD.Recv(recvSubmatrixA, 0, recvSubmatrixA.length, MPI.OBJECT, MASTER, FROM_MASTER);
            MPI.COMM_WORLD.Recv(recvMatrixB, 0, recvMatrixB.length, MPI.OBJECT, MASTER, FROM_MASTER);

            double[][] sendResultMatrix = Matrix.multiply(recvSubmatrixA, recvMatrixB);
            MPI.COMM_WORLD.Send(startIndex, 0, 1, MPI.INT, MASTER, FROM_WORKER);
            MPI.COMM_WORLD.Send(endIndex, 0, 1, MPI.INT, MASTER, FROM_WORKER);
            MPI.COMM_WORLD.Send(
                sendResultMatrix, 0, sendResultMatrix.length, MPI.OBJECT, MASTER, FROM_WORKER
            );
        }
        MPI.Finalize();
    }
}