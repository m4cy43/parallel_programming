import java.io.*;
import java.net.*;
public class Client {
    private static final int SIZE = 100;
    private static final int threadsNum = 4;
    private static final int PORT = 5000;
    private static final boolean printFlag = true;
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", PORT);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
        ) {
            System.out.println("Client connected to the server on port " + PORT + "...");
            System.out.println("Choose data location: Server(s) / Client(c)...");
            while (!socket.isOutputShutdown()) {
                if (bufferedReader.ready()) {
                    String clientCommand = bufferedReader.readLine();
                    if (clientCommand.equals("s")) {
                        long time1 = System.nanoTime();

                        output.writeObject(clientCommand);
                        output.flush();

                        output.writeObject(SIZE);
                        output.flush();

                        output.writeObject(threadsNum);
                        output.flush();

                        response(time1, input);
                    }
                    if (clientCommand.equals("c")) {
                        double[][] matrixA = Generation.lineElement(SIZE);
                        double[][] matrixB = Generation.identity(SIZE);
                        long time1 = System.nanoTime();

                        output.writeObject(clientCommand);
                        output.flush();

                        output.writeObject(SIZE);
                        output.flush();

                        output.writeObject(threadsNum);
                        output.flush();

                        output.writeObject(matrixA);
                        output.flush();

                        output.writeObject(matrixB);
                        output.flush();

                        response(time1, input);
                    }
                    System.out.println();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void response(long time1, ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        System.out.println("Waiting...");
        double[][] matrix = (double[][]) in.readObject();
        long time2 = System.nanoTime();
        System.out.println("Result:");
        if (printFlag) {
            printMatrix(matrix);
        }
        long duration = (time2 - time1) / 1000000;
        System.out.println("Algorithm duration: " + duration + " ms");
    }
    public static void printMatrix(double[][] matrix) {
        for (double[] lines : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(lines[j] + " ");
            }
            System.out.println();
        }
    }
}