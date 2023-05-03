import mpi.*;
public class NonblockingShare {
    private static final int SIZE = 750;
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

        if(rank == MASTER){
            System.out.printf("NonBlocking\nTask number: %d\n", size);
            double time1 = System.nanoTime();
            int partOfWork = SIZE / workersNum;
            int extraWork = SIZE % workersNum;
            for(int worker = 1; worker <= workersNum; worker++){
                int startIndex = (worker - 1) * partOfWork;
                int endIndex = startIndex + partOfWork;
                if(worker == workersNum){
                    endIndex += extraWork;
                }
                Request isend1 = MPI.COMM_WORLD.Isend(
                    new int[]{startIndex}, 0,  1, MPI.INT, worker, FROM_MASTER
                );
                Request isend2 =  MPI.COMM_WORLD.Isend(
                    new int[]{endIndex}, 0,  1, MPI.INT, worker, FROM_MASTER
                );
                isend1.Wait();
                isend2.Wait();

                double[][] submatrixA = Matrix.getSubmatrix(matrixA, startIndex, endIndex, SIZE);
                Request isend3 =  MPI.COMM_WORLD.Isend(
                    submatrixA, 0, submatrixA.length, MPI.OBJECT, worker, FROM_MASTER
                );
                Request isend4 = MPI.COMM_WORLD.Isend(
                    matrixB, 0, matrixB.length, MPI.OBJECT, worker, FROM_MASTER
                );
                isend3.Wait();
                isend4.Wait();
            }

            for(int worker = 1; worker <= workersNum; worker++){
                int[] startIndex = new int[1];
                int[] endIndex = new int[1];
                Request irecv1 = MPI.COMM_WORLD.Irecv(startIndex, 0, 1, MPI.INT, worker, FROM_WORKER);
                Request irecv2 = MPI.COMM_WORLD.Irecv(endIndex, 0, 1, MPI.INT, worker, FROM_WORKER);
                irecv1.Wait();
                irecv2.Wait();

                int submatrixSize = endIndex[0] - startIndex[0] + 1;
                double[][] multipliedSubmatrix = new double[submatrixSize][SIZE];
                Request irecv3 = MPI.COMM_WORLD.Irecv(
                    multipliedSubmatrix, 0, multipliedSubmatrix.length, MPI.OBJECT, worker, FROM_WORKER
                );
                irecv3.Wait();
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
            Request irecv1 = MPI.COMM_WORLD.Irecv(startIndex, 0, 1, MPI.INT, MASTER, FROM_MASTER);
            Request irecv2 = MPI.COMM_WORLD.Irecv(endIndex, 0,  1, MPI.INT, MASTER, FROM_MASTER);
            irecv1.Wait();
            irecv2.Wait();
            System.out.printf("Start:%d, End:%d, Worker:%d\n", startIndex[0], endIndex[0], rank);

            double[][] recvSubmatrixA = new double[endIndex[0] - startIndex[0] + 1][SIZE];
            double[][] recvMatrixB = new double[SIZE][SIZE];
            Request irecv3 = MPI.COMM_WORLD.Irecv(
                recvSubmatrixA, 0, recvSubmatrixA.length, MPI.OBJECT, MASTER, FROM_MASTER
            );
            Request irecv4 = MPI.COMM_WORLD.Irecv(
                recvMatrixB, 0, recvMatrixB.length, MPI.OBJECT, MASTER, FROM_MASTER
            );
            irecv3.Wait();
            irecv4.Wait();

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