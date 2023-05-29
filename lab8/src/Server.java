import java.io.*;
import java.net.*;
public class Server {
    private static ServerSocket server;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static final int PORT = 5000;
    public static void main(String[] args) throws IOException {
        try {
            server = new ServerSocket(PORT);
            System.out.print("Server is running on port " + PORT + "...");
            try (Socket client = server.accept()) {
                System.out.print("\nConnected...");
                output = new ObjectOutputStream(client.getOutputStream());
                input = new ObjectInputStream(client.getInputStream());
                while (client.isConnected()) {
                    System.out.println("\nServer is waiting for Client...");
                    String side = (String) input.readObject();
                    int SIZE = (int) input.readObject();
                    int threadsNum = (int) input.readObject();
                    request(side, SIZE, threadsNum);
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
            server.close();
        }
    }
    private static void request(String side, int matrixSize, int numOfThreads)
            throws IOException, ClassNotFoundException {
        if (side.equals("s")) {
            System.out.println("Calculation on Server has started...");
            double[][] matrixA = Generation.lineElement(matrixSize);
            double[][] matrixB = Generation.identity(matrixSize);
            Multiplication multiplication = new Multiplication(matrixA, matrixB, numOfThreads);
            double[][] result = multiplication.multiply();
            System.out.println("Calculation on Server has stopped...");
            output.writeObject(result);
        }
        if (side.equals("c")) {
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