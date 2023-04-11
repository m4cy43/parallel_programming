package lab4_4;

import java.io.File;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private final static int threadNum = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) {
        List<File> files = FindFile.getListOfFilesToSearch("src/lab4_4/");
        String[] keywords = {"web", "css", "html", "javascript", "http"};
        long time0 = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadNum);
        var documentSearcher = new DocumentSearcher(files, keywords);
        List<File> searchedFiles = forkJoinPool.submit(documentSearcher).join();
        long time1 = System.nanoTime();
        System.out.printf("Algorithm time: %dms\n", (time1-time0)/1000000);
        for (File file: searchedFiles) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
