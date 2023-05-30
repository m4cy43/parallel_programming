import java.io.*;
import java.net.*;
public class Server {
    private static ServerSocket serverSocket;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static final int PORT = 5000;
    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.print("Server is running on port " + PORT + "...");
            try (Socket client = serverSocket.accept()) {
                System.out.print("\nConnected...");
                output = new ObjectOutputStream(client.getOutputStream());
                input = new ObjectInputStream(client.getInputStream());
                while (client.isConnected()) {
                    System.out.println("\nServer is waiting for Client...");
                    String source = (String) input.readObject();
                    int SIZE = (int) input.readObject();
                    int threadsNum = (int) input.readObject();
                    request(source, SIZE, threadsNum);
                    output.flush();
                }
            } finally {
                input.close();
                output.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\nShutdown...");
            serverSocket.close();
        }
    }
    private static void request(String source, int matrixSize, int numOfThreads)
            throws IOException, ClassNotFoundException {
        if (source.equals("s")) {
            System.out.println("Calculation on Server has started...");
            double[][] matrixA = Generation.identicalRows(matrixSize);
            double[][] matrixB = Generation.mainDiagonal(matrixSize);
            Multiplication multiplication = new Multiplication(matrixA, matrixB, numOfThreads);
            double[][] result = multiplication.multiply();
            System.out.println("Calculation on Server has stopped...");
            output.writeObject(result);
        }
        if (source.equals("c")) {
            System.out.println("Calculation on Client has started...");
            double[][] matrixA = (double[][]) input.readObject();
            double[][] matrixB = (double[][]) input.readObject();
            Multiplication multiplication = new Multiplication(matrixA, matrixB, numOfThreads);
            double[][] result = multiplication.multiply();
            System.out.println("Calculation on Client has stopped...");
            output.writeObject(result);
        }
    }
}